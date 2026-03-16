# LazyPizza Unit Test Generator

You are generating unit tests for **LazyPizza** — a Kotlin + Jetpack Compose Android pizza ordering app following Clean Architecture (domain / data / presentation), MVI, and StateFlow/Channel events.

Work through the three phases below in order.

---

## Phase 1 — Wire test infrastructure (if missing)

Read `app/build.gradle.kts`. Check whether ALL of the following test dependencies are present:
- `mockk` (io.mockk:mockk)
- `kotlinx-coroutines-test`
- `app.cash.turbine:turbine`

If any are missing, add them to `libs.versions.toml` (versions block + libraries block) and to `app/build.gradle.kts` under `testImplementation`. Use these versions:
- MockK: `1.13.12`
- kotlinx-coroutines-test: match the existing `coroutinesPlayServices` version in `libs.versions.toml`
- Turbine: `1.2.0`

Example additions to `libs.versions.toml`:
```toml
[versions]
mockk = "1.13.12"
turbine = "1.2.0"

[libraries]
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
turbine = { group = "app.cash.turbine", name = "turbine", version.ref = "turbine" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutinesPlayServices" }
```

Example additions to `app/build.gradle.kts`:
```kotlin
testImplementation(libs.mockk)
testImplementation(libs.turbine)
testImplementation(libs.kotlinx.coroutines.test)
```

---

## Phase 2 — Ensure BaseViewModelTest exists

Check for `app/src/test/java/com/danzucker/lazypizza/core/BaseViewModelTest.kt`.

If it does not exist, create it:

```kotlin
package com.danzucker.lazypizza.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest {

    protected val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUpDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }
}
```

---

## Phase 3 — Generate the test file

### 3a — Identify the target file

The user's currently open file in the IDE is shown in the system context. Use that as the target. If no file is indicated, ask the user which file to test.

Read the target file fully. Determine its **type**:
- **ViewModel** — extends `ViewModel`, has `onAction()`, `state: StateFlow`, `events: Flow`
- **UseCase** — single `invoke()` / `execute()` / named operator function, returns `Result<T,E>`
- **Repository impl** — implements a repository interface, talks to Firestore/Firebase
- **Mapper** — pure top-level extension functions (e.g. `fun X.toY(): Y`)
- **Domain model / sealed class** — data classes or sealed classes with logic

### 3b — Derive the test file path

Mirror the source path under the test source set:
- Source: `app/src/main/java/com/danzucker/lazypizza/.../<FileName>.kt`
- Test:   `app/src/test/java/com/danzucker/lazypizza/.../<FileName>Test.kt`

If a test file already exists at that path, read it first and only add missing test cases — do not overwrite existing ones.

### 3c — Write the tests

Apply the rules for each type below.

---

#### Rules for ViewModels

- Extend `BaseViewModelTest`
- Annotate class with `@OptIn(ExperimentalCoroutinesApi::class)`
- Mock every constructor dependency with `mockk<InterfaceType>()`
- Set up default mock stubs in `@Before` using `every { }` / `coEvery { }` / `every { repo.someFlow() } returns flowOf(...)`
- Instantiate the ViewModel **after** stubs are set up in `@Before`
- Collect `state` with `runTest { viewModel.state.test { ... } }` (Turbine)
- Collect `events` channel with `runTest { viewModel.events.test { ... } }` (Turbine)
- Trigger behavior via `viewModel.onAction(SomeAction)`
- Use `assertIs<ExpectedType>(awaitItem())` or `assertEquals(expected, awaitItem())` for events
- Use `cancelAndIgnoreRemainingEvents()` when you only care about specific events
- Cover ALL `onAction` branches: happy path + error path (repository returns `Result.Error`)
- Cover state guard conditions (e.g. empty cart, unauthenticated user) for `handlePlaceOrder`-style methods
- Use `advanceUntilIdle()` when testing methods that use `launchIn` or deferred coroutines

**ViewModel test skeleton:**
```kotlin
package com.danzucker.lazypizza.product.presentation.cart

import app.cash.turbine.test
import com.danzucker.lazypizza.core.BaseViewModelTest
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.product.ProductRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest : BaseViewModelTest() {

    private lateinit var cartRepository: CartRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        cartRepository = mockk()
        productRepository = mockk()

        // Default stubs — override per test when needed
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { cartRepository.getCartSummary() } returns flowOf(/* stub CartSummary */)
        every { productRepository.getProducts() } returns flowOf(Result.Success(emptyList()))

        viewModel = CartViewModel(cartRepository, productRepository)
    }

    // ... test functions
}
```

---

#### Rules for UseCases

- No `BaseViewModelTest` needed (no ViewModel scope)
- Mock repository dependencies
- Call `runTest { val result = useCase(...) }`
- Assert `result is Result.Success` or `result is Result.Error`
- One test per meaningful path

---

#### Rules for Mappers

- No mocks needed — mappers are pure functions
- Test with representative input objects
- Assert each field maps correctly
- Include an edge case (empty list, null-like defaults, zero price, etc.)

---

#### Rules for Repository impls

- Flag in a comment: *"Full integration tests require a Firestore emulator. Unit tests below mock the Firestore SDK at the collection/document level."*
- Use `mockk<CollectionReference>()`, `mockk<DocumentReference>()`, etc.
- Use `coEvery { doc.get().await() } returns mockTask(snapshot)` pattern
- Keep scope narrow — test the mapping and Result wrapping, not Firebase internals

---

### 3d — Quality checklist before writing

Before writing the file, verify mentally:
- [ ] Every `onAction` branch has at least one test
- [ ] Both `Result.Success` and `Result.Error` paths are tested for each repo call
- [ ] State guard conditions (empty collections, auth checks) are tested
- [ ] No test uses `Thread.sleep()` — use `runTest` / `advanceUntilIdle()` instead
- [ ] Turbine `test { }` blocks end with `cancelAndIgnoreRemainingEvents()` or `awaitComplete()`
- [ ] MockK stubs use `coEvery` for suspend functions, `every` for regular functions and Flows
- [ ] Test method names follow `methodName_condition_expectedBehavior` convention — no backticks (underscores only, no spaces)

---

## Output

1. Show a brief summary of what infrastructure changes were made (or "already present").
2. Show the generated test file path.
3. Write the test file using the Write tool.
4. If there are tricky behaviors worth a note (e.g. `shuffled()` in recommendations makes deterministic assertions hard), call them out briefly.
