package com.danzucker.lazypizza.auth.data

import android.app.Activity
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Manages Firebase Authentication
 *
 * Supports:
 * - Anonymous authentication (for guest users)
 * - Phone authentication with OTP verification
 */
class AuthManager(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    /**
     * Current user ID (or null if not signed in)
     */
    val currentUserId: String?
        get() = auth.currentUser?.uid

    /**
     * Current user
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isSignedIn: Boolean
        get() = auth.currentUser != null

    /**
     * Check if current user is anonymous
     */
    val isAnonymous: Boolean
        get() = auth.currentUser?.isAnonymous ?: false

    /**
     * Observe authentication state changes
     */
    fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    /**
     * Sign in anonymously
     *
     * This creates a temporary user account that persists across app restarts
     * but is deleted if user uninstalls the app.
     *
     * Perfect for:
     * - Testing
     * - Guest checkout
     * - Pre-registration experience
     */
    suspend fun signInAnonymously(): Result<String, DataError.Network> {
        return try {
            val result = auth.signInAnonymously().await()
            Result.Success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Timber.d("Error signing in anonymously: $e")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    /**
     * Ensure user is authenticated
     * If not signed in, sign in anonymously
     *
     * Call this before any Firestore operations that require auth
     */
    suspend fun ensureAuthenticated(): Result<String, DataError.Network> {
        return if (isSignedIn) {
            Result.Success(currentUserId!!)
        } else signInAnonymously()
    }

    /**
     * Send phone verification code
     *
     * @param phoneNumber Phone number in international format (e.g., +1234567890)
     * @param activity Activity context required for reCAPTCHA
     * @param onCodeSent Callback when code is sent successfully
     * @param onVerificationCompleted Callback when auto-verification happens
     * @param onVerificationFailed Callback when verification fails
     */
    suspend fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit,
        onVerificationCompleted: () -> Unit,
        onVerificationFailed: (DataError.Network) -> Unit
    ): Result<Unit, DataError.Network> = suspendCoroutine { continuation ->

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Timber.d("Auto-verification completed, signing in user...")
                auth.signInWithCredential(credential)
                    .addOnSuccessListener { authResult ->
                        Timber.d("Auto-verification sign-in successful: ${authResult.user?.uid}")
                        onVerificationCompleted()
                        continuation.resume(Result.Success(Unit))
                    }
                    .addOnFailureListener { e ->
                        Timber.e(e, "Auto-verification sign-in failed")
                        val error = when (e) {
                            is FirebaseAuthInvalidCredentialsException ->
                                DataError.Network.INVALID_PHONE_NUMBER
                            is FirebaseTooManyRequestsException ->
                                DataError.Network.TOO_MANY_REQUESTS
                            else -> DataError.Network.UNKNOWN
                        }
                        onVerificationFailed(error)
                        continuation.resume(Result.Error(error))
                    }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Timber.d("Phone verification failed: ${e.message}")

                val error = when (e) {
                    is FirebaseAuthInvalidCredentialsException ->
                        DataError.Network.INVALID_PHONE_NUMBER
                    is FirebaseTooManyRequestsException ->
                        DataError.Network.TOO_MANY_REQUESTS
                    is FirebaseAuthException -> when (e.errorCode) {
                        // you can refine here if you want
                        else -> DataError.Network.UNKNOWN
                    }
                    else -> DataError.Network.UNKNOWN
                }
                onVerificationFailed(error)
                continuation.resume(Result.Error(error))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Timber.d("Verification code sent. ID: $verificationId")
                onCodeSent(verificationId)
                continuation.resume(Result.Success(Unit))
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * Verify OTP code and sign in
     *
     * @param verificationId The verification ID from sendVerificationCode
     * @param code The 6-digit OTP code entered by user
     */
    suspend fun verifyCode(
        verificationId: String,
        code: String
    ): Result<String, DataError.Network> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            val result = auth.signInWithCredential(credential).await()
            Result.Success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Timber.d("Phone verification failed: ${e.message}")
            val error = when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    DataError.Network.INVALID_CODE
                is FirebaseTooManyRequestsException ->
                    DataError.Network.TOO_MANY_REQUESTS
                is FirebaseAuthException -> when (e.errorCode) {
                    "ERROR_SESSION_EXPIRED",
                    "ERROR_CODE_EXPIRED" -> DataError.Network.CODE_EXPIRED
                    else -> DataError.Network.UNKNOWN
                }
                else -> DataError.Network.UNKNOWN
            }

            Result.Error(error)
        }
    }

    /**
     * Sign out current user
     */
    fun signOut() {
        auth.signOut()
    }
}