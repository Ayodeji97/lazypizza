package com.danzucker.lazypizza.product.presentation.orderhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderItemUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderStatusUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
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
        viewModelScope.launch {
            _state.update { it.copy(isLoadingData = true) }

            // Check if user is authenticated
            authRepository.observeAuthState()
                .collect { user ->
                    val isAuthenticated = user != null && !user.isAnonymous

                    _state.update {
                        it.copy(
                            isAuthenticated = isAuthenticated,
                            orders = if (isAuthenticated) getDummyOrders() else emptyList(),
                            isLoadingData = false
                        )
                    }
                }
        }
    }

    // ✅ Dummy data for now - will replace with Firebase in Milestone 4
    private fun getDummyOrders(): List<OrderUi> {
        return listOf(
            OrderUi(
                id = "1",
                orderNumber = "#20241208001",
                date = "December 08, 14:30",
                items = listOf(
                    OrderItemUi("Margherita Pizza", 2),
                    OrderItemUi("Pepsi", 1)
                ),
                totalAmount = "$25.98",
                status = OrderStatusUi.IN_PROGRESS
            ),
            OrderUi(
                id = "2",
                orderNumber = "#20241207002",
                date = "December 07, 19:15",
                items = listOf(
                    OrderItemUi("Pepperoni Pizza", 1),
                    OrderItemUi("Garlic Bread", 1),
                    OrderItemUi("Coca Cola", 2)
                ),
                totalAmount = "$32.45",
                status = OrderStatusUi.COMPLETED
            ),
            OrderUi(
                id = "3",
                orderNumber = "#20241206003",
                date = "December 06, 12:45",
                items = listOf(
                    OrderItemUi("Hawaiian Pizza", 1),
                    OrderItemUi("Cookies Ice Cream", 1)
                ),
                totalAmount = "$18.99",
                status = OrderStatusUi.CANCELLED
            ),
            OrderUi(
                id = "4",
                orderNumber = "#20241205004",
                date = "December 05, 18:20",
                items = listOf(
                    OrderItemUi("Veggie Pizza", 2),
                    OrderItemUi("Fanta", 2)
                ),
                totalAmount = "$28.50",
                status = OrderStatusUi.COMPLETED
            )
        )
    }
}