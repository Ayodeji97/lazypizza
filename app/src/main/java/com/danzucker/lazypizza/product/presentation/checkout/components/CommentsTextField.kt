package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun CommentsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.comments_subtitle),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surfaceTint,
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 92.dp),
            // Minimum height as per requirements
            placeholder = {
                Text(
                    text = stringResource(R.string.add_comment_hint),
                    style =
                        MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Normal,
                        ),
                    color = MaterialTheme.colorScheme.surfaceTint,
                )
            },
            textStyle =
                MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal,
                ),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
            shape = RoundedCornerShape(12.dp),
            maxLines = Int.MAX_VALUE, // Allow unlimited lines for auto-expansion
            singleLine = false,
        )
    }
}

@Preview(name = "Empty")
@Composable
private fun CommentsTextFieldEmptyPreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            CommentsTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview(name = "With Short Text")
@Composable
private fun CommentsTextFieldShortTextPreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            CommentsTextField(
                value = "Please add extra napkins",
                onValueChange = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview(name = "With Long Text (Auto-Expanded)")
@Composable
private fun CommentsTextFieldLongTextPreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            CommentsTextField(
                value =
                    "Please add extra napkins and make sure the pizza is well done. " +
                        "Also, I would like the delivery to be made at the back entrance. " +
                        "Please ring the doorbell twice. Thank you!",
                onValueChange = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
