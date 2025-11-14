@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun LazyPizzaCenteredTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.background,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    navigationIcon: (@Composable () -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    color = titleColor,
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            navigationIcon?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        scrollBehavior = scrollBehavior
    )
}

@Preview
@Composable
private fun LazyPizzaTopAppBarPreview() {
    LazyPizzaTheme {
        LazyPizzaCenteredTopAppBar(
            title = stringResource(R.string.lazy_pizza),
        )
    }
}