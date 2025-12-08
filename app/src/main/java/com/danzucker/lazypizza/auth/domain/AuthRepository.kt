package com.danzucker.lazypizza.auth.domain

import android.app.Activity
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.EmptyResult
import com.danzucker.lazypizza.core.domain.util.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit,
        onVerificationCompleted: () -> Unit,
        onVerificationFailed: (DataError.Network) -> Unit
    ): Result<Unit, DataError.Network>

    suspend fun verifyCode(
        verificationId: String,
        code: String
    ): Result<String, DataError.Network>

    suspend fun signInAnonymously(): Result<String, DataError.Network>

    fun isAuthenticated(): Boolean

    fun isAnonymous(): Boolean

    fun getCurrentUserId(): String?

    fun observeAuthState(): Flow<FirebaseUser?>
    suspend fun transferGuestCart(fromUserId: String?): EmptyResult<DataError.Network>

    fun signOut()
}