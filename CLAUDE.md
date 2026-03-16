# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

LazyPizza is an Android pizza ordering app built with Kotlin and Jetpack Compose. Users can browse products, customize orders with toppings, manage a cart, and place pickup orders via Firebase.

## Build & Development Commands

```bash
./gradlew build                  # Full build
./gradlew assembleDebug          # Debug APK
./gradlew installDebug           # Build and install on connected device
./gradlew test                   # Unit tests
./gradlew testDebug              # Debug unit tests only
./gradlew connectedAndroidTest   # Instrumented tests (requires device/emulator)
./gradlew lint                   # Run lint checks
```

Run a single test class:
```bash
./gradlew testDebug --tests "com.danzucker.lazypizza.ExampleUnitTest"
```

## Architecture

**Clean Architecture + MVI** with three layers per feature module:

- `data/` — Firebase/Firestore repositories
- `domain/` — Use cases, repository interfaces, domain models
- `presentation/` — ViewModels, UI state (`*State`), user actions (`*Action`), one-time events (`*Event`), and Compose screens/components

**Unidirectional data flow**: User gestures → `Action` → `ViewModel.onAction()` → `StateFlow` update + optional `Channel` event → Compose recompose.

## Module Structure

```
app/src/main/java/com/danzucker/lazypizza/
├── app/              # Application class (Koin init), MainActivity, Navigation
├── auth/             # Phone/OTP authentication (Firebase Auth)
├── product/          # All ordering features
│   ├── productlist/
│   ├── productdetail/
│   ├── cart/
│   ├── checkout/
│   ├── orderconfirmation/
│   └── orderhistory/
└── core/             # Shared utilities, design system, domain primitives
    ├── data/         # DataStore preferences, Firestore data seeder
    ├── domain/       # Result<T,E> wrapper, DataError types, shared interfaces
    └── presentation/ # LazyPizzaTheme, reusable Compose components
```

## Navigation

Routes are defined in `app/navigation/NavigationRoute.kt` and wired in `NavigationRoot.kt`. The start destination is `ProductList`. Route parameters are passed as typed arguments (kotlinx-serialization).

## Dependency Injection

Koin is used throughout. Each feature module exposes a Koin module. DI is initialized in `LazyPizzaApplication`. Use `singletonOf` for repositories and `viewModelOf` for ViewModels.

## Key Libraries

| Purpose | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | `navigation-compose` |
| DI | Koin 4.1.1 |
| Backend | Firebase Auth + Firestore |
| Images | Coil |
| Async | Kotlin Coroutines + Flow |
| Preferences | DataStore |
| Logging | Timber |
| Serialization | kotlinx-serialization-json |

## Code Review

Always verify that code logic is correct before considering a task complete — check edge cases, state transitions, and data flow through the MVI pipeline.

## Conventions

- `Result<T, E>` (in `core/domain`) is the standard return type for repository calls — use `Success`/`Error` branches, not exceptions.
- One-time navigation/toast events go through a `Channel<Event>` consumed in the screen via `collectAsEffect`.
- Domain models live in `domain/model/`; DTO→domain mapping is in `domain/mappers/`.
- Firestore persistent cache is enabled — offline reads will succeed.
