package com.danzucker.lazypizza.product.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ProductDetailViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
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
            else -> TODO("Handle actions")
        }
    }

}