package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.danzucker.lazypizza.core.presentation.designsystem.components.RemoteImage
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaShadowColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.elevationLarge
import com.danzucker.lazypizza.product.presentation.models.MiniCardInfo

@Composable
fun LazyPizzaMiniCard(
    miniCardInfo: MiniCardInfo,
    onClick: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = miniCardInfo.quantity > 0
    val quantity = miniCardInfo.quantity

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
                RemoteImage(
                    imageUrl = miniCardInfo.imageUrl,
                    contentDescription = miniCardInfo.title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = miniCardInfo.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
            )

            if (quantity > 0) {
                ProductSelectionSection(
                    quantity = quantity.toString(),
                    onDecreaseClick = {
                        onQuantityChange(quantity - 1)
                    },
                    onIncreaseClick = {
                        if (quantity < 3) onQuantityChange(quantity + 1)
                    },
                    enableIncreaseButton = quantity <= 3
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


@Preview
@Composable
private fun LazyPizzaMiniCardPreview() {
    LazyPizzaTheme {
        LazyPizzaMiniCard(
            miniCardInfo = MiniCardInfo(
                id = "1",
                title = "Pizza Margherita",
                price = "$12.99",
                imageUrl = ""
            ),
            onClick = {},
            onQuantityChange = {},
            modifier = Modifier
                .padding(20.dp)
        )
    }
}