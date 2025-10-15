package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme


@Composable
fun LazyPizzaBackground(
    modifier: Modifier = Modifier,
    topStartCornerRadius: Dp = 16.dp,
    topEndCornerRadius: Dp = 16.dp,
    bottomStartCornerRadius: Dp = 0.dp,
    bottomEndCornerRadius: Dp = 0.dp,
    topPadding: Dp = 20.dp,
    bottomPadding: Dp = 32.dp,
    horizontalStartPadding: Dp = 16.dp,
    horizontalEndPadding: Dp = 16.dp,
    centerContent: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(
            topStart = topStartCornerRadius,
            topEnd = topEndCornerRadius,
            bottomStart = bottomStartCornerRadius,
            bottomEnd = bottomEndCornerRadius
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = topPadding,
                    start = horizontalStartPadding,
                    end = horizontalEndPadding,
                    bottom = bottomPadding
                ),
            horizontalAlignment = if (centerContent) {
                Alignment.CenterHorizontally
            } else {
                Alignment.Start
            }
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun NoteMarkBackgroundPreview() {
    LazyPizzaTheme {
        LazyPizzaBackground(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.DarkGray
                )
        ) {
            Text(
                text = "Hello world!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Hello world!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}