# LazyPizza Feature Scaffold

You are scaffolding a new feature for **LazyPizza** — a Kotlin + Jetpack Compose Android app following Clean Architecture and MVI. Use the patterns already established in the codebase (`orderhistory`, `checkout`, `cart`, `productlist`).

---

## Step 1 — Gather inputs

Ask the user for the following before generating any files. Wait for all answers before proceeding.

1. **Feature name** (PascalCase, e.g. `Loyalty`, `Profile`, `Coupon`) — used as the class/file name prefix
2. **Needs a repository + data layer?** (yes / no) — if yes, scaffold domain interface + Firebase stub implementation
3. **Needs navigation wiring?** (yes / no) — if yes, add a route to `NavigationRoute.kt` and a composable block to `NavigationRoot.kt`
4. **Route parameters?** (only if navigation = yes) — list any typed params the route carries (e.g. `userId: String`, `amount: Int`), or "none"

Derive:
- `featureName` = PascalCase input (e.g. `Loyalty`)
- `featureNameLower` = camelCase (e.g. `loyalty`)
- `packageBase` = `com.danzucker.lazypizza`
- `presentationPackage` = `$packageBase.product.presentation.$featureNameLower`
- `domainPackage` = `$packageBase.product.domain.$featureNameLower`
- `dataPackage` = `$packageBase.product.data`
- `sourcePath` = `app/src/main/java/com/danzucker/lazypizza`

---

## Step 2 — Generate presentation layer files

Create all five files. Follow the exact patterns shown below.

### `${featureName}Action.kt`
**Path:** `$sourcePath/product/presentation/$featureNameLower/${featureName}Action.kt`

```kotlin
package $presentationPackage

sealed interface ${featureName}Action {
    data object OnBackPressed : ${featureName}Action
    // Add feature-specific actions here
}
```

### `${featureName}Event.kt`
**Path:** `$sourcePath/product/presentation/$featureNameLower/${featureName}Event.kt`

```kotlin
package $presentationPackage

import com.danzucker.lazypizza.core.presentation.util.UiText

sealed interface ${featureName}Event {
    data object NavigateBack : ${featureName}Event
    data class ShowError(val message: UiText) : ${featureName}Event
    // Add feature-specific events here
}
```

### `${featureName}State.kt`
**Path:** `$sourcePath/product/presentation/$featureNameLower/${featureName}State.kt`

```kotlin
package $presentationPackage

data class ${featureName}State(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val hasError: Boolean
        get() = errorMessage != null
}
```

### `${featureName}ViewModel.kt`
**Path:** `$sourcePath/product/presentation/$featureNameLower/${featureName}ViewModel.kt`

```kotlin
package $presentationPackage

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

class ${featureName}ViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(${featureName}State())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ${featureName}State()
        )

    private val eventChannel = Channel<${featureName}Event>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ${featureName}Action) {
        when (action) {
            is ${featureName}Action.OnBackPressed -> handleBackPressed()
        }
    }

    private fun loadInitialData() {
        // TODO: Load data here
    }

    private fun handleBackPressed() {
        viewModelScope.launch {
            eventChannel.send(${featureName}Event.NavigateBack)
        }
    }
}
```

If the feature has a repository dependency, add it as a constructor parameter:
```kotlin
class ${featureName}ViewModel(
    private val ${featureNameLower}Repository: ${featureName}Repository
) : ViewModel() { ... }
```

### `${featureName}Screen.kt`
**Path:** `$sourcePath/product/presentation/$featureNameLower/${featureName}Screen.kt`

