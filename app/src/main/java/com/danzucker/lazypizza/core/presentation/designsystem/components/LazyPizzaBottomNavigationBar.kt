package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.CartIcon
import com.danzucker.lazypizza.core.presentation.designsystem.HistoryIcon
import com.danzucker.lazypizza.core.presentation.designsystem.MenuIcon
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaGradientSolidColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.applyIf

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int? = null
)

@Composable
fun LazyPizzaBottomNavigationBar(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
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
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.surfaceTint,
                    unselectedTextColor = MaterialTheme.colorScheme.surfaceTint,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
private fun LazyPizzaBottomNavigationBarPreview() {
    LazyPizzaTheme {
        LazyPizzaBottomNavigationBar(
            items = listOf(
                BottomNavItem(label = "Menu", icon = MenuIcon),
                BottomNavItem(label = "Cart", icon = CartIcon, badgeCount = 3),
                BottomNavItem(label = "History", icon = HistoryIcon)
            ),
            selectedIndex = 0,
            onItemSelected = {},
            modifier = Modifier
                .padding(
                    top = 50.dp
                )
        )
    }
}