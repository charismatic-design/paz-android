# PAZ Android &bull; Native Ethiopian Macro & FX Terminal

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Compose-Material_3-4285F4.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-3DDC84.svg?style=flat&logo=android)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Native Android client for the **PAZ Financial Ecosystem**, delivering real-time Ethiopian foreign exchange intelligence, commercial bank buying/selling spreads, physical gold bullion stack valuations, diaspora remittance optimization, and macroeconomic intelligence tailored for Android devices.

---

## 📱 User Interface & Visual Design

Engineered in modern **Jetpack Compose (Material 3)** adhering to the progressive disclosure architecture and terminal dark aesthetic established across `paz-web` and `paz-ios`:

### Obsidian & Gold Fintech Design System
- **Obsidian Background**: `#0B0F19` deep terminal canvas
- **Card Surface**: `#161F30` elevated container background
- **Surface Elevation**: `#1C273C` interactive card surface
- **Border Stroke**: `#223049` subtle structural hairline borders
- **Gold Accent**: `#D4AF37` / `#FDE68A` bullion & primary accents
- **Cyan Accent**: `#38BDF8` interbank FX highlight
- **Green & Red**: `#10B981` (bullish / spread gains) & `#F43F5E` (inflationary erosion)

### 4-Tab Progressive Disclosure Architecture

```
┌───────────────────────────────────────────────────────────┐
│  PAZ [ADDIS LIVE]                   [⚡ Sync] [🔔 Alerts] │
├───────────────────────────────────────────────────────────┤
│                                                           │
│  [ Tab 1: Pulse ]                                         │
│  • USD/ETB (163.29) & Gold 24K (21,315 ETB/g) Hero Banner │
│  • AI Macro Market Sentiment Card                         │
│  • Ethiopian Macro News & Dispatches Wire                 │
│                                                           │
│  [ Tab 2: FX ]                                            │
│  • Segmented: Commercial Bank Rates vs. Diaspora Remit    │
│  • Bank Rates Table: CBE, Abyssinia, Awash, Dashen, NBE   │
│  • Remittance Optimizer: Net ETB Payouts & Fees           │
│                                                           │
│  [ Tab 3: Gold ]                                          │
│  • Segmented: Gold Vault & Karats vs. Wealth Simulator    │
│  • 24K, 22K, 21K, 18K Karats & Personal Vault Calculator  │
│  • Inflation vs. NBE T-Bill (15.2%) & Gold Hedge Slider   │
│                                                           │
│  [ Tab 4: ESX ]                                           │
│  • Institutional ESX Equities — Launching Soon            │
│  • Capital Market Readiness Milestones Checklist          │
│  • 1-Click Priority Investor Waitlist                     │
│                                                           │
├───────────────────────────────────────────────────────────┤
│    [ Pulse ]         [ FX ]         [ Gold ]      [ ESX ] │
└───────────────────────────────────────────────────────────┘
```

1. **`Pulse` Tab**:
   - Hero rate cards highlighting real-time interbank floating FX (`163.29 ETB/USD`) and physical 24K gold bullion (`21,315 ETB/g`).
   - AI Market Sentiment Card with confidence badges, macro context, and headline count.
   - Live Ethiopian macro financial news stream with direct article dispatch links.
2. **`FX` Tab**:
   - Custom segmented control switching between **Commercial Bank Rates Table** and **Diaspora Remittance & Fee Optimizer**.
   - Bank comparison table across `CBE`, `Bank of Abyssinia`, `Awash Bank`, `Dashen Bank`, and `National Bank of Ethiopia (NBE)` across `USD`, `EUR`, `GBP`, and `AED`.
   - Remittance simulator computing net ETB received across digital apps (Remitly, Western Union, Dahabshiil), SWIFT wire, and Bole airport cash.
3. **`Gold` Tab**:
   - Real-time spot gold pricing ($2,748/oz) and purity breakdown (`24K`, `22K`, `21K`, `18K`).
   - Interactive **My Gold Vault Stack Calculator** calculating instantaneous portfolio net worth in ETB and USD.
   - **Inflation vs. NBE T-Bill (15.2%) & Gold Hedge Simulator** with interactive Compose `Slider` (1 to 5 years) comparing cash erosion (~28% inflation) against NBE risk-free yields and historical gold returns.
