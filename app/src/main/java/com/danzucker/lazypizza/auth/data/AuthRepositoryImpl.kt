package com.danzucker.lazypizza.auth.data

import android.app.Activity
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.EmptyResult
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

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

    override suspend fun transferGuestCart(fromUserId: String?): EmptyResult<DataError.Network> {
        val oldUserId = fromUserId ?: authManager.currentUserId
        val newUserId = authManager.currentUser?.uid

        if (oldUserId == null || newUserId == null) {
            Timber.d("Cannot transfer cart: oldUserId=$oldUserId, newUserId=$newUserId")
            return Result.Success(Unit) // No cart to transfer
        }

        if (oldUserId == newUserId) {
            Timber.d("User IDs are the same, no transfer needed")
            return Result.Success(Unit)
        }

        Timber.d("Transferring cart from $oldUserId to $newUserId")

        return when (val result = cartRepository.transferCart(
            fromUserId = oldUserId,
            toUserId = newUserId
        )) {
            is Result.Success -> {
                Timber.d("Cart transferred successfully")
                Result.Success(Unit)
            }
            is Result.Error -> {
                Timber.e("Failed to transfer cart: ${result.error}")
                Result.Error(result.error)
            }
        }
    }

    override fun signOut() {
        authManager.signOut()
    }
}