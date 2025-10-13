package com.danzucker.lazypizza.product.presentation.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProductViewModel : ViewModel() {

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
            else -> {}
        }
    }

    private fun loadProducts() {
        // Implementation to load products - Dummy data for now
        _state.update {
            it.copy(
                categories = listOf(
                    UiText.StringResourceWithArgs(R.string.pizza),
                    UiText.StringResourceWithArgs(R.string.Drinks),
                    UiText.StringResourceWithArgs(R.string.sauces),
                    UiText.StringResourceWithArgs(R.string.ice_cream),
                )
            )
        }
        val list = List(20) {
            LazyPizzaProductListUi(
                id = it.toString(),
                name = "Pizza $it",
                description = "Tomato sauce, mozzarella, mushrooms, olives, bell pepper, onion, corn",
                price = "$${(10..30).random()}",
                imageUrl = "",
                isAvailable = true,
                category = if (it % 2 == 0) "Pizza" else "Beverage",
                rating = (1..5).random().toFloat(),
                reviewsCount = (0..100).random(),
                isFavorite = it % 2 == 0,
                cardType = if (it % 2 == 0) LazyPizzaCardType.PIZZA else LazyPizzaCardType.OTHERS
            )
        }

        _state.update {
            it.copy(
                isLoadingData = false,
                hasProducts = list.isNotEmpty(),
                products = list
            )
        }
    }

}