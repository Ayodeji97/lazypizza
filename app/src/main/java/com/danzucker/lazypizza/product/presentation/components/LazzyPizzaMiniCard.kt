package com.danzucker.lazypizza.product.presentation.components

import android.R.attr.name
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.MinusIcon
import com.danzucker.lazypizza.core.presentation.designsystem.PlusIcon
import com.danzucker.lazypizza.core.presentation.designsystem.components.CardShell
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaShadowColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.elevationLarge

@Composable
fun LazyPizzaMiniCard(
    miniCardInfo: MiniCardInfo,
    quantity: String,
    selected: Boolean,
    onClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),
        modifier = modifier
            .widthIn(min = 100.dp)
            .wrapContentHeight()
            .shadow(
                elevation = elevationLarge,
                shape = RoundedCornerShape(12.dp),
                spotColor = LazyPizzaShadowColor,
                ambientColor = LazyPizzaShadowColor
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            // .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.corn),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            Text(
                text = miniCardInfo.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
            )

            if (selected) {
                ProductSelectionSection(
                    quantity = quantity,
                    onDecreaseClick = onDecreaseClick,
                    onIncreaseClick = onIncreaseClick
                )
            } else {
                Text(
                    text = miniCardInfo.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

data class MiniCardInfo(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val imageUrl: String = ""
)

@Preview
@Composable
private fun LazyPizzaMiniCardPreview() {
    LazyPizzaTheme {
        LazyPizzaMiniCard(
            miniCardInfo = MiniCardInfo(
                id = "1",
                title = "Pizza Margherita",
                description = "Tomato, mozzarella, basil",
                price = "$12.99",
                imageUrl = ""
            ),
            selected = true,
            onClick = {},
            quantity = "1",
            onDecreaseClick = {},
            onIncreaseClick = {},
            modifier = Modifier
                .padding(20.dp)
        )
    }
}