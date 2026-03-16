# LazyPizza 🍕

An Android pizza ordering app built with Kotlin and Jetpack Compose. Browse the menu, customize your pizza, and place pickup orders.

---

## Demo

<!-- Add demo video link or embed below -->
> 📹 **Video** — _coming soon_

<!-- Add screenshots below, one per feature -->
| Home | Product Detail | Cart | Checkout |
|------|---------------|------|----------|
| _screenshot_ | _screenshot_ | _screenshot_ | _screenshot_ |

---

## Features

- Phone/OTP authentication via Firebase
- Browse pizza menu with images
- Customize pizzas with toppings
- Add to cart and manage quantities
- Checkout and place pickup orders
- Order confirmation and history

---

## Tech Stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| DI | Koin 4.1 |
| Backend | Firebase Auth + Firestore |
| Images | Coil |
| Async | Kotlin Coroutines + Flow |
| Storage | DataStore |

---

## Architecture

Clean Architecture + MVI with three layers per feature:

```
data/       → Firebase/Firestore repositories
domain/     → Use cases, models, repository interfaces
presentation/ → ViewModels, UI state, screens
```

**Data flow:** User gesture → `Action` → `ViewModel` → `StateFlow` → Compose UI

---

## Project Structure

```
app/src/main/java/com/danzucker/lazypizza/
├── app/           # Application, MainActivity, Navigation
├── auth/          # Phone/OTP authentication
├── product/
│   ├── productlist/
│   ├── productdetail/
│   ├── cart/
│   ├── checkout/
│   ├── orderconfirmation/
│   └── orderhistory/
└── core/          # Theme, shared components, domain utilities
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11+
- `google-services.json` placed in `app/`

### Build & Run

```bash
./gradlew assembleDebug       # Build debug APK
./gradlew installDebug        # Install on connected device
./gradlew test                # Unit tests
./gradlew connectedAndroidTest # Instrumented tests (requires device)
./gradlew lint                # Lint checks
```

---

## Requirements

- Min SDK: 24 (Android 7.0)
- Target SDK: 36
