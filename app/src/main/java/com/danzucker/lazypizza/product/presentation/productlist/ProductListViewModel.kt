package com.danzucker.lazypizza.product.presentation.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.domain.product.ProductRepository
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.ProductCategory
import com.danzucker.lazypizza.product.presentation.mappers.getPriceAsDouble
import com.danzucker.lazypizza.product.presentation.mappers.toProductListUi
import com.danzucker.lazypizza.product.presentation.productlist.ProductListEvent.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProductListState())

    private val eventChannel = Channel<ProductListEvent>()
    val events = eventChannel.receiveAsFlow()
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadProducts()
                observeCartItemsCount()
                observeAuthState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProductListState()
        )

    fun onAction(action: ProductListAction) {
        when (action) {
            is ProductListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
                filterProducts()
            }

            is ProductListAction.OnAddToCart -> {
                addToCartFromList(action.productId)
            }

            is ProductListAction.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = action.category) }
            }

            is ProductListAction.OnDeleteFromCart -> {
                removeFromCart(action.productId)
            }

            ProductListAction.OnPhoneNumberClick -> {
                viewModelScope.launch {
                    eventChannel.send(OpenPhoneDialer("+15553217890"))
                }
            }

            is ProductListAction.OnProductClick -> {} // This is handled in the root composable (ProductListScreen)
            is ProductListAction.OnQuantityChange -> {
                updateQuantityInList(action.productId, action.quantity)
            }

            is ProductListAction.OnScrollToCategoryComplete -> onScrollComplete()
            is ProductListAction.OnUserIconClick -> handleUserIconClick()
            is ProductListAction.OnLogoutConfirmed -> handleLogout()
            is ProductListAction.OnLogoutCancelled -> handleLogoutCancelled()
        }
    }

    private fun observeAuthState() {
        authRepository.observeAuthState()
            .onEach { user ->
                _state.update {
                    it.copy(
                        isAuthenticated = user != null,
                        isAnonymous = user?.isAnonymous ?: false,
                        userPhoneNumber = user?.phoneNumber
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleUserIconClick() {
        viewModelScope.launch {
            if (state.value.isAuthenticated && !state.value.isAnonymous) {
                // User is signed in with phone → Show logout dialog
                _state.update { it.copy(showLogoutDialog = true) }
            } else {
                // User is not signed in or is anonymous → Navigate to auth
                eventChannel.send(NavigateToAuth)
            }
        }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            authRepository.signOut()
            _state.update { it.copy(showLogoutDialog = false) }
        }
    }

    private fun handleLogoutCancelled() {
        _state.update { it.copy(showLogoutDialog = false) }
    }

    private fun observeCartItemsCount() {
        cartRepository.getCartItemsCount()
            .onEach { count ->
                _state.update { it.copy(cartItemsCount = count) }
            }.launchIn(viewModelScope)
    }

    private fun loadProducts() {
        // Implementation to load products - Dummy data for now
        viewModelScope.launch {
            _state.update { it.copy(isLoadingData = true) }

            productRepository.getProducts()
                .onEach { result ->
                    when (result) {
                        is Result.Success -> {
                            val products = result.data.map { product ->
                                product.toProductListUi(quantityInCart = 0)
                            }
                            val categories = ProductCategory.entries.map { it.displayName }

                            _state.update {
                                it.copy(
                                    isLoadingData = false,
                                    allProducts = products,
                                    filteredProducts = products,
                                    categories = categories,
                                    customerPhoneNumber = UiText.StringResource(R.string.customer_phone_number)
                                )
                            }
                        }
                        is Result.Error -> {
                            _state.update { it.copy(isLoadingData = false) }
                            eventChannel.send(
                                ShowErrorMessage(
                                    UiText.StringResource(R.string.failed_to_load_products)
                                )
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun filterProducts() {
        val currentState = _state.value
        val query = currentState.searchQuery.lowercase()

        val filtered = currentState.allProducts.filter { product ->
            if (query.isBlank()) {
                true
            } else {
                product.name.lowercase().contains(query) ||
                        product.description.lowercase().contains(query)
            }
        }
        _state.update { it.copy(filteredProducts = filtered) }
    }

    fun onScrollComplete() {
        _state.update { it.copy(selectedCategory = null) }
    }

    /**
     * Add product to cart from product list
     * For drinks, sauces, ice cream - no toppings
     *
     * According to requirements:
     * - Does NOT navigate to cart
     * - Shows feedback (handled in UI with snackbar/vibration)
     * - Updates badge count
     */

    private fun addToCartFromList(productId: String) {
        viewModelScope.launch {
            val product = _state.value.allProducts.find { it.id == productId } ?: return@launch
            val cartItem = CartItem(
                id = product.id,
                productId = productId,
                name = product.name,
                imageUrl = product.imageUrl,
                basePrice = product.getPriceAsDouble(),
                quantity = 1,
                toppings = emptyList(),
                category = product.category
            )
            when (cartRepository.addToCart(cartItem)) {
                is Result.Success -> {
                    updateLocalCartQuantity(
                        productId = productId,
                        quantity = 1
                    )
                    eventChannel.send(ItemAddedToCart)
                }

                is Result.Error -> eventChannel.send(ShowErrorMessage(UiText.StringResource(R.string.failed_to_add_to_cart)))
            }
        }
    }

    /**
     * Update quantity from product list screen
     *
     * Note: This is UI-only update. Actual cart sync happens via CartViewModel
     */
    private fun updateQuantityInList(productId: String, newQuantity: Int) {
        if (newQuantity < 0) return

        viewModelScope.launch {
            updateLocalCartQuantity(productId, newQuantity)
            // TODO: In production, also update CartRepository
            // For now, cart quantities are managed by CartViewModel
        }
    }

    /**
     * Update local product list UI to reflect cart changes
     * This provides immediate visual feedback while cart syncs in background
     */
    private fun updateLocalCartQuantity(productId: String, quantity: Int) {
        _state.update { currentState ->
            val updatedProducts = currentState.allProducts.map { product ->
                if (product.id == productId) {
                    product.copy(quantityInCart = quantity)
                } else {
                    product
                }
            }
            currentState.copy(allProducts = updatedProducts)
        }
        filterProducts()
    }


    /**
     * Remove from cart (quantity becomes 0)
     */
    private fun removeFromCart(productId: String) {
        updateQuantityInList(productId, 0)
    }
}