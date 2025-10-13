package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.DeleteIcon
import com.danzucker.lazypizza.core.presentation.designsystem.MinusIcon
import com.danzucker.lazypizza.core.presentation.designsystem.PlusIcon
import com.danzucker.lazypizza.core.presentation.designsystem.components.CardShell
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaShadowColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.elevationLarge
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import java.util.Locale

@Composable
fun LazyPizzaOtherProductCard(
    lazyPizzaUi: LazyPizzaProductListUi,
    quantity: Int,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isMobilePortrait: Boolean = true,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevationLarge,
                shape = RoundedCornerShape(12.dp),
                spotColor = LazyPizzaShadowColor,
                ambientColor = LazyPizzaShadowColor
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .size(if (isMobilePortrait) 120.dp else 140.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .size(if (isMobilePortrait) 120.dp else 140.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.margherita),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 12.dp,
                            end = 16.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lazyPizzaUi.name,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (quantity != 0) {
                        CardShell(
                            onClick = onDelete,
                            modifier = Modifier
                        ) {
                            Icon(
                                imageVector = DeleteIcon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(14.dp)
                            )
                        }
                    }
                }

                if (quantity == 0) {
                    ProductAddToCardSection(
                        price = lazyPizzaUi.price,
                        onAddToCart = onAddToCart,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    ProductQuantitySection(
                        quantity = quantity.toString(),
                        totalAmount = String.format(
                            Locale.getDefault(),
                            "$%.2f",
                            quantity * lazyPizzaUi.price.removePrefix("$").toDouble(),
                        ),
                        price = lazyPizzaUi.price,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun ProductAddToCardSection(
    price: String,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                bottom = 12.dp,
                end = 16.dp
            )
    ) {
        Text(
            text = price,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        TextButton(
            onClick = onAddToCart,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.01f),
                contentColor = MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            ),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 8.dp
            )
        ) {
            Text(
                text = stringResource(R.string.add_to_cart),
            )
        }
    }
}


@Composable
private fun ProductQuantitySection(
    quantity: String,
    totalAmount: String,
    price: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                bottom = 12.dp,
                end = 16.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CardShell(
                onClick = {},
            ) {
                Icon(
                    imageVector = MinusIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .size(14.dp)
                )
            }

            Text(
                text = quantity,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            CardShell(
                onClick = {},
            ) {
                Icon(
                    imageVector = PlusIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .size(14.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = totalAmount,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "$quantity x $price",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surfaceTint,
            )
        }

    }
    
}


@Preview
@Composable
private fun LazyPizzaOtherProductCardPreview() {
    LazyPizzaTheme {
        LazyPizzaOtherProductCard(
            lazyPizzaUi = LazyPizzaProductListUi(
                id = "1",
                name = "Coca Cola",
                description = "Refreshing beverage",
                price = "$1.99",
                cardType = LazyPizzaCardType.OTHERS,
                imageUrl = "",
                isAvailable = true,
                category = "Vegetarian",
                rating = 4.5f,
                reviewsCount = 150,
                isFavorite = false
            ),
            onClick = {},
            onAddToCart = {},
            onQuantityChange = {},
            onDelete = {},
            quantity = 2,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun ProductAddToCardSectionPreview() {
    LazyPizzaTheme {
        ProductAddToCardSection(
            price = "$12.49",
            onAddToCart = {},
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun ProductQuantitySectionPreview() {
    LazyPizzaTheme {
        ProductQuantitySection(
            quantity = "2",
            totalAmount = "$24.99",
            price = "$12.49",
            modifier = Modifier
                .padding(16.dp)
        )
    }
}


