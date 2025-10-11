package com.danzucker.lazypizza.core.presentation.util

//import com.danzucker.notemark.R
//import com.danzucker.notemark.core.domain.util.DataError
//
//// TODO: Add the remaining error types as needed
//fun DataError.asUiText(): UiText {
//    return when(this) {
//        DataError.Local.DISK_FULL -> UiText.StringResourceWithArgs(
//            R.string.error_disk_full
//        )
//        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResourceWithArgs(
//            R.string.error_request_timeout
//        )
//        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResourceWithArgs(
//            R.string.error_too_many_requests
//        )
//        DataError.Network.NO_INTERNET -> UiText.StringResourceWithArgs(
//            R.string.error_no_internet
//        )
//        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResourceWithArgs(
//            R.string.error_payload_too_large
//        )
//        DataError.Network.SERVER_ERROR -> UiText.StringResourceWithArgs(
//            R.string.error_server_error
//        )
//        DataError.Network.SERIALIZATION -> UiText.StringResourceWithArgs(
//            R.string.error_serialization
//        )
//        DataError.Network.CONFLICT -> UiText.StringResourceWithArgs(
//            R.string.error_email_exists
//        )
//        DataError.Network.UNAUTHORIZED -> UiText.StringResourceWithArgs(
//            R.string.error_unauthorised
//        )
//        else -> UiText.StringResourceWithArgs(R.string.error_unknown)
//    }
//}