```kotlin
package $presentationPackage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.lazypizza.core.presentation.designsystem.components.BackButton
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaCenteredTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.collectAsEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun ${featureName}Root(
    onNavigateBack: () -> Unit,
    viewModel: ${featureName}ViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.events.collectAsEffect { event ->
        when (event) {
            is ${featureName}Event.NavigateBack -> onNavigateBack()
            is ${featureName}Event.ShowError -> { /* Show snackbar/toast */ }
        }
    }

    ${featureName}Screen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ${featureName}Screen(
    state: ${featureName}State,
    onAction: (${featureName}Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LazyPizzaCenteredTopAppBar(
                title = "${featureName}",   // TODO: replace with stringResource
                navigationIcon = {
                    BackButton(onClick = { onAction(${featureName}Action.OnBackPressed) })
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${featureName} Screen") // TODO: replace with real content
        }
    }
}

@Preview
@Composable
private fun ${featureName}ScreenPreview() {
    LazyPizzaTheme {
        ${featureName}Screen(
            state = ${featureName}State(),
            onAction = {}
        )
    }
}
```

---

## Step 3 — Generate domain layer (only if repository = yes)

### `${featureName}Repository.kt`
**Path:** `$sourcePath/product/domain/$featureNameLower/${featureName}Repository.kt`

```kotlin
package $domainPackage

import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ${featureName}Repository {
    // TODO: Define repository methods
    // Example:
    // fun getAll(): Flow<Result<List<${featureName}Model>, DataError.Network>>
    // suspend fun getById(id: String): Result<${featureName}Model?, DataError.Network>
}
```

### `Firebase${featureName}Repository.kt`
**Path:** `$sourcePath/product/data/Firebase${featureName}Repository.kt`

```kotlin
package $dataPackage

import com.danzucker.lazypizza.product.domain.$featureNameLower.${featureName}Repository
import com.google.firebase.firestore.FirebaseFirestore

class Firebase${featureName}Repository(
    private val firestore: FirebaseFirestore
) : ${featureName}Repository {
    // TODO: Implement repository methods
}
```

---

## Step 4 — Wire Koin DI

Read `app/src/main/java/com/danzucker/lazypizza/product/di/ProductModule.kt`.

Add the following registrations inside the `productModule` block:

- If repository = yes:
```kotlin
singleOf(::Firebase${featureName}Repository).bind<${featureName}Repository>()
```
- Always:
```kotlin
viewModelOf(::${featureName}ViewModel)
```

Also add the required imports.

---

## Step 5 — Wire navigation (only if navigation = yes)

### 5a — Add route to `NavigationRoute.kt`

Read `app/src/main/java/com/danzucker/lazypizza/app/navigation/NavigationRoute.kt`.

If no route params: add inside the sealed interface:
```kotlin
@Serializable
data object ${featureName} : NavigationRoute
```

If route has params (e.g. `userId: String`):
```kotlin
@Serializable
data class ${featureName}(val userId: String) : NavigationRoute
```

### 5b — Add composable to `NavigationRoot.kt`

Read `app/src/main/java/com/danzucker/lazypizza/app/navigation/NavigationRoot.kt`.

Add inside the `NavHost` block:
```kotlin
composable<NavigationRoute.${featureName}> {
    ${featureName}Root(
        onNavigateBack = navController::navigateUp
    )
}
```

Add the import for `${featureName}Root` at the top of the file.

---

## Step 6 — Summary

After writing all files, print a summary:

```
## Feature '${featureName}' scaffolded

### Files created
- presentation/${featureNameLower}/${featureName}Action.kt
- presentation/${featureNameLower}/${featureName}Event.kt
- presentation/${featureNameLower}/${featureName}State.kt
- presentation/${featureNameLower}/${featureName}ViewModel.kt
- presentation/${featureNameLower}/${featureName}Screen.kt
[- domain/${featureNameLower}/${featureName}Repository.kt]       ← if repository = yes
[- data/Firebase${featureName}Repository.kt]                     ← if repository = yes

### Files updated
- product/di/ProductModule.kt         ← ViewModel registered [+ repository registered]
[- app/navigation/NavigationRoute.kt  ← Route added]            ← if navigation = yes
[- app/navigation/NavigationRoot.kt   ← Composable wired]       ← if navigation = yes

### Next steps
1. Replace TODO comments in ${featureName}Screen with real UI
2. Replace the title string in the TopAppBar with a stringResource entry
3. [Define ${featureName}Repository methods and implement Firebase${featureName}Repository]
4. [Add the screen as a navigation target from another screen]
```
