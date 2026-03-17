package com.danzucker.lazypizza.product.presentation.checkout

import app.cash.turbine.test
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.core.BaseViewModelTest
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.CartSummary
import com.danzucker.lazypizza.product.domain.model.Product
import com.danzucker.lazypizza.product.domain.model.ProductCategory
import com.danzucker.lazypizza.product.domain.order.OrderRepository
import com.danzucker.lazypizza.product.domain.product.ProductRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest : BaseViewModelTest() {
    private lateinit var cartRepository: CartRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: CheckoutViewModel

    @Before
    fun setUp() {
        cartRepository = mockk()
        orderRepository = mockk()
        productRepository = mockk()
        authRepository = mockk()

        // Default happy-path stubs — override per test as needed
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { cartRepository.getCartSummary() } returns flowOf(CartSummary(emptyList()))
        every { productRepository.getProducts() } returns flowOf(Result.Success(emptyList()))
        every { authRepository.isAuthenticated() } returns true
        every { authRepository.isAnonymous() } returns false
        every { authRepository.getCurrentUserId() } returns "user-123"

        viewModel = CheckoutViewModel(cartRepository, orderRepository, productRepository, authRepository)
    }

    // ──────────────────────────────────────────────
    // OnPickupTimeSelected
    // ──────────────────────────────────────────────

    @Test
    fun onPickupTimeSelected_earliest_setsPickupOptionToEarliestAndClearsScheduledTime() =
        runTest {
            // Switch to SCHEDULED first to set a scheduled time, then switch back
            viewModel.onAction(CheckoutAction.OnPickupTimeSelected(PickupTimeOption.SCHEDULED))
            viewModel.onAction(CheckoutAction.OnPickupTimeSelected(PickupTimeOption.EARLIEST))

            assertEquals(PickupTimeOption.EARLIEST, viewModel.state.value.pickupTimeOption)
            assertTrue(viewModel.state.value.scheduledDateTime == null)
        }

    @Test
    fun onPickupTimeSelected_scheduled_setsPickupOptionAndSendsShowDatePickerEvent() =
        runTest {
            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnPickupTimeSelected(PickupTimeOption.SCHEDULED))

                assertEquals(CheckoutEvent.ShowDatePicker, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            // Subscribe to state to get the live value (stateIn returns initialValue when unsubscribed)
            viewModel.state.test {
                assertEquals(PickupTimeOption.SCHEDULED, awaitItem().pickupTimeOption)
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnScheduleTimeClick
    // ──────────────────────────────────────────────

    @Test
    fun onScheduleTimeClick_sendsShowDatePickerEvent() =
        runTest {
            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnScheduleTimeClick)

                assertEquals(CheckoutEvent.ShowDatePicker, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnToggleOrderDetails
    // ──────────────────────────────────────────────

    @Test
    fun onToggleOrderDetails_expandsOnFirstCallAndCollapsesOnSecond() =
        runTest {
            viewModel.state.test {
                assertFalse(awaitItem().isOrderDetailsExpanded) // initial

                viewModel.onAction(CheckoutAction.OnToggleOrderDetails)
                assertTrue(awaitItem().isOrderDetailsExpanded)

                viewModel.onAction(CheckoutAction.OnToggleOrderDetails)
                assertFalse(awaitItem().isOrderDetailsExpanded)

                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnCommentChange
    // ──────────────────────────────────────────────

    @Test
    fun onCommentChange_updatesCommentInState() =
        runTest {
            viewModel.state.test {
                awaitItem() // consume initial

                viewModel.onAction(CheckoutAction.OnCommentChange("Extra spicy please"))
                assertEquals("Extra spicy please", awaitItem().comment)

                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnBackPressed
    // ──────────────────────────────────────────────

    @Test
    fun onBackPressed_sendsNavigateBackEvent() =
        runTest {
            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnBackPressed)

                assertEquals(CheckoutEvent.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnPlaceOrder — guard conditions
    // ──────────────────────────────────────────────

    @Test
    fun onPlaceOrder_emptyCart_sendsShowErrorWithoutCallingOrderRepository() =
        runTest {
            // Default setup has empty cart, so orderItems in _state is empty
            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnPlaceOrder)

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onPlaceOrder_notAuthenticated_sendsShowErrorEvent() =
        runTest {
            every { authRepository.isAuthenticated() } returns false
            val cartItem = makeCartItem()
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))

            // Collect state to trigger onStart → observeCart() → populate _state.orderItems
            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnPlaceOrder)

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onPlaceOrder_anonymousUser_sendsShowErrorEvent() =
        runTest {
            every { authRepository.isAuthenticated() } returns true
            every { authRepository.isAnonymous() } returns true
            val cartItem = makeCartItem()
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnPlaceOrder)

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnPlaceOrder — happy path
    // ──────────────────────────────────────────────

    @Test
    fun onPlaceOrder_success_navigatesToOrderConfirmationWithCorrectOrderId() =
        runTest {
            val cartItem = makeCartItem()
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))
            coEvery { orderRepository.createOrder(any()) } returns Result.Success("order-abc")
            coEvery { cartRepository.clearCart() } returns Result.Success(Unit)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnPlaceOrder)

                val event = awaitItem()
                assertTrue(event is CheckoutEvent.NavigateToOrderConfirmation)
                assertEquals("order-abc", (event as CheckoutEvent.NavigateToOrderConfirmation).orderId)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onPlaceOrder_success_resetsIsPlacingOrderToFalseAfterCompletion() =
        runTest {
            val cartItem = makeCartItem()
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))
            coEvery { orderRepository.createOrder(any()) } returns Result.Success("order-xyz")
            coEvery { cartRepository.clearCart() } returns Result.Success(Unit)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnPlaceOrder)
                advanceUntilIdle()

                assertFalse(viewModel.state.value.isPlacingOrder)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onPlaceOrder_orderRepositoryError_sendsShowErrorAndResetsIsPlacingOrder() =
        runTest {
            val cartItem = makeCartItem()
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))
            coEvery { orderRepository.createOrder(any()) } returns Result.Error(DataError.Network.SERVER_ERROR)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnPlaceOrder)

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                assertFalse(viewModel.state.value.isPlacingOrder)
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnQuantityChange
    // ──────────────────────────────────────────────

    @Test
    fun onQuantityChange_itemExistsAndRepoSucceeds_noEventEmitted() =
        runTest {
            val cartItem = makeCartItem(id = "item-1")
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))
            coEvery { cartRepository.updateQuantity("item-1", 3) } returns Result.Success(Unit)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnQuantityChange("item-1", 3))
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onQuantityChange_repoReturnsError_sendsShowErrorEvent() =
        runTest {
            val cartItem = makeCartItem(id = "item-1")
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))
            coEvery { cartRepository.updateQuantity("item-1", 3) } returns Result.Error(DataError.Network.SERVER_ERROR)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnQuantityChange("item-1", 3))

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnDeleteItem
    // ──────────────────────────────────────────────

    @Test
    fun onDeleteItem_itemExistsAndRepoSucceeds_noEventEmitted() =
        runTest {
            val cartItem = makeCartItem(id = "item-1")
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))
            coEvery { cartRepository.removeFromCart("item-1") } returns Result.Success(Unit)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnDeleteItem("item-1"))
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onDeleteItem_repoReturnsError_sendsShowErrorEvent() =
        runTest {
            val cartItem = makeCartItem(id = "item-1")
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))
            coEvery { cartRepository.removeFromCart("item-1") } returns Result.Error(DataError.Network.SERVER_ERROR)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnDeleteItem("item-1"))

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // OnAddRecommendedItem
    // ──────────────────────────────────────────────

    @Test
    fun onAddRecommendedItem_productNotInAllProducts_sendsShowErrorEvent() =
        runTest {
            // Default: productRepository returns empty list → allProducts is empty
            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnAddRecommendedItem("nonexistent-id"))

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onAddRecommendedItem_repoSucceeds_sendsShowMessageEvent() =
        runTest {
            val drinkProduct = makeProduct(id = "drink-1", category = ProductCategory.DRINKS)
            every { productRepository.getProducts() } returns flowOf(Result.Success(listOf(drinkProduct)))
            coEvery { cartRepository.addToCart(any()) } returns Result.Success(Unit)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnAddRecommendedItem("drink-1"))

                assertTrue(awaitItem() is CheckoutEvent.ShowMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onAddRecommendedItem_repoReturnsError_sendsShowErrorEvent() =
        runTest {
            val drinkProduct = makeProduct(id = "drink-1", category = ProductCategory.DRINKS)
            every { productRepository.getProducts() } returns flowOf(Result.Success(listOf(drinkProduct)))
            coEvery { cartRepository.addToCart(any()) } returns Result.Error(DataError.Network.SERVER_ERROR)

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.events.test {
                viewModel.onAction(CheckoutAction.OnAddRecommendedItem("drink-1"))

                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // onDateSelected / onTimeSelected
    // ──────────────────────────────────────────────

    @Test
    fun onDateSelected_sendsShowTimePickerEvent() =
        runTest {
            viewModel.events.test {
                viewModel.onDateSelected(System.currentTimeMillis())

                assertEquals(CheckoutEvent.ShowTimePicker, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onTimeSelected_selectedTimeInPast_sendsShowErrorAndResetsToEarliest() =
        runTest {
            viewModel.events.test {
                // Call onDateSelected inside the collector so ShowTimePicker is received, not lost
                viewModel.onDateSelected(1_000L) // epoch + 1s → far in the past
                assertEquals(CheckoutEvent.ShowTimePicker, awaitItem())

                viewModel.onTimeSelected(0, 0)
                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
            // pickupTimeOption and scheduledDateTime both reset to defaults — match initialValue so readable directly
            assertEquals(PickupTimeOption.EARLIEST, viewModel.state.value.pickupTimeOption)
            assertTrue(viewModel.state.value.scheduledDateTime == null)
        }

    @Test
    fun onTimeSelected_timeOutsideOperatingHours_sendsShowErrorEvent() =
        runTest {
            val futureDateMillis = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L

            viewModel.events.test {
                viewModel.onDateSelected(futureDateMillis)
                assertEquals(CheckoutEvent.ShowTimePicker, awaitItem())

                // 22:00 is past the 21:45 cutoff
                viewModel.onTimeSelected(22, 0)
                assertTrue(awaitItem() is CheckoutEvent.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onTimeSelected_validFutureTimeWithinOperatingHours_setsScheduledDateTimeInState() =
        runTest {
            val futureDateMillis = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L

            viewModel.events.test {
                viewModel.onDateSelected(futureDateMillis)
                assertEquals(CheckoutEvent.ShowTimePicker, awaitItem())

                // 12:00 noon is well within 10:15–21:45 — no event expected
                viewModel.onTimeSelected(12, 0)
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
            // Subscribe to state to read the live value
            viewModel.state.test {
                val state = awaitItem()
                assertNotNull(state.scheduledDateTime)
                assertEquals(PickupTimeOption.SCHEDULED, state.pickupTimeOption)
                cancelAndIgnoreRemainingEvents()
            }
        }

    // ──────────────────────────────────────────────
    // canPlaceOrder derived property
    // ──────────────────────────────────────────────

    @Test
    fun canPlaceOrder_emptyCartAndNotPlacingOrder_isFalse() {
        assertFalse(viewModel.state.value.canPlaceOrder)
    }

    @Test
    fun canPlaceOrder_itemsInCartAndNotPlacingOrder_isTrue() =
        runTest {
            val cartItem = makeCartItem()
            every { cartRepository.getCartItems() } returns flowOf(listOf(cartItem))
            every { cartRepository.getCartSummary() } returns flowOf(CartSummary(listOf(cartItem)))

            viewModel.state.test {
                advanceUntilIdle()
                cancelAndIgnoreRemainingEvents()
            }

            assertTrue(viewModel.state.value.canPlaceOrder)
        }

    // ──────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────

    private fun makeCartItem(
        id: String = "item-1",
        productId: String = "item-1",
        name: String = "Margherita",
        price: Double = 12.99,
        quantity: Int = 1,
        category: String = "Pizza",
    ) = CartItem(
        id = id,
        productId = productId,
        name = name,
        imageUrl = "",
        basePrice = price,
        quantity = quantity,
        toppings = emptyList(),
        category = category,
    )

    private fun makeProduct(
        id: String = "product-1",
        name: String = "Coke",
        price: Double = 2.99,
        category: ProductCategory = ProductCategory.DRINKS,
    ) = Product(
        id = id,
        name = name,
        price = price,
        category = category,
    )
}
