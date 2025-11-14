package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.CartIcon
import com.danzucker.lazypizza.core.presentation.designsystem.HistoryIcon
import com.danzucker.lazypizza.core.presentation.designsystem.MenuIcon
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaGradientSolidColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.applyIf

@Composable
fun LazyPizzaNavigationRail(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surfaceTint
    ) {
        Spacer(modifier = Modifier.weight(1f)) // push items to center
        items.forEachIndexed { index, item ->
            Spacer(modifier = Modifier.height(4.dp)) // like a hack to give a little space at top of each item
            NavigationRailItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    if (item.badgeCount != null && item.badgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = LazyPizzaGradientSolidColor,
                                    contentColor = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .applyIf(selectedIndex != index) {
                                            offset(x = (4).dp, y = (-4).dp)
                                        }
                                ) {
                                    Text(
                                        text = item.badgeCount.toString(),
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        ) {
                            NavigationIcon(
                                item = item,
                                isSelected = selectedIndex == index
                            )
                        }
                    } else {
                        NavigationIcon(
                            item = item,
                            isSelected = selectedIndex == index
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.surfaceTint,
                    unselectedTextColor = MaterialTheme.colorScheme.surfaceTint,
                    indicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f)) // push items to center
    }
}

@Composable
fun NavigationIcon(
    item: BottomNavItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(28.dp)
            .applyIf(isSelected) {
                background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = CircleShape
                )
            }
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label
        )
    }
}


@Preview
@Composable
private fun LazyPizzaNavigationRailPreview() {
    LazyPizzaTheme {
        LazyPizzaNavigationRail(
            items = listOf(
                BottomNavItem(label = "Menu", icon = MenuIcon),
                BottomNavItem(label = "Cart", icon = CartIcon, badgeCount = 3),
                BottomNavItem(label = "History", icon = HistoryIcon)
            ),
            selectedIndex = 1,
            onItemSelected = {}
        )
    }
}