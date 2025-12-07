package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.LogoutIcon
import com.danzucker.lazypizza.core.presentation.designsystem.UserIcon

@Composable
fun UserIcon(
    isAuthenticated: Boolean,
    isAnonymous: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isAuthenticated && !isAnonymous) {
                LogoutIcon
            } else {
                UserIcon
            },
            contentDescription = if (isAuthenticated && !isAnonymous) {
                stringResource(R.string.logout)
            } else {
                stringResource(R.string.sign_in)
            },
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}