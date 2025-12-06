package com.danzucker.lazypizza.auth.presentation.auth.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R

@Composable
fun OtpCodeBox1(
    digit: String,
    isError: Boolean,
    onCodeChange: (String) -> Unit,
    onFocused: () -> Unit,
    modifier: Modifier = Modifier,
    isValidInput: Boolean = true,
    placeholder: String = stringResource(R.string.zero_code_placeholder),
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
) {

    var isFocused by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = digit,
        onValueChange = {
            if (isValidInput) {
                onCodeChange(it)
            }
        },
        placeholder = {
            if (!isFocused && digit.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                )
            }
        },
        modifier = modifier
            .size(width = 56.dp, height = 48.dp)
            .onFocusChanged {
                isFocused = it.isFocused
                if (it.isFocused) {
                    onFocused()
                }
            },
        textStyle = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Normal
        ),
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onAny = {
                onImeAction?.invoke()
                if (imeAction == ImeAction.Done) {
                    keyboardController?.hide()
                }
            }
        ),
        shape = RoundedCornerShape(100),
        colors = lazyPizzaOtpCodeTextFieldColors(),
    )
}


@Composable
fun lazyPizzaOtpCodeTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.surfaceTint,
        cursorColor = MaterialTheme.colorScheme.onSurface,
        errorCursorColor = MaterialTheme.colorScheme.primary,
        errorBorderColor = MaterialTheme.colorScheme.primary,
    )
}
