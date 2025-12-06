package com.danzucker.lazypizza.auth.domain.model

data class AuthUser(
    val uid: String,
    val phoneNumber: String?,
    val isAnonymous: Boolean
)
