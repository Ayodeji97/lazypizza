package com.danzucker.lazypizza.product.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.model.Product
import com.danzucker.lazypizza.product.domain.order.OrderRepository
import com.danzucker.lazypizza.product.domain.product.ProductRepository
import com.danzucker.lazypizza.product.presentation.util.formatAmount
import com.danzucker.lazypizza.product.presentation.util.formatPickupTime
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
import kotlinx.datetime.Clock
import timber.log.Timber
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.domain.mappers.toOrderItem
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.Order
import com.danzucker.lazypizza.product.domain.model.OrderStatus
import com.danzucker.lazypizza.product.domain.model.ProductCategory
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.mappers.toProductListUi
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<CheckoutEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(CheckoutState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CheckoutState()
        )

    // Cache for all products (for recommendations)
    private var allProducts = listOf<Product>()

    // Store selected date and time separately for picker dialogs
    private var selectedDateMillis: Long? = null
    private var selectedHour: Int? = null
    private var selectedMinute: Int? = null

    // Cache recommendations per cart snapshot to avoid reshuffling on every emission
    private var lastCartItemIds: Set<String> = emptySet()
    private var cachedRecommendations: List<RecommendedAddOnUi> = emptyList()

    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.OnPickupTimeSelected -> handlePickupTimeSelected(action.option)
            CheckoutAction.OnScheduleTimeClick -> handleScheduleTimeClick()
            CheckoutAction.OnToggleOrderDetails -> handleToggleOrderDetails()
            is CheckoutAction.OnProductClick -> handleProductClick(action.productId)
            is CheckoutAction.OnQuantityChange -> handleQuantityChange(action.cartItemId, action.quantity)
            is CheckoutAction.OnDeleteItem -> handleDeleteItem(action.cartItemId)
            is CheckoutAction.OnAddRecommendedItem -> handleAddRecommendedItem(action.addOnId)
            is CheckoutAction.OnCommentChange -> handleCommentChange(action.comment)
            CheckoutAction.OnPlaceOrder -> handlePlaceOrder()
            CheckoutAction.OnBackPressed -> handleBackPressed()
        }
    }

    private fun loadInitialData() {
        val earliestPickupMillis = computeEarliestPickupMillis()

        val formattedTime = formatPickupTime(earliestPickupMillis)

        _state.update {
            it.copy(earliestPickupTime = formattedTime)
        }

        loadProducts()
        observeCart()
    }

    private fun loadProducts() {
        productRepository.getProducts()
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        allProducts = result.data
                        // Invalidate cache so the next cart emission recomputes
                        // recommendations with the freshly loaded product list.
                        lastCartItemIds = emptySet()
                    }
                    is Result.Error -> {
                        Timber.w("Failed to load products for recommendations")
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCart() {
        combine(
            cartRepository.getCartItems(),
            cartRepository.getCartSummary()
        ) { cartItems, summary ->
            cartItems to summary
        }.onEach { (cartItems, summary) ->
            val orderItems = cartItems.map { cartItem ->
                val product = allProducts.find { it.id == cartItem.productId }
                val productUi = product?.toProductListUi(quantityInCart = cartItem.quantity)
                productUi?.copy(id = cartItem.id) ?: run {
                    LazyPizzaProductListUi(
                        id = cartItem.id,
                        name = cartItem.name,
                        description = "",
                        price = formatAmount(cartItem.basePrice),
                        imageUrl = cartItem.imageUrl,
                        isAvailable = true,
                        category = cartItem.category,
                        rating = 0f,
                        reviewsCount = 0,
                        isFavorite = false,
                        cardType = LazyPizzaCardType.OTHERS,
                        quantityInCart = cartItem.quantity
                    )
                }
            }

            val cartItemIds = cartItems.map { it.productId }.toSet()
            if (cartItemIds != lastCartItemIds) {
                lastCartItemIds = cartItemIds
                cachedRecommendations = allProducts
                    .filter {
                        (it.category == ProductCategory.SAUCES ||
                                it.category == ProductCategory.DRINKS) &&
                                it.id !in cartItemIds
                    }
                    .shuffled()
                    .take(5)
                    .map { product ->
                        RecommendedAddOnUi(
                            id = product.id,
                            name = product.name,
                            price = product.price,
                            imageUrl = product.imageUrl
                        )
                    }
            }
            val recommendations = cachedRecommendations
            _state.update { currentState ->
                currentState.copy(
                    orderItems = orderItems,
                    recommendedAddOns = recommendations,
                    totalAmount = summary.total
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun handlePickupTimeSelected(option: PickupTimeOption) {
        _state.update { it.copy(pickupTimeOption = option) }

        if (option == PickupTimeOption.SCHEDULED) {
            viewModelScope.launch {
                eventChannel.send(CheckoutEvent.ShowDatePicker)
            }
        } else {
            // Reset scheduled time
            selectedDateMillis = null
            selectedHour = null
            selectedMinute = null
            _state.update { it.copy(scheduledDateTime = null) }
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

    private fun handleQuantityChange(productId: String, quantity: Int) {
        viewModelScope.launch {
            // Find cart item ID (may be different from product ID if it has toppings)
            val cartItem = _state.value.orderItems.find { it.id == productId }
            if (cartItem != null) {
                when (cartRepository.updateQuantity(cartItem.id, quantity)) {
                    is Result.Success -> Unit
                    is Result.Error -> {
                        eventChannel.send(
                            CheckoutEvent.ShowError(
                                UiText.StringResource(R.string.failed_to_update_quantity)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun handleDeleteItem(productId: String) {
        viewModelScope.launch {
            val cartItem = _state.value.orderItems.find { it.id == productId }
            if (cartItem != null) {
                when (cartRepository.removeFromCart(cartItem.id)) {
                    is Result.Success -> Unit
                    is Result.Error -> {
                        eventChannel.send(
                            CheckoutEvent.ShowError(
                                UiText.StringResource(R.string.failed_to_remove_item)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun handleAddRecommendedItem(addOnId: String) {
        viewModelScope.launch {
            // Find product
            val product = allProducts.find { it.id == addOnId }
            if (product == null) {
                eventChannel.send(
                    CheckoutEvent.ShowError(
                        UiText.StringResource(R.string.failed_to_add_item)
                    )
                )
                return@launch
            }

            // Create cart item
            val cartItem = CartItem(
                id = product.id,
                productId = product.id,
                name = product.name,
                imageUrl = product.imageUrl,
                basePrice = product.price,
                quantity = 1,
                toppings = emptyList(),
                category = product.category.displayName
            )

            when (cartRepository.addToCart(cartItem)) {
                is Result.Success -> {
                    eventChannel.send(
                        CheckoutEvent.ShowMessage(
                            UiText.StringResource(R.string.item_added_to_order, arrayOf(product.name))
                        )
                    )
                }
                is Result.Error -> {
                    eventChannel.send(
                        CheckoutEvent.ShowError(
                            UiText.StringResource(R.string.failed_to_add_item)
                        )
                    )
                }
            }
        }
    }

    private fun handleCommentChange(comment: String) {
        _state.update { it.copy(comment = comment) }
    }

    private fun handlePlaceOrder() {
        viewModelScope.launch {
            // Validate order
            val currentState = _state.value

            if (currentState.orderItems.isEmpty()) {
                eventChannel.send(
                    CheckoutEvent.ShowError(
                        UiText.StringResource(R.string.empty_cart_error)
                    )
                )
                return@launch
            }

            // Check authentication
            if (!authRepository.isAuthenticated() || authRepository.isAnonymous()) {
                eventChannel.send(
                    CheckoutEvent.ShowError(
                        UiText.StringResource(R.string.signin_required_error)
                    )
                )
                return@launch
            }

            _state.update { it.copy(isPlacingOrder = true) }

            try {
                // Calculate pickup time
                val pickupTimeMillis = calculatePickupTimeMillis()
                val pickupTimeFormatted = formatPickupTime(pickupTimeMillis)

                // Validate pickup time
                val isEarliest = currentState.pickupTimeOption == PickupTimeOption.EARLIEST
                if (!isPickupTimeValid(pickupTimeMillis, skipMinimumWaitCheck = isEarliest)) {
                    _state.update { it.copy(isPlacingOrder = false) }
                    eventChannel.send(
                        CheckoutEvent.ShowError(
                            UiText.StringResource(R.string.invalid_pickup_time_error)
                        )
                    )
                    return@launch
                }

                // Get cart items snapshot from repository
                val cartItems = cartRepository.getCartItems().first()

                // Ensure user ID is available before placing order
                val userId = authRepository.getCurrentUserId()
                if (userId == null) {
                    _state.update { it.copy(isPlacingOrder = false) }
                    eventChannel.send(
                        CheckoutEvent.ShowError(
                            UiText.StringResource(R.string.signin_required_error)
                        )
                    )
                    return@launch
                }

                // Create order
                val order = Order(
                    id = "", // Will be set by repository
                    orderNumber = Order.generateOrderNumber(),
                    userId = userId,
                    items = cartItems.map { it.toOrderItem() },
                    pickupTime = pickupTimeFormatted,
                    pickupTimeMillis = pickupTimeMillis,
                    comment = currentState.comment,
                    subtotal = currentState.totalAmount,
                    tax = 0.0, // TODO: Calculate tax in future milestone
                    total = currentState.totalAmount,
                    status = OrderStatus.IN_PROGRESS,
                    createdAt = System.currentTimeMillis()
                )

                // Place order
                when (val result = orderRepository.createOrder(order)) {
                    is Result.Success -> {
                        val orderId = result.data

                        // Clear cart after successful order
                        when (cartRepository.clearCart()) {
                            is Result.Success -> {
                                Timber.d("Cart cleared successfully after order placement")
                            }
                            is Result.Error -> {
                                Timber.w("Failed to clear cart after order placement")
                                // Don't fail the order, just log
                            }
                        }

                        _state.update { it.copy(isPlacingOrder = false) }

                        // Navigate to order confirmation
                        eventChannel.send(
                            CheckoutEvent.NavigateToOrderConfirmation(
                                orderId = orderId,
                                orderNumber = order.orderNumber,
                                pickupTime = pickupTimeFormatted
                            )
                        )
                    }
                    is Result.Error -> {
                        _state.update { it.copy(isPlacingOrder = false) }
                        eventChannel.send(
                            CheckoutEvent.ShowError(
                                UiText.StringResource(R.string.order_placement_failed)
                            )
                        )
                    }
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                Timber.e(e, "Error placing order")
                _state.update { it.copy(isPlacingOrder = false) }
                eventChannel.send(
                    CheckoutEvent.ShowError(
                        UiText.StringResource(R.string.order_placement_failed)
                    )
                )
            }
        }
    }

    private fun handleBackPressed() {
        viewModelScope.launch {
            eventChannel.send(CheckoutEvent.NavigateBack)
        }
    }

    /**
     * Called when user selects a date from the DatePicker
     */
    fun onDateSelected(dateMillis: Long) {
        selectedDateMillis = dateMillis
        // After date is selected, show time picker
        viewModelScope.launch {
            eventChannel.send(CheckoutEvent.ShowTimePicker)
        }
    }

    /**
     * Called when user selects a time from the TimePicker
     */
    fun onTimeSelected(hour: Int, minute: Int) {
        // Combine date and time
        val dateMillis = selectedDateMillis ?: return

        val dateTime = combineDateTime(dateMillis, hour, minute)
        val timeMillis = dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

        // Validate time
        val now = Clock.System.now().toEpochMilliseconds()
        val earliestAllowed = now + (15 * 60 * 1000) // Current time + 15 minutes

        if (timeMillis < earliestAllowed) {
            viewModelScope.launch {
                eventChannel.send(
                    CheckoutEvent.ShowError(
                        UiText.StringResource(R.string.pickup_time_too_early_error)
                    )
                )
            }
            // Reset to EARLIEST option and clear any previously cached time
            selectedHour = null
            selectedMinute = null
            _state.update {
                it.copy(
                    pickupTimeOption = PickupTimeOption.EARLIEST,
                    scheduledDateTime = null
                )
            }
            return
        }

        // Validate time is within operating hours (10:15 - 21:45)
        if (!isWithinOperatingHours(hour, minute)) {
            viewModelScope.launch {
                eventChannel.send(
                    CheckoutEvent.ShowError(
                        UiText.StringResource(R.string.pickup_time_outside_hours_error)
                    )
                )
            }
            // Revert fully to EARLIEST so calculatePickupTimeMillis() never falls
            // back to midnight (hour=0, minute=0) from stale null values.
            selectedDateMillis = null
            selectedHour = null
            selectedMinute = null
            _state.update {
                it.copy(
                    pickupTimeOption = PickupTimeOption.EARLIEST,
                    scheduledDateTime = null
                )
            }
            return
        }

        // Validation passed — persist the selected time
        selectedHour = hour
        selectedMinute = minute

        // Format and display using the util function
        val formatted = formatPickupTime(timeMillis)
        _state.update {
            it.copy(
                pickupTimeOption = PickupTimeOption.SCHEDULED,
                scheduledDateTime = formatted
            )
        }
    }

    /**
     * Calculate pickup time in milliseconds
     */
    private fun calculatePickupTimeMillis(): Long {
        val currentState = _state.value

        return when (currentState.pickupTimeOption) {
            PickupTimeOption.EARLIEST -> {
                computeEarliestPickupMillis()
            }
            PickupTimeOption.SCHEDULED -> {
                // Use selected date and time
                val dateMillis = selectedDateMillis ?: return Clock.System.now().toEpochMilliseconds()
                val hour = selectedHour ?: 0
                val minute = selectedMinute ?: 0

                val dateTime = combineDateTime(dateMillis, hour, minute)
                dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            }
        }
    }

    /**
     * Validate pickup time is valid
     */
    private fun isPickupTimeValid(pickupTimeMillis: Long, skipMinimumWaitCheck: Boolean = false): Boolean {
        if (!skipMinimumWaitCheck) {
            val now = Clock.System.now().toEpochMilliseconds()
            val earliestAllowed = now + (15 * 60 * 1000) // Current time + 15 minutes

            if (pickupTimeMillis < earliestAllowed) {
                return false
            }
        }

        // Check if within operating hours (10:15 - 21:45)
        val pickupTime = Instant.fromEpochMilliseconds(pickupTimeMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val hour = pickupTime.hour
        val minute = pickupTime.minute

        return isWithinOperatingHours(hour, minute)
    }

    /**
     * Combine date (from DatePicker) with time (from TimePicker)
     */
    private fun combineDateTime(dateMillis: Long, hour: Int, minute: Int): LocalDateTime {
        val date = Instant.fromEpochMilliseconds(dateMillis)
            .toLocalDateTime(TimeZone.UTC)
            .date

        return date.atTime(hour, minute)
    }

    private fun handleProductClick(productId: String) {
        // TODO: Navigate to product details if needed (future milestone)
    }

    private fun isWithinOperatingHours(hour: Int, minute: Int): Boolean {
        if (hour < 10 || hour > 21) return false
        if (hour == 10 && minute < 15) return false
        if (hour == 21 && minute > 45) return false
        return true
    }

    private fun computeEarliestPickupMillis(): Long {
        val now = Clock.System.now()
        val candidate = now.toEpochMilliseconds() + (15 * 60 * 1000)
        val candidateLocal = Instant.fromEpochMilliseconds(candidate)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        if (isWithinOperatingHours(candidateLocal.hour, candidateLocal.minute)) {
            return candidate
        }

        val tz = TimeZone.currentSystemDefault()
        val candidateDate = candidateLocal.date

        // Before 10:15 → clamp to today at 10:15
        if (candidateLocal.hour < 10 || (candidateLocal.hour == 10 && candidateLocal.minute < 15)) {
            return candidateDate.atTime(10, 15).toInstant(tz).toEpochMilliseconds()
        }

        // After 21:45 → clamp to next day at 10:15
        val tomorrow = candidateDate.plus(1, DateTimeUnit.DAY)
        return tomorrow.atTime(10, 15).toInstant(tz).toEpochMilliseconds()
    }
}