package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.BackIcon
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(32.dp)
            .background(
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = BackIcon,
            contentDescription = stringResource(R.string.back),
        )
    }
}

@Preview
@Composable
private fun BackButtonPreview() {
    LazyPizzaTheme {
        BackButton(
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
        )
    }
}