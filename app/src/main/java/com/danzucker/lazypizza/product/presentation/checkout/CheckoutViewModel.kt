package com.danzucker.lazypizza.product.presentation.checkout

import android.text.format.DateUtils.isToday
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CheckoutViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<CheckoutEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(CheckoutState())
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
            initialValue = CheckoutState()
        )

    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.OnPickupTimeSelected -> handlePickupTimeSelected(action.option)
            CheckoutAction.OnScheduleTimeClick -> handleScheduleTimeClick()
            CheckoutAction.OnToggleOrderDetails -> handleToggleOrderDetails()
            is CheckoutAction.OnProductClick -> handleProductClick(action.productId)
            is CheckoutAction.OnQuantityChange -> handleQuantityChange(action.productId, action.quantity)
            is CheckoutAction.OnDeleteItem -> handleDeleteItem(action.productId)
            is CheckoutAction.OnAddRecommendedItem -> handleAddRecommendedItem(action.addOnId)
            is CheckoutAction.OnCommentChange -> handleCommentChange(action.comment)
            CheckoutAction.OnPlaceOrder -> handlePlaceOrder()
            CheckoutAction.OnBackPressed -> handleBackPressed()
        }
    }

    private fun loadInitialData() {
        // Calculate earliest pickup time (current time + 15 minutes)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 15)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        _state.update {
            it.copy(
                earliestPickupTime = timeFormat.format(calendar.time),
                // TODO: Load cart items from repository
                // TODO: Load recommended add-ons from repository
            )
        }
    }

    private fun handlePickupTimeSelected(option: PickupTimeOption) {
        _state.update { it.copy(pickupTimeOption = option) }

        if (option == PickupTimeOption.SCHEDULED) {
            viewModelScope.launch {
                eventChannel.send(CheckoutEvent.ShowDatePicker)
            }
        }
    }

    private fun handleScheduleTimeClick() {
        viewModelScope.launch {
            eventChannel.send(CheckoutEvent.ShowDatePicker)
        }
    }

    private fun handleToggleOrderDetails() {
        _state.update { it.copy(isOrderDetailsExpanded = !it.isOrderDetailsExpanded) }
    }

    private fun handleProductClick(productId: String) {
        // TODO: Navigate to product details if needed
    }

    private fun handleQuantityChange(productId: String, quantity: Int) {
        // TODO: Update quantity in cart repository
    }

    private fun handleDeleteItem(productId: String) {
        // TODO: Delete item from cart repository
    }

    private fun handleAddRecommendedItem(addOnId: String) {
        // TODO: Add recommended item to cart
    }

    private fun handleCommentChange(comment: String) {
        _state.update { it.copy(comment = comment) }
    }

    private fun handlePlaceOrder() {
        viewModelScope.launch {
            _state.update { it.copy(isPlacingOrder = true) }

            // TODO: Place order via repository
            // For now, just simulate success
            kotlinx.coroutines.delay(1000)

            _state.update { it.copy(isPlacingOrder = false) }
            eventChannel.send(CheckoutEvent.NavigateToOrderConfirmation)
        }
    }

    private fun handleBackPressed() {
        viewModelScope.launch {
            eventChannel.send(CheckoutEvent.NavigateBack)
        }
    }

    fun setScheduledDateTime(date: String, time: String) {
        val scheduledDateTime = if (isToday(date)) {
            time // Show only time if today
        } else {
            "$date, $time" // Show date + time if future day
        }

        _state.update {
            it.copy(
                pickupTimeOption = PickupTimeOption.SCHEDULED,
                scheduledDateTime = scheduledDateTime
            )
        }
    }

    private fun isToday(date: String): Boolean {
        val today = SimpleDateFormat("MMMM dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
        return date == today
    }
}