4. **`ESX` Tab**:
   - Institutional overview of the Ethiopian Securities Exchange launch.
   - Regulatory and technical readiness milestone checklist (ECMA framework, automated trading engine, clearing bank integration).
   - 1-click priority waitlist subscription.
5. **Top Toolbar Price Alerts `ModalBottomSheet`**:
   - Accessible via the top bar `🔔 Alerts` icon.
   - Allows traders and diaspora remitters to set breakout alerts for currency pairs and gold karats.
   - Interactive watchlist with single-tap deletion.

---

## 🏗 Architecture & Code Structure

```
paz-android/
├── app/
│   ├── build.gradle.kts           # App-level dependencies & Compose configuration
│   ├── proguard-rules.pro         # Proguard/R8 optimization rules
│   └── src/main/
│       ├── AndroidManifest.xml    # Permissions (INTERNET) & cleartext traffic config
│       ├── res/values/            # Colors, strings, themes
│       └── java/com/paz/android/
│           ├── MainActivity.kt    # Root Compose Activity & NavigationBar Scaffold
│           ├── data/
│           │   ├── Models.kt      # Kotlinx Serialization data classes & UiState
│           │   └── PazApiService.kt # Coroutine HTTP client with LAN host probing & fallbacks
│           └── ui/
│               ├── theme/Theme.kt # Obsidian dark palette & Material 3 scheme
│               ├── components/    # Reusable Compose widgets (Hero cards, badges, toggles)
│               └── screens/
│                   ├── PulseTab.kt # Pulse hero banner, AI sentiment & news
│                   ├── FxTab.kt    # Bank rates table & remittance fee engine
│                   ├── GoldTab.kt  # Karat cards, vault stack & hedge simulator
│                   ├── EsxTab.kt   # Institutional ESX checklist & waitlist
│                   └── PriceAlertsModal.kt # ModalBottomSheet for price alerts
├── build.gradle.kts               # Root Gradle build script
├── settings.gradle.kts            # Project settings & repositories
├── gradle.properties              # JVM & AndroidX optimization flags
└── README.md
```

### Kotlin Concurrency & Networking Architecture
- **OkHttp + Kotlin Coroutines (`Dispatchers.IO`)**: Concurrent asynchronous data loading with `async`/`await` across news, banks, remittance, gold, and alert endpoints.
- **Dynamic Host Resolution & Local LAN Fallback**:
  - Automatically checks `http://10.0.2.2:8000` (standard Android Emulator bridge to Mac host `127.0.0.1:8000`).
  - Probes local Wi-Fi LAN IP addresses (`http://192.168.1.2:8000` / `http://192.168.1.10:8000`) for seamless execution on physical Android phones connected over Wi-Fi.
- **Cleartext Network Security**: Configured with `android:usesCleartextTraffic="true"` and `ACCESS_NETWORK_STATE` to communicate with the local FastAPI development server without SSL certificate barriers.
- **Resilient Offline Benchmarks**: Pre-calibrated Ethiopian economic benchmarks guarantee immediate, flicker-free UI rendering even in zero-connectivity environments.

---

## 🛠 Build & Run Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer / IntelliJ IDEA
- Android SDK (API 35, Min SDK 26)
- Java 17+ or JDK 21

### Running with Android Studio
1. Open Android Studio and select **Open**.
2. Select the `/Users/yourstruly/Documents/PAZ/paz-android` directory.
3. Allow Gradle to synchronize dependencies.
4. Select your target device (Android Emulator or physical Android device connected via USB/Wi-Fi).
5. Click **Run** (`Shift + F10`) to build and install the application.

### Connecting to the Local Backend
Ensure the PAZ FastAPI backend is running on your Mac host:
```bash
cd /Users/yourstruly/Documents/PAZ/paz-backend
./venv/bin/uvicorn main:app --reload --host 0.0.0.0 --port 8000
```
- In the **Android Emulator**, requests automatically route to `http://10.0.2.2:8000`.
- On a **Physical Android Device**, ensure the phone is connected to the same Wi-Fi network; the app will automatically communicate with `http://192.168.1.2:8000`.

---

## 📄 License
This project is licensed under the MIT License.
