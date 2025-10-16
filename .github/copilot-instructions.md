# LSMovil - AI Coding Agent Instructions

## Project Overview
LSMovil is an Android mobile application built with Java, using Firebase for backend services (Authentication, Firestore, Realtime Database). The app implements a standard authentication flow with Google Sign-In and email/password options.

## Architecture & Structure

### Technology Stack
- **Language**: Java (JVM Target: 11)
- **Build System**: Gradle with Kotlin DSL (`.kts` files)
- **Android SDK**: Target SDK 35, Min SDK 24
- **Key Dependencies**: 
  - Firebase BOM 32.7.0 (Firestore, Auth, Analytics, Realtime Database)
  - Google Play Services Auth 20.7.0
  - Glide 4.16.0 for image loading
  - Material Design Components 1.9.0

### Package Structure
All code lives in `com.example.lsmovil` with a flat activity-based architecture:
- `MainActivity.java` - Splash screen (3 second delay → Inicio)
- `Inicio.java` - Login screen (email/password + Google Sign-In)
- `SignUpActivity.java` - User registration with email/password
- `ForgotPasswordActivity.java` - Password recovery
- `Principal.java` - Main app screen with navigation drawer
- `ConfiguracionActivity.java`, `SoporteActivity.java`, `AcercaActivity.java` - Settings/support screens

## Critical Development Patterns

### Firebase Integration
**Authentication Flow**:
1. User authenticates via Firebase Auth (email or Google)
2. User data is stored/updated in Firestore `usuarios` collection with fields: `uid`, `nombre`, `correo`, `fotoURL`, `provider`
3. Email users: Firestore stores additional profile data; Google users: data comes from Google profile

**Key Pattern**: Always check network connectivity before Firebase operations using `isNetworkAvailable()` helper method.

### Navigation Pattern
The app uses a **Navigation Drawer** from `Principal.java`:
- Drawer menu defined in `res/menu/drawer_menu.xml`
- Navigation handled via `NavigationView.setNavigationItemSelectedListener()`
- Activities are launched with explicit intents (no Navigation Component library)
- All activities are locked to portrait orientation in `AndroidManifest.xml`

### UI/UX Conventions
- **Loading States**: Toggle visibility of ProgressBar and disable buttons during async operations (see `showLoadingState()` / `hideLoadingState()` in `Inicio.java`)
- **Keyboard Management**: Use static `hideKeyboard(Activity)` method (present in multiple activities) to dismiss keyboard programmatically
- **Password Visibility**: Checkbox-controlled toggle using `PasswordTransformationMethod` / `HideReturnsTransformationMethod`
- **Toolbar Styling**: Custom styled title with SpannableString in `Principal.java` for brand consistency

### Error Handling Patterns
1. **Network checks**: Always validate internet connectivity before Firebase calls
2. **Google Play Services validation**: Check availability before Google Sign-In (see `signInWithGoogle()` in `Inicio.java`)
3. **Detailed error messaging**: Map Firebase/Google error codes to user-friendly Spanish messages
4. **Firestore failure fallback**: If Firestore save fails, still allow login but log error (see `firebaseAuthWithGoogle()`)

## Build & Development Workflow

### Build Commands (PowerShell)
```powershell
# Clean build
.\gradlew clean

# Build debug APK
.\gradlew assembleDebug

# Install on connected device
.\gradlew installDebug

# Run app
.\gradlew installDebug; adb shell am start -n com.example.lsmovil/.MainActivity
```

### Key Configuration Files
- `gradle/libs.versions.toml` - Centralized dependency management (VERSION CATALOG pattern)
- `app/google-services.json` - Firebase configuration (DO NOT commit to public repos)
- `app/build.gradle.kts` - Custom debug signing config using default Android keystore at `~/.android/debug.keystore`

### Version Catalog Usage
Dependencies are referenced via `libs.X.Y.Z` notation:
```kotlin
implementation(libs.firebase.firestore)  // NOT implementation("com.google.firebase:firebase-firestore")
```

## Firebase-Specific Requirements

### Google Sign-In Setup
- Web client ID stored in `res/values/strings.xml` as `default_web_client_id`
- Requires SHA-1 certificate fingerprint configured in Firebase Console
- Requires Google Services plugin: `com.google.gms.google-services` version 4.4.3

### Firestore Collection Schema
**usuarios** collection:
```
{
  uid: String (Firebase Auth UID)
  nombre: String (display name)
  correo: String (email)
  fotoURL: String (profile photo URL, can be empty)
  provider: String ("email" or "google")
}
```

## Common Pitfalls & Solutions

1. **Google Sign-In Errors**: Always check Google Play Services availability before initiating sign-in. Error code 10 (DEVELOPER_ERROR) indicates SHA-1 not configured in Firebase Console.

2. **Fragment vs Activity**: This project uses pure Activities (no Fragments). Do not suggest Fragment-based solutions.

3. **Kotlin vs Java**: Project is Java-only. Do not suggest Kotlin coroutines, flows, or Kotlin-specific syntax.

4. **Architecture Components**: No ViewModel, LiveData, or Repository pattern used. Direct Firebase SDK calls from Activities.

5. **Async Pattern**: Uses Firebase Task-based callbacks, not RxJava or Coroutines.

## Localization
- App name: "LSMovil" (non-translatable)
- UI text: Spanish
- String resources in `res/values/strings.xml` are minimal; most text is hardcoded in layouts

## Testing
- Standard AndroidX test libraries configured
- No custom test files present in workspace
- Test configuration: JUnit 4.13.2, Espresso 3.5.1

---
**When modifying this codebase**: Maintain the existing activity-based architecture, use Firebase Tasks (not callbacks), validate network connectivity, and keep UI feedback patterns consistent across activities.
