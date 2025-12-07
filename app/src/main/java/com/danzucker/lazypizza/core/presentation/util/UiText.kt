package com.danzucker.lazypizza.core.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UiText {
    data class DynamicString(val value: String) : UiText

    @Stable
    data class StringResource(
        @StringRes val resId: Int,
        val args: Array<Any> = arrayOf()
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    @Stable
    data class Combined(
        val format: String,
        val uiTexts: Array<UiText>
    ): UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Combined

            if (format != other.format) return false
            if (!uiTexts.contentEquals(other.uiTexts)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = format.hashCode()
            result = 31 * result + uiTexts.contentHashCode()
            return result
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
            is Combined -> {
                val strings = uiTexts.map { uiText ->
                    when(uiText) {
                        is Combined -> throw IllegalArgumentException("Can't nest combined UiTexts.")
                        is DynamicString -> uiText.value
                        is StringResource -> stringResource(uiText.resId, *uiText.args)
                    }
                }
                String.format(format, *strings.toTypedArray())
            }
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
            is Combined -> {
                val strings = uiTexts.map { uiText ->
                    when(uiText) {
                        is Combined -> throw IllegalArgumentException("Can't nest combined UiTexts.")
                        is DynamicString -> uiText.value
                        is StringResource -> context.getString(uiText.resId, *uiText.args)
                    }
                }
                String.format(format, *strings.toTypedArray())
            }
        }
    }
}