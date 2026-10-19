# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

PlaylistRoast is an early-stage single-module Android app (`:app`, package `com.roman.mydemoapp`) built with Kotlin and Jetpack Compose + Material 3. Single-activity architecture: `MainActivity` calls `enableEdgeToEdge()` and sets `AppTheme { MainScreen() }`. UI strings are in Ukrainian (e.g. the top bar title "Просмажка").

SDK: minSdk 26, targetSdk 35, compileSdk 36, Java/JVM target 11. Dependencies are managed via the version catalog in `gradle/libs.versions.toml` (Compose BOM 2024.09.00, Kotlin 2.0.21, AGP 8.9.1).

## Commands

Use the Gradle wrapper from the repo root:

- Build debug APK: `./gradlew assembleDebug`
- Install on device/emulator: `./gradlew installDebug`
- Unit tests (JVM): `./gradlew testDebugUnitTest`
- Single unit test: `./gradlew testDebugUnitTest --tests "com.roman.mydemoapp.ExampleUnitTest"`
- Instrumented tests (needs device/emulator): `./gradlew connectedDebugAndroidTest`
- Lint: `./gradlew lint`

## Structure

Code lives under `app/src/main/java/com/roman/mydemoapp/`:

- `MainActivity.kt` — entry point; forwards share/deep-link intents to the activity-scoped `LinkViewModel`.
- `mainscreen/` — the main screen (`MainScreen` is a `Scaffold` with a `TopAppBar`) and its components such as `ScreenshotCard`. Screens are organized as one package per screen.
- `navigation/` — `AppNavigation` (NavHost, `Routes`), `LinkViewModel` (pending incoming link), `extractPlaylistLink`.
- `settings/`, `playlist/` — one package per screen, each with its screen, ViewModel and UI state.
- `ui/theme/` — `AppTheme` (`Theme.kt`), `Color.kt`, `Type.kt`. Wrap all UI/previews in `AppTheme`.

Architecture is lean MVVM (no DI): each screen with state has a `XViewModel` exposing `StateFlow<XUiState>` (plus a `Channel`-backed `events` flow for one-shots), a stateful `XScreen` that creates it via `viewModel { }` and collects with `collectAsStateWithLifecycle()`, and a stateless `XContent` that previews and tests use. `SettingsViewModel` persists the roast level through the `SettingsStore` interface (DataStore impl). ViewModel unit tests live in `app/src/test` and use `MainDispatcherRule`.

Note: `MainScreen.kt` uses `androidx.compose.material.icons.filled.*`; `material-icons-extended` is not in the catalog, so check the dependency before using icons beyond the core set.
