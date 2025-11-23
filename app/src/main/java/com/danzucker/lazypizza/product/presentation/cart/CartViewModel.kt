package com.danzucker.lazypizza.product.presentation.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.mappers.toCartItemUi
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CartState())

    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeCart()
                loadRecommendedAddOns()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CartState()
        )

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.BackToMenu -> {
                viewModelScope.launch {
                    eventChannel.send(CartEvent.NavigateBack)
                }
            }
            is CartAction.OnProceedToCheckout -> {
                // TODO: Implement in next milestone
                viewModelScope.launch {
                    eventChannel.send(CartEvent.ShowMessage(UiText.StringResourceWithArgs(R.string.checkout_coming_soon)))
                }
            }
            is CartAction.OnQuantityChange -> {
                updateQuantity(action.itemId, action.quantity)
            }

            is CartAction.OnAddRecommendedItem -> addRecommendedItem(action.itemId)
            is CartAction.OnDeleteItem -> deleteItem(action.itemId)
        }
    }

    private fun observeCart() {
        combine(
            cartRepository.getCartItems(),
            cartRepository.getCartSummary()
        ) { cartItems, cartSummary ->
            cartItems to cartSummary
        }.onEach { (items, summary) ->
            _state.update { currentState ->
                currentState.copy(
                    isLoadingData = false,
                    cartItems = items.map { cartItem ->
                        cartItem.toCartItemUi()
                    },
                    totalAmount = summary.total,
                )

            }
        }.launchIn(viewModelScope)
    }

    private fun loadRecommendedAddOns() {
        // TODO: Load from repository/use case
        // For now, generate random recommendations
        viewModelScope.launch {
            val recommendations = generateRandomRecommendations()
            _state.update { it.copy(recommendedAddOns = recommendations) }
        }
    }

    private fun updateQuantity(itemId: String, newQuantity: Int) {
        viewModelScope.launch {
            when (cartRepository.updateQuantity(itemId, newQuantity)) {
                is Result.Success -> Unit
                is Result.Error -> {
                    eventChannel.send(
                        CartEvent.ShowErrorMessage(
                            UiText.StringResourceWithArgs(R.string.failed_to_update_quantity)
                        )
                    )
                }
            }
        }
    }

    private fun deleteItem(itemId: String) {
        viewModelScope.launch {
            when(cartRepository.removeFromCart(itemId)) {
                is Result.Success -> Unit
                is Result.Error -> {
                    eventChannel.send(
                        CartEvent.ShowErrorMessage(
                            UiText.StringResourceWithArgs(R.string.failed_to_remove_item)
                        )
                    )
                }
            }

            // TODO: If item is sauce/drink, add back to recommendations
        }
    }

    private fun addRecommendedItem(itemId: String) {
        viewModelScope.launch {
            // TODO: Implement adding recommended item to cart
            // Remove from recommendations, add to cart
           // eventChannel.send(CartEvent.ShowMessage("Added to cart!"))
        }
    }

    private fun generateRandomRecommendations(): List<RecommendedAddOnUi> {
        // TODO: Replace with actual data from repository
        val allAddOns = listOf(
            RecommendedAddOnUi("s1", "BBQ Sauce", 0.59, ""),
            RecommendedAddOnUi("s2", "Garlic Sauce", 0.59, ""),
            RecommendedAddOnUi("s3", "Ranch Sauce", 0.59, ""),
            RecommendedAddOnUi("d1", "Coca Cola", 1.99, ""),
            RecommendedAddOnUi("d2", "Sprite", 1.99, ""),
            RecommendedAddOnUi("d3", "Vanilla Shake", 2.49, "")
        )
        return allAddOns.shuffled().take(3)
    }

}