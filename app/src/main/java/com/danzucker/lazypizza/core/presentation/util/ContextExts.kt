package com.danzucker.lazypizza.core.presentation.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Traverse the Context hierarchy to find an Activity
 * Returns null if no Activity is found
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
