package com.danzucker.lazypizza.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * There are some problem with solution - But in this case we can go with this solution
 * If we face any problem we can always go for another advance solution
 *
 * 1. The use of local density which is like a global variable can leads to some inconsistency
 * 2. When a compose function return a value, it will never be skipped, that is it will always be recomposed
 * even though it's state doesn't change
 *
 * 3. This function can only be use only inside a composable function.
 */


@Composable
fun Modifier.negativePadding(horizontal: Dp): Modifier {
    val density = LocalDensity.current
    val px = with(density) {
        horizontal.roundToPx()
    }
    return layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints = constraints.copy(
                minWidth = constraints.minWidth + 2 * px,
                maxWidth = constraints.maxWidth + 2 * px
            )
        )

        layout(placeable.width, placeable.height) {
            placeable.place(
                x = 0,
                y = 0
            )
        }
    }
}

inline fun Modifier.applyIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier = if (condition) {
    this.then(modifier())
} else {
    this
}

