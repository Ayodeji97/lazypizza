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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
    focusRequester: FocusRequester,
    shouldRequestFocus: Boolean = false,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(digit, TextRange(digit.length)))
    }

    // Sync when the ViewModel updates the digit externally (e.g. error clear)
    LaunchedEffect(digit) {
        if (textFieldValue.text != digit) {
            textFieldValue = TextFieldValue(digit, TextRange(digit.length))
        }
    }

    LaunchedEffect(shouldRequestFocus) {
        if (shouldRequestFocus) {
            focusRequester.requestFocus()
        }
    }

    Surface(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(100),
        color =
            when {
                isError -> MaterialTheme.colorScheme.surface
                isFocused -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.surfaceVariant
            },
        border =
            BorderStroke(
                width = 1.dp,
                color =
                    when {
                        isError || isFocused -> MaterialTheme.colorScheme.primary
                        else -> Color.Transparent
                    },
            ),
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                when {
                    // Clear box (backspace or delete)
                    newValue.text.isEmpty() -> {
                        textFieldValue = TextFieldValue("")
                        onCodeChange("")
                    }

                    // User typed a digit into an empty box
                    newValue.text.length == 1 &&
                        isValidInput &&
                        newValue.text.all { it.isDigit() } -> {
                        textFieldValue = TextFieldValue(newValue.text, TextRange(1))
                        onCodeChange(newValue.text)
                    }

                    // User typed a new digit into a filled box — take the new character
                    newValue.text.length == 2 &&
                        isValidInput &&
                        newValue.text.last().isDigit() -> {
                        val replacement = newValue.text.last().toString()
                        textFieldValue = TextFieldValue(replacement, TextRange(1))
                        onCodeChange(replacement)
                    }

                    // Ignore everything else (pastes, letters, etc.)
                }
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (focusState.isFocused) {
                            onFocused()
                            // Move cursor to end on focus — no selection to avoid highlight handles
                            textFieldValue = TextFieldValue(digit, TextRange(digit.length))
                        }
                    },
            textStyle =
                MaterialTheme.typography.titleSmall.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal,
                ),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction,
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = {
                        onImeAction?.invoke()
                    },
                    onDone = {
                        keyboardController?.hide()
                    },
                ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (digit.isEmpty() && !isFocused) {
                        Text(
                            text = placeholder,
                            style =
                                MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                ),
                            color = MaterialTheme.colorScheme.surfaceTint,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

@Preview(name = "Empty")
@Composable
private fun OtpCodeBoxEmptyPreview() {
    LazyPizzaTheme {
        val focusRequester = remember { FocusRequester() }
        OtpCodeBox(
            digit = "",
            isError = false,
            onCodeChange = {},
            onFocused = {},
            modifier = Modifier.padding(16.dp),
            focusRequester = focusRequester,
        )
    }
}

@Preview(name = "Filled")
@Composable
private fun OtpCodeBoxFilledPreview() {
    LazyPizzaTheme {
        val focusRequester = remember { FocusRequester() }
        OtpCodeBox(
            digit = "1",
            isError = false,
            onCodeChange = {},
            onFocused = {},
            modifier = Modifier.padding(16.dp),
            focusRequester = focusRequester,
        )
    }
}

@Preview(name = "Error")
@Composable
private fun OtpCodeBoxErrorPreview() {
    LazyPizzaTheme {
        val focusRequester = remember { FocusRequester() }
        OtpCodeBox(
            digit = "5",
            isError = true,
            onCodeChange = {},
            onFocused = {},
            modifier = Modifier.padding(16.dp),
            focusRequester = focusRequester,
        )
    }
}
