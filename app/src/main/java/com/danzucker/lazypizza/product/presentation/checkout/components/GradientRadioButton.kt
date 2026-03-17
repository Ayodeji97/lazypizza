package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaButtonGradient
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun GradientRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val gradient = MaterialTheme.colorScheme.LazyPizzaButtonGradient
    val unselectedColor = MaterialTheme.colorScheme.surfaceTint

    val selectableModifier =
        if (onClick != null) {
            Modifier.clickable(
                interactionSource = interactionSource,
                indication =
                    ripple(
                        bounded = false,
                        radius = 20.dp,
                    ),
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onClick,
            )
        } else {
            Modifier
        }

    Canvas(
        modifier
            .then(selectableModifier)
            .wrapContentSize(Alignment.Center)
            .padding(2.dp)
            .requiredSize(20.dp),
    ) {
        val strokeWidth = 2.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2

        if (selected) {
            // Outer circle with gradient
            drawCircle(
                brush = gradient,
                radius = radius,
                style = Stroke(width = strokeWidth),
            )
            // Inner filled circle with gradient
            drawCircle(
                brush = gradient,
                radius = radius * 0.5f,
                style = Fill,
            )
        } else {
            // Unselected: just the outer circle
            drawCircle(
                color = unselectedColor,
                radius = radius,
                style = Stroke(width = strokeWidth),
            )
        }
    }
}

@Preview(name = "Selected")
@Composable
private fun GradientRadioButtonSelectedPreview() {
    LazyPizzaTheme {
        GradientRadioButton(
            selected = true,
            onClick = {},
        )
    }
}

@Preview(name = "Unselected")
@Composable
private fun GradientRadioButtonUnselectedPreview() {
    LazyPizzaTheme {
        GradientRadioButton(
            selected = false,
            onClick = {},
        )
    }
}
