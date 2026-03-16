package com.danzucker.lazypizza.product.presentation.orderhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.domain.order.OrderRepository
import com.danzucker.lazypizza.product.presentation.mappers.toOrderUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(OrderHistoryState())

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = OrderHistoryState()
        )

    fun onAction(action: OrderHistoryAction) {
        when (action) {
            OrderHistoryAction.SignIn -> {
                // Handled in OrderHistoryRoot
            }
            OrderHistoryAction.GoToMenu -> {
                // Handled in OrderHistoryRoot
            }
            OrderHistoryAction.Refresh -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        _state.update { it.copy(isLoadingData = true) }

        combine(
            authRepository.observeAuthState(),
            orderRepository.getOrders()
        ) { user, ordersResult ->
            val isAuthenticated = user != null && !user.isAnonymous
            val orders = when (ordersResult) {
                is Result.Success -> ordersResult.data.map { it.toOrderUi() }
                is Result.Error -> emptyList()
            }
            val errorMessage = if (ordersResult is Result.Error) UiText.StringResource(R.string.failed_to_load_orders) else null
            Triple(isAuthenticated, orders, errorMessage)
        }.onEach { (isAuthenticated, orders, errorMessage) ->
            _state.update {
                it.copy(
                    isAuthenticated = isAuthenticated,
                    orders = orders,
                    isLoadingData = false,
                    errorMessage = errorMessage
                )
            }
        }.launchIn(viewModelScope)
    }

}