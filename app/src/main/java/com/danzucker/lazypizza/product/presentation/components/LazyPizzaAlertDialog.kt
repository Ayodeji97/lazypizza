package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimarySmallButton
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun LazyPizzaAlertDialog(
    title: String,
    modifier: Modifier = Modifier,
    body: String? = null,
    confirmText: String = stringResource(R.string.ok),
    dismissText: String = stringResource(R.string.cancel),
    onDismissClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier
            .fillMaxWidth(0.9f),
        onDismissRequest = onDismissClick,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {
            PrimarySmallButton(
                text = confirmText,
                onClick = onConfirmClick
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismissClick,
            ) {
                Text(text = dismissText)
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge
            )
        },
        text = {
            body?.let {
                Text(
                    text = body
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.surface,
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun LazyPizzaAlertDialogPreview() {
    LazyPizzaTheme {
        LazyPizzaAlertDialog(
            title = "Are you sure you want\n" +
                    "to log out?",
        )
    }
}