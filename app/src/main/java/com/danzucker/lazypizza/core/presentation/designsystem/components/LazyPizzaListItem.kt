package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaShadowColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.elevationLarge


@Composable
fun LazyPizzaListItem(
    lazyPizzaUi: LazyPizzaUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ),
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
                modifier = Modifier
                    .weight(3f)
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = lazyPizzaUi.name,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = lazyPizzaUi.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceTint
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = lazyPizzaUi.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


data class LazyPizzaUi(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val isAvailable: Boolean,
    val category: String,
    val rating: Float,
    val reviewsCount: Int,
    val isFavorite: Boolean
)

@Preview
@Composable
private fun LazyPizzaListItemPreview() {
    LazyPizzaTheme {
        LazyPizzaListItem(
            lazyPizzaUi = LazyPizzaUi(
                id = 1,
                name = "Margherita",
                description = "Classic delight with 100% real mozzarella cheese",
                price = "$5.99",
                imageUrl = "",
                isAvailable = true,
                category = "Vegetarian",
                rating = 4.5f,
                reviewsCount = 150,
                isFavorite = false
            ),
            onClick = {},
            modifier = Modifier
                .padding(30.dp)
        )
    }
}