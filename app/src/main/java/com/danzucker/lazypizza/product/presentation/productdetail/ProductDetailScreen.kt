package com.danzucker.lazypizza.product.presentation.productdetail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.lazypizza.core.presentation.designsystem.components.BackButton
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.ObserveAsEvents
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailRoot(
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ProductDetailEvent.NavigateBack,
            ProductDetailEvent.NavigateBackToMenu -> onNavigateBack()
            is ProductDetailEvent.ShowErrorMessage -> {
                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
            }

            is ProductDetailEvent.ShowMessage -> {
                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
                onNavigateBack()
            }
        }
    }

    ProductDetailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    Scaffold(
        topBar = {
            LazyPizzaTopAppBar(
                navigationIcon = {
                    BackButton(
                        onClick = {
                            onAction(ProductDetailAction.OnBackClick)
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (fromWindowSizeClass(windowSizeClass = windowClass)) {
                DeviceScreenType.MOBILE_PORTRAIT -> {
                    ProductDetailPortraitContent(
                        state = state,
                        onAction = onAction
                    )
                }
                else -> {
                    ProductDetailLandscapeContent(
                        state = state,
                        onAction = onAction
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun ProductDetailScreenPreview() {
    LazyPizzaTheme {
        ProductDetailScreen(
            state = ProductDetailState(),
            onAction = {}
        )
    }
}