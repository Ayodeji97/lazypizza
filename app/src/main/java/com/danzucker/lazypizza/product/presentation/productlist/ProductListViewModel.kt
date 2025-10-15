package com.danzucker.lazypizza.product.presentation.productlist

import android.R.attr.category
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.presentation.util.SampleProductProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProductListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadProducts()
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
                addToCart(action.productId)
            }
            is ProductListAction.OnCategorySelected -> {
               // _state.update { it.copy(selectedCategories = action.category) }
                toggleCategory(action.category)
               // filterProducts()
            }
            is ProductListAction.OnDeleteFromCart -> {
                removeFromCart(action.productId)
            }
            ProductListAction.OnPhoneNumberClick -> {
                // Handle phone number click, e.g., open dialer
            }
            is ProductListAction.OnProductClick -> {
                // Handle product click, e.g., navigate to product details
            }
            is ProductListAction.OnQuantityChange -> {
                updateQuantity(action.productId, action.quantity)
            }
        }
    }

    private fun loadProducts() {
        // Implementation to load products - Dummy data for now
        viewModelScope.launch {
            _state.update { it.copy(isLoadingData = true) }

            // Simulate network delay
            kotlinx.coroutines.delay(500)

            val products = SampleProductProvider.getProducts()
            val categories = SampleProductProvider.getCategories()

            _state.update {
                it.copy(
                    isLoadingData = false,
                    allProducts = products,
                    customerPhoneNumber = UiText.StringResourceWithArgs(R.string.customer_phone_number),
                    filteredProducts = products,
                    categories = categories,
                    selectedCategories = emptySet()
                )
            }
        }
    }

    private fun toggleCategory(category: String) {
        _state.update { currentState ->
            val newSelectedCategories = if (category in currentState.selectedCategories) {
                // Deselect category
                currentState.selectedCategories - category
            } else {
                // Select category
                currentState.selectedCategories + category
            }
            currentState.copy(selectedCategories = newSelectedCategories)
        }
        filterProducts()
    }

    private fun filterProducts() {
        val currentState = _state.value
        val query = currentState.searchQuery.lowercase()
        val selectedCategories = currentState.selectedCategories

        val filtered = currentState.allProducts.filter { product ->
            val matchesSearch = if (query.isBlank()) {
                true
            } else {
                product.name.lowercase().contains(query) ||
                        product.description.lowercase().contains(query)
            }

            val matchesCategory = if (selectedCategories.isEmpty()) {
                true
            } else {
                product.category in selectedCategories
            }
            matchesSearch && matchesCategory
        }
        _state.update { it.copy(filteredProducts = filtered) }
    }

    private fun addToCart(productId: String) {
        _state.update { currentState ->
            val updatedProducts = currentState.allProducts.map { product ->
                if (product.id == productId) {
                    product.copy(quantityInCart = 1)
                } else {
                    product
                }
            }
            currentState.copy(allProducts = updatedProducts)
        }
        filterProducts()
    }

    private fun updateQuantity(productId: String, newQuantity: Int) {
        if (newQuantity < 0) return

        _state.update { currentState ->
            val updatedProducts = currentState.allProducts.map { product ->
                if (product.id == productId) {
                    product.copy(quantityInCart = newQuantity)
                } else {
                    product
                }
            }
            currentState.copy(allProducts = updatedProducts)
        }
        filterProducts()
    }

    private fun removeFromCart(productId: String) {
        updateQuantity(productId, 0)
    }

}