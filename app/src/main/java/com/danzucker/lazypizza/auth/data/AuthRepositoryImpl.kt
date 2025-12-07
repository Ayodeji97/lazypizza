package com.danzucker.lazypizza.auth.data

import android.app.Activity
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authManager: AuthManager,
    private val cartRepository: CartRepository
) : AuthRepository {

    override suspend fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit,
        onVerificationCompleted: () -> Unit,
        onVerificationFailed: (DataError.Network) -> Unit
    ): Result<Unit, DataError.Network> {
        return authManager.sendVerificationCode(
            phoneNumber = phoneNumber,
            activity = activity,
            onCodeSent = onCodeSent,
            onVerificationCompleted = onVerificationCompleted,
            onVerificationFailed = onVerificationFailed
        )
    }

    override suspend fun verifyCode(
        verificationId: String,
        code: String
    ): Result<String, DataError.Network> {
        return authManager.verifyCode(verificationId, code)
    }

    override suspend fun signInAnonymously(): Result<String, DataError.Network> {
        return authManager.signInAnonymously()
    }

    override fun isAuthenticated(): Boolean {
        return authManager.isSignedIn
    }

    override fun isAnonymous(): Boolean {
        return authManager.isAnonymous
    }

    override fun getCurrentUserId(): String? {
        return authManager.currentUserId
    }

    override fun observeAuthState(): Flow<FirebaseUser?> {
        return authManager.observeAuthState()
    }

    override suspend fun transferGuestCart() {
        val oldUserId = authManager.currentUserId // Anonymous user
        val newUserId = authManager.currentUser?.uid // Authenticated user

        if (oldUserId != null && newUserId != null && oldUserId != newUserId) {
            cartRepository.transferCart(
                fromUserId = oldUserId,
                toUserId = newUserId
            )
        }
    }

    override fun signOut() {
        authManager.signOut()
    }
}