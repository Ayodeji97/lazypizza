package com.danzucker.lazypizza.product.presentation.cart.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.PlusIcon
import com.danzucker.lazypizza.core.presentation.designsystem.components.CardShell
import com.danzucker.lazypizza.core.presentation.designsystem.components.RemoteImage
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaShadowColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.elevationLarge
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.util.formatAmount
import java.util.Locale

@Composable
fun RecommendedAddOnCard(
    addOn: RecommendedAddOnUi,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .width(160.dp)
            .wrapContentHeight()
            .shadow(
                elevation = elevationLarge,
                shape = RoundedCornerShape(12.dp),
                spotColor = LazyPizzaShadowColor,
                ambientColor = LazyPizzaShadowColor
            )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                RemoteImage(
                    imageUrl = addOn.imageUrl,
                    contentDescription = addOn.name,
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = addOn.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    maxLines = 1,
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = formatAmount(addOn.price),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    CardShell(
                        onClick = onAddClick,
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            imageVector = PlusIcon,
                            contentDescription = "Add to cart",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RecommendedAddOnCardPreview() {
    LazyPizzaTheme {
        RecommendedAddOnCard(
            addOn = RecommendedAddOnUi(
                id = "1",
                name = "BBQ Sauce",
                price = 0.59,
                imageUrl = ""
            ),
            onAddClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}