package com.danzucker.lazypizza.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.danzucker.lazypizza.core.presentation.designsystem.CartIcon
import com.danzucker.lazypizza.core.presentation.designsystem.HistoryIcon
import com.danzucker.lazypizza.core.presentation.designsystem.MenuIcon
import com.danzucker.lazypizza.core.presentation.designsystem.components.BottomNavItem
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBottomNavigationBar
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaNavigationRail
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import com.danzucker.lazypizza.product.presentation.cart.CartRoot
import com.danzucker.lazypizza.product.presentation.orderhistory.OrderHistoryRoot
import com.danzucker.lazypizza.product.presentation.productlist.ProductListRoot

@Composable
fun MainScaffold(
    onNavigateToProductDetails: (String) -> Unit,
    cartItemCount: Int = 0 // TODO: Will be connected to actual cart state later
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceScreenType = fromWindowSizeClass(windowSizeClass = windowClass)
    val isWideScreen = deviceScreenType != DeviceScreenType.MOBILE_PORTRAIT

    val navItems = listOf(
        BottomNavItem(label = "Menu", icon = MenuIcon),
        BottomNavItem(
            label = "Cart",
            icon = CartIcon,
            badgeCount = if (cartItemCount > 0) cartItemCount else null
        ),
        BottomNavItem(label = "History", icon = HistoryIcon)
    )

    if (isWideScreen) {
        // Wide screen layout with NavigationRail
        Row(modifier = Modifier.fillMaxSize()) {
            LazyPizzaNavigationRail(
                items = navItems,
                selectedIndex = selectedTabIndex,
                onItemSelected = { selectedTabIndex = it }
            )

            // Content area - each screen manages its own Scaffold/layout
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTabIndex) {
                    0 -> ProductListRoot(
                        onNavigateToProductDetails = onNavigateToProductDetails
                    )
                    1 -> CartRoot(
                        //onNavigateToMenu = { selectedTabIndex = 0 }
                    )
                    2 -> OrderHistoryRoot()
                }
            }
        }
    } else {
        // Mobile layout with BottomNavigationBar
        Scaffold(
           // containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                LazyPizzaBottomNavigationBar(
                    items = navItems,
                    selectedIndex = selectedTabIndex,
                    onItemSelected = { selectedTabIndex = it }
                )
            }
        ) { innerPadding ->
            // Each screen manages its own Scaffold with top bar
            // They just need to respect the bottom padding from innerPadding
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTabIndex) {
                    0 -> ProductListRoot(
                        onNavigateToProductDetails = onNavigateToProductDetails
                    )
                    1 -> CartRoot(
                        //onNavigateToMenu = { selectedTabIndex = 0 }
                    )
                    2 -> OrderHistoryRoot()
                }
            }
        }
    }
}