# LazyPizza 🍕

An Android pizza ordering app built with Kotlin and Jetpack Compose. Browse the menu, customize your pizza, and place pickup orders.

---

## Demo

<!-- Add demo video link or embed below -->
> 📹 **Video** —

### Screenshots

| Home | Product Detail |
|:---:|:---:|
| <img width="300" alt="Home" src="https://github.com/user-attachments/assets/590c0b84-c6a2-4290-a4a5-a84c55c93970" /> | <img width="300" alt="Product Detail" src="https://github.com/user-attachments/assets/e65af518-2d7f-4f51-beff-53ba71131611" /> |
| **Cart** | **Checkout** |
| <img width="300" alt="Cart" src="https://github.com/user-attachments/assets/1efcb64d-e07f-4e30-a40c-a2e338de1783" /> | <img width="300" alt="Checkout" src="https://github.com/user-attachments/assets/e7144982-e2a3-45ce-9db9-d92dfbd4379e" /> |

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
