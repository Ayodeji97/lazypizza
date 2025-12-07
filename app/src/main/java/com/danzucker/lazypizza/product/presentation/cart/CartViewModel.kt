package com.danzucker.lazypizza.product.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.domain.product.ProductRepository
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.mappers.toCartItemUi
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.Product
import com.danzucker.lazypizza.product.domain.model.ProductCategory
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
import timber.log.Timber
import kotlin.collections.map

class CartViewModel(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CartState())

    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    // Track all available add-ons (sauces and drinks)
    private val allAvailableAddOns = mutableListOf<RecommendedAddOnUi>()

    // Cache all products
    private var allProducts = listOf<Product>()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadAllAvailableAddOns()
                observeCart()
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
                    eventChannel.send(CartEvent.ShowMessage(UiText.StringResource(R.string.checkout_coming_soon)))
                }
            }
            is CartAction.OnQuantityChange -> {
                updateQuantity(action.itemId, action.quantity)
            }

            is CartAction.OnAddRecommendedItem -> addRecommendedItem(action.itemId)
            is CartAction.OnDeleteItem -> deleteItem(action.itemId)
        }
    }


    /**
     * Load all available sauces and drinks that can be recommended
     */
    private fun loadAllAvailableAddOns() {
        productRepository.getProducts()
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        allProducts = result.data

                        // Filter sauces and drinks
                        val addOns = result.data
                            .filter {
                                it.category == ProductCategory.SAUCES ||
                                        it.category == ProductCategory.DRINKS
                            }
                            .map { product ->
                                RecommendedAddOnUi(
                                    id = product.id,
                                    name = product.name,
                                    price = product.price,
                                    imageUrl = product.imageUrl
                                )
                            }

                        allAvailableAddOns.clear()
                        allAvailableAddOns.addAll(addOns)
                    }
                    is Result.Error -> {
                        // Handle error silently or show message
                        Timber.w("Failed to load add-ons: ${result.error}")
                    }
                }
            }.launchIn(viewModelScope)
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
                    recommendedAddOns = generateRecommendations(items)
                )

            }
        }.launchIn(viewModelScope)
    }

    /**
     * Generate recommendations excluding items already in cart
     * Returns 3 random items from sauces and drinks (mixed)
     */
    private fun generateRecommendations(cartItems: List<CartItem>): List<RecommendedAddOnUi> {
        // Get IDs of items already in cart
        val cartItemIds = cartItems.map { it.productId }.toSet()

        // Filter out items already in cart
        val availableForRecommendation = allAvailableAddOns.filter { addOn ->
            addOn.id !in cartItemIds
        }

        return availableForRecommendation
            .shuffled()
    }

    private fun updateQuantity(itemId: String, newQuantity: Int) {
        viewModelScope.launch {
            when (cartRepository.updateQuantity(itemId, newQuantity)) {
                is Result.Success -> Unit
                is Result.Error -> {
                    eventChannel.send(
                        CartEvent.ShowErrorMessage(
                            UiText.StringResource(R.string.failed_to_update_quantity)
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
                            UiText.StringResource(R.string.failed_to_remove_item)
                        )
                    )
                }
            }
        }
    }

    /**
     * Add recommended item to cart
     *
     * Flow:
     * 1. Find the recommended item details
     * 2. Create CartItem
     * 3. Add to cart
     * 4. Item is automatically removed from recommendations via observeCart()
     */
    private fun addRecommendedItem(itemId: String) {
        viewModelScope.launch {
            // Find the item in our available add-ons
            val addOn = allAvailableAddOns.find { it.id == itemId } ?: return@launch
            // Get full product details to get category
            val product = allProducts.find { it.id == itemId } ?: return@launch
            val cartItem = CartItem(
                id = itemId,
                productId = itemId,
                name = addOn.name,
                imageUrl = addOn.imageUrl,
                basePrice = addOn.price,
                quantity = 1,
                toppings = emptyList(),
                category = product.category.displayName
            )

            when (cartRepository.addToCart(cartItem)) {
                is Result.Success -> {
                    /**
                     * Item will be automatically removed from recommendations
                     * via observeCart() which regenerates recommendations
                     */
                    eventChannel.send(
                        CartEvent.ShowMessage(
                            UiText.DynamicString("${addOn.name} added to cart")
                        )
                    )
                }
                is Result.Error-> {
                    eventChannel.send(
                        CartEvent.ShowErrorMessage(
                            UiText.StringResource(R.string.failed_to_add_item)
                        )
                    )
                }
            }
        }
    }


}