package com.danzucker.lazypizza.core.domain.util

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        BAD_REQUEST,
        UNAUTHORIZED,
        METHOD_NOT_ALLOWED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        PERMISSION_DENIED,
        INVALID_PHONE_NUMBER,
        INVALID_CODE,
        CODE_EXPIRED,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        NOT_FOUND,
    }
}