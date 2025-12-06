package com.danzucker.lazypizza.auth.presentation.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun OtpCodeBox(
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

    Surface(
        modifier = modifier
            .height(48.dp),
        shape = RoundedCornerShape(100),
        color = when {
            isError -> MaterialTheme.colorScheme.surface
            isFocused -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        border = BorderStroke(
            width = 1.dp,
            color = when {
                isError -> MaterialTheme.colorScheme.primary
                isFocused -> MaterialTheme.colorScheme.primary
                else -> Color.Transparent
            }
        )
    ) {
        BasicTextField(
            value = digit,
            onValueChange = {
                if (isValidInput && it.length <= 1 && it.all { char -> char.isDigit() }) {
                    onCodeChange(it)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged {
                    isFocused = it.isFocused
                    if (it.isFocused) {
                        onFocused()
                    }
                },
            textStyle = MaterialTheme.typography.titleSmall.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
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
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (digit.isEmpty() && !isFocused) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}


@Preview(name = "Empty")
@Composable
private fun OtpCodeBoxEmptyPreview() {
    LazyPizzaTheme {
        OtpCodeBox(
            digit = "",
            isError = false,
            onCodeChange = {},
            onFocused = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Filled")
@Composable
private fun OtpCodeBoxFilledPreview() {
    LazyPizzaTheme {
        OtpCodeBox(
            digit = "1",
            isError = false,
            onCodeChange = {},
            onFocused = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Error")
@Composable
private fun OtpCodeBoxErrorPreview() {
    LazyPizzaTheme {
        OtpCodeBox(
            digit = "5",
            isError = true,
            onCodeChange = {},
            onFocused = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}