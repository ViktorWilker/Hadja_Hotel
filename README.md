# 🏨 Hadja Hotel — Internal Management System

An Android application built with Kotlin and Jetpack Compose for hotel staff use.
The system covers room reservations, guest management, event booking, AC maintenance quotes, vehicle fueling analysis, and operational reports.

---

## Screens

| Login | Home | Reservations |
|---|---|---|
| Authentication with lockout after 3 attempts | Main menu with access to all 6 modules | Stepper form with visual room grid |

| Guests | Guest Details | Events |
|---|---|---|
| List with prefix search and registration via bottom sheet | Profile with reservation history | Full pipeline with waiters and buffet calculation |

| Air Conditioning | Fueling | Reports |
|---|---|---|
| Automatic quote comparison | Cost-benefit analysis between stations | Consolidated session metrics |

---

## Architecture

The project follows a modular three-layer architecture with clear separation of concerns.

```
┌─────────────────────────────────────────┐
│              UI (Screens)               │
│   Jetpack Compose · no business logic   │
├─────────────────────────────────────────┤
│            ViewModel                    │
│   Reactive state · bridges UI ↔ Service │
├─────────────────────────────────────────┤
│            Services                     │
│   Pure Kotlin · no Android · testable   │
├─────────────────────────────────────────┤
│             Model                       │
│   Data classes · Enums · State          │
└─────────────────────────────────────────┘
```

### Why this separation?

The `Services` are pure Kotlin, with no Android dependencies.
This means all business logic can be developed, understood and tested completely independently from the interface.
The `HotelViewModel` acts as the bridge, keeping state alive across recompositions and exposing data via `mutableStateOf` so Compose reacts automatically to any change.

---

## Project Structure

```
app/src/main/java/com/hadjahotel/
│
├── model/
│   ├── HotelState.kt
│   ├── Reservation.kt
│   ├── Guest.kt
│   ├── Event.kt
│   ├── AcQuote.kt
│   ├── FuelResult.kt
│   ├── BuffetResult.kt
│   ├── ReportData.kt
│   ├── RoomType.kt
│   └── OperationResult.kt
│
├── service/
│   ├── AuthService.kt
│   ├── ReservationService.kt
│   ├── GuestService.kt
│   ├── EventService.kt
│   ├── AirConditioningService.kt
│   ├── FuelService.kt
│   └── ReportService.kt
│
├── viewmodel/
│   └── HotelViewModel.kt
│
├── ui/
│   ├── screen/
│   │   ├── LoginScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── ReservationScreen.kt
│   │   ├── GuestScreen.kt
│   │   ├── GuestDetailScreen.kt
│   │   ├── EventScreen.kt
│   │   ├── AirConditioningScreen.kt
│   │   ├── FuelScreen.kt
│   │   └── ReportScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── navigation/
│   └── NavGraph.kt
│
└── util/
    ├── Formatter.kt
    └── Validator.kt
```

---

## Modules

### 🛏 Room Reservations
Manages the hotel's 20 rooms.
Staff enters stay details and the system calculates the final amount based on room type (Standard, Executive or Luxury) with an automatic 10% service fee. A 4×5 visual grid displays each room's real-time availability.

```
subtotal = daily_rate × nights × room_type_factor
fee      = subtotal × 10%
total    = subtotal + fee
```

### 👥 Guest Management
Internal directory with a capacity of 15 active guests. Supports prefix search, registration, editing and removal. When a reservation is confirmed, the guest is automatically registered.
The detail screen shows the full profile with reservation history.

### 🎪 Events
Full pipeline for auditorium booking. The system automatically selects between Laranja Auditorium (up to 220 guests) and Colorado (up to 350), checks availability by time window, and calculates required waiters and buffet costs (coffee, water and snacks).

### ❄️ Air Conditioning
Quote comparison tool for maintenance services. Supports multiple companies with volume-based discount rules and travel fees.
The comparison panel appears automatically once 2 or more quotes are added, highlighting the cheapest option.

### ⛽ Fueling
Cost-benefit analysis between two partner gas stations (Wayne Oil and Stark Petrol).
The system applies the 70% rule to determine whether ethanol is cost-effective and calculates the total fueling cost (42 liters) for each scenario.

### 📊 Operational Reports
Consolidates real-time session data: confirmed reservations, occupancy rate, registered guests, confirmed events, and accumulated revenue broken down by hospitality and events.

---

## Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | Declarative UI |
| Material Design 3 | Design system |
| ViewModel | State management |
| Navigation Compose | Screen navigation |

---

## Design

The color palette was chosen to convey sophistication with personality:

- **Green `#1D9E75`** — primary color, buttons, confirmations and active elements
- **Gold `#BA7517`** — accent color, decorative details and highlights
- **Off-white `#FAFAF9`** — screen background, clean and lightweight

---

## About

Built as an academic project with a focus on modular software architecture.
Business logic was written first as pure Kotlin, completely decoupled from Android, then integrated into the mobile interface.
This approach ensures each service is cohesive, testable, and framework-independent.
