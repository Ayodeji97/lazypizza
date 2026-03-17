package com.danzucker.lazypizza.core.presentation.designsystem.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun LazyPizzaTextField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    // Use TextFieldValue so we can pin the cursor to end after formatting.
    // Without this, inserting "+" at position 0 shifts the cursor and causes
    // digits to appear out of order (e.g. "+513" instead of "+351").
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(phoneNumber, TextRange(phoneNumber.length)))
    }

    // Whenever the ViewModel applies formatting and updates phoneNumber, sync the
    // TextFieldValue and move the cursor to the end.
    LaunchedEffect(phoneNumber) {
        if (textFieldValue.text != phoneNumber) {
            textFieldValue = TextFieldValue(phoneNumber, TextRange(phoneNumber.length))
        }
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onPhoneNumberChange(newValue.text)
        },
        modifier =
            modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                },
        shape = RoundedCornerShape(100),
        placeholder = {
            if (!isFocused && phoneNumber.isEmpty()) {
                Text(
                    text = stringResource(R.string.phone_number_placeholder),
                    style =
                        MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Normal,
                        ),
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        readOnly = readOnly,
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.surfaceTint,
            ),
        textStyle =
            MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
    )
}

@Preview
@Composable
private fun LazyPizzaTextFieldPreview() {
    LazyPizzaTheme {
        LazyPizzaTextField(
            phoneNumber = "+1 234 567 8901",
            onPhoneNumberChange = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
        )
    }
}
