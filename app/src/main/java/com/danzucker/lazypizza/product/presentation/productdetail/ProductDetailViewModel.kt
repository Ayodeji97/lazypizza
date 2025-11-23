package com.danzucker.lazypizza.product.presentation.productdetail

import android.R.attr.category
import android.R.attr.name
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.mappers.toCartTopping
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.ToppingData
import com.danzucker.lazypizza.product.presentation.mappers.getPriceAsDouble
import com.danzucker.lazypizza.product.presentation.mappers.toToppingData
import com.danzucker.lazypizza.product.presentation.models.PizzaDetailUi
import com.danzucker.lazypizza.product.presentation.models.ToppingUi
import com.danzucker.lazypizza.product.presentation.productlist.ProductListEvent.FailedToAddToCart
import com.danzucker.lazypizza.product.presentation.util.SampleProductProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProductDetailState())

    private val eventChannel = Channel<ProductDetailEvent>()
    val events = eventChannel.receiveAsFlow()
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadProductDetail()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProductDetailState()
        )

    fun onAction(action: ProductDetailAction) {
        when (action) {
            is ProductDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(ProductDetailEvent.NavigateBack)
                }
            }
            is ProductDetailAction.OnAddToCartClick -> {
                addPizzaToCart()
            }
            is ProductDetailAction.OnToppingClick -> {
                toggleTopping(action.toppingId)
            }
            is ProductDetailAction.OnToppingQuantityChange -> {
                updateToppingQuantity(action.toppingId, action.quantity)
            }
        }
    }

    private fun loadProductDetail() {
        // Load product detail logic here
        val productId = savedStateHandle.get<String>("productId") ?: "1"
        val product = SampleProductProvider.getProductById(productId)

        if (product == null) {
            _state.update { it.copy(isLoadingData = false) }
            return
        }

        // Load pizza details (dummy data for now)
        val pizza = PizzaDetailUi(
            id = product.id,
            name = product.name,
            description = product.description,
            basePrice = product.getPriceAsDouble(),
                //product.price.removePrefix("$").toDoubleOrNull() ?: 0.0,
            imageUrl = product.imageUrl, // Use the actual imageUrl from the product
            imageResId = R.drawable.margherita, // Keep as fallback if needed
            ingredients = product.description, // Or create a separate ingredients field
            category = product.category
        )

        val toppings = SampleProductProvider.getToppings()
        _state.update {
            it.copy(
                isLoadingData = false,
                pizzaDetail = pizza,
                availableToppings = toppings,
                basePizzaPrice = pizza.basePrice,
                totalPrice = pizza.basePrice
            )
        }
    }

    private fun toggleTopping(toppingId: String) {
        val currentState = _state.value
        val currentQuantity = currentState.selectedToppings[toppingId] ?: 0

        if (currentQuantity == 0) {
            // Add topping with quantity 1
            updateToppingQuantity(toppingId, 1)
        } else {
            // Remove topping
            updateToppingQuantity(toppingId, 0)
        }
    }

    private fun updateToppingQuantity(toppingId: String, newQuantity: Int) {
        val topping = _state.value.availableToppings.find { it.id == toppingId } ?: return

        val validQuantity = newQuantity.coerceIn(0, topping.maxQuantity)

        _state.update { currentState ->
            val updatedToppings = if (validQuantity == 0) {
                currentState.selectedToppings - toppingId
            } else {
                currentState.selectedToppings + (toppingId to validQuantity)
            }

            currentState.copy(
                selectedToppings = updatedToppings,
                totalPrice = calculateTotalPrice(
                    currentState.basePizzaPrice,
                    currentState.availableToppings,
                    updatedToppings
                )
            )
        }

    }

    private fun calculateTotalPrice(
        basePrice: Double,
        allToppings: List<ToppingUi>,
        selectedToppings: Map<String, Int>
    ): Double {
        var total = basePrice
        selectedToppings.forEach { (toppingId, quantity) ->
            val topping = allToppings.find { it.id == toppingId }
            if (topping != null) {
                val toppingPrice = topping.price.removePrefix("$").toDoubleOrNull() ?: 0.0
                total += toppingPrice * quantity
            }
        }
        return total
    }

    private fun addPizzaToCart() {
        viewModelScope.launch {
            val currentState = _state.value
            val pizza = currentState.pizzaDetail ?: return@launch

            val toppingsData = currentState.selectedToppings.mapNotNull { (toppingId, quantity) ->
                val topping = currentState.availableToppings.find { it.id == toppingId }
                topping?.let { toppingUi ->
                    toppingId to toppingUi.toToppingData(quantity)
                }
            }.toMap()

            val cartToppings = toppingsData.map { (toppingId, toppingData) ->
                toppingData.toCartTopping(toppingId)
            }

            val cartItemId = CartItem.generateId(productId = pizza.id, cartToppings)

            val cartItem = CartItem(
                id = cartItemId,
                productId = pizza.id,
                name = pizza.name,
                imageUrl = pizza.imageUrl,
                basePrice = pizza.basePrice,
                quantity = 1, // adding one quantity at a time
                toppings = cartToppings,
                category = pizza.category
            )

            when(cartRepository.addToCart(item = cartItem)) {
                is Result.Success-> eventChannel.send(ProductDetailEvent.NavigateBackToMenu)
                is Result.Error -> eventChannel.send(ProductDetailEvent.FailedToAddToCart)
            }
        }
    }

}