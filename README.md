# EATByMark

**EATByMark** is a location-based Android app that helps users discover places to eat near them. 
It periodically fetches nearby venues and lets users mark favourites for quick access later. 
Designed for simplicity, the app minimizes distractions and provides lightweight, real-time updates 
for decision-making on the go.

---

### Tools
* Android Studio Meerkat | 2024.3.1 Patch 2
* Gradle: 8.11.1
* Git: version 2.47.1
* JDK: version 17.0.14
* Vim: version 9.1

---

### Tech Stack

**Languages & Frameworks**
* Kotlin 2.1.20
* Jetpack Compose (2025.04.01)
* Material 3

**Architecture & Patterns**
* Clean Architecture (with domain/usecase layers)
* MVI (Model-View-Intent) for UI

**Dependency Injection**
* Hilt 2.56.2
* Hilt Navigation Compose

**Networking & Serialization**
* Retrofit 2.12.0
* Moshi 1.15.2
* OkHttp Logging Interceptor 4.9.3

**Persistence**
* Room 2.7.1 with Paging

**Image Loading**
* Coil 2.7.0 with BlurHash support

**Testing**
* JUnit 4.13.2
* MockK 1.14.2
* Kotlinx Coroutines Test
* Espresso 3.6.1

**Crash Reporting**
* ACRA 5.12.0 (Mail reporting)

---

### Architecture

EATByMark is built using **Clean Architecture**, which separates the app into clear layers:

- **Presentation (UI)**: Built with Jetpack Compose and MVI. ViewModels expose `StateFlow` of `ViewState`, reacting to `Intent`s and handling UI updates predictably.
- **Domain**: Contains `UseCase` and `UseCaseProvider` classes which encapsulate business logic.
- **Data**: Contains repositories, local Room DAOs, and remote API clients. This layer abstracts actual data sources.
- **Core**: Provides shared resources like `CoroutineDispatchers`, `AppConfig`, and the Room database configuration.

Dependency injection across the layers is handled cleanly via **Hilt**, using modular `@Module` definitions, `@InstallIn`, custom scopes, and `@TestInstallIn` for overrides during testing.

---

### Modules Overview

- `core`: Application-wide configurations and shared singletons (e.g., Retrofit, Room, Dispatchers)
- `places`:
  - `placesaround`: Fetches venue suggestions near current location
  - `currentlocation`: Supplies device location info
  - `favouriteplaces`: Persists user-marked favourites
https://github.com/user-attachments/assets/db39fd3e-f0f3-458a-b8c0-8a1e3236c1bf

---

### Testing Overview
- Automated tests are written for the main feature key components of CurrentLocationDataSource and PlacesAroundRepository
- UI testing was done manually to confirm behavior

---



https://github.com/user-attachments/assets/f5343b84-5585-4525-80c0-a0fa454f43a7



