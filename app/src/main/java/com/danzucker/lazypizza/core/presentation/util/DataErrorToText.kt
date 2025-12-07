package com.danzucker.lazypizza.core.presentation.util

import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.domain.util.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.INVALID_PHONE_NUMBER -> UiText.StringResource(R.string.error_invalid_phone_number)
        DataError.Network.INVALID_CODE -> UiText.StringResource(R.string.error_invalid_code)
        DataError.Network.CODE_EXPIRED -> UiText.StringResource(R.string.error_code_expired)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_too_many_requests)
        // Try to add other error as needed
        else -> UiText.StringResource(R.string.error_generic)
    }
}