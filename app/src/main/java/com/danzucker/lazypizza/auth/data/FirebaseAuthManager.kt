package com.danzucker.lazypizza.auth.data

import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Manages Firebase Authentication
 *
 * For now: Uses anonymous authentication
 * Future: Can add email/password, Google Sign-In, etc.
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
            println("Error signing in anonymously: $e")
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
     * Sign out current user
     */
    fun signOut() {
        auth.signOut()
    }


}
