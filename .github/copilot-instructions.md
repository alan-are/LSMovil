# LSMovil - AI Coding Agent Instructions

## Project Overview
LSMovil is an Android mobile application built with **Java**, using Firebase for backend services (Authentication, Firestore, Realtime Database). The app implements a standard authentication flow with Google Sign-In and email/password options, following Material Design 3 principles with edge-to-edge UI.

## Architecture & Technology Stack

### Core Technologies
- **Language**: Java 11 (no Kotlin - pure Java codebase)
- **Build System**: Gradle 8.13.0 with Kotlin DSL (`.kts` files)
- **Android SDK**: Min SDK 24 (Android 7.0), Target SDK 35
- **UI Framework**: Material Design 3 Components (version 1.9.0)
- **Image Loading**: Glide 4.16.0 with annotation processor
- **Backend**: Firebase BOM 32.7.0
  - Firebase Authentication (email/password + Google)
  - Cloud Firestore (user profiles)
  - Realtime Database (configured but minimal usage)
  - Firebase Analytics

### Architecture Pattern
**Activity-Based Architecture** - No Fragments, No ViewModel, No Repository Pattern
- Direct Firebase SDK calls from Activities
- Flat package structure in `com.example.lsmovil`
- All activities locked to **portrait orientation** in `AndroidManifest.xml`
- Navigation via explicit `Intent` objects (no Navigation Component)

### Activity Flow Map
```
MainActivity (Splash, 3s) 
  → Inicio (Login) 
    ├→ SignUpActivity (Register with email)
    ├→ ForgotPasswordActivity (Password recovery)
    └→ Principal (Main app w/ Navigation Drawer)
         ├→ ConfiguracionActivity
         ├→ SoporteActivity
         └→ AcercaActivity
```

## Critical Development Patterns

### 1. Firebase Authentication Flow
**Two authentication methods with different data storage patterns:**

**Email/Password Registration** (`SignUpActivity.java`):
1. Validate inputs (name 3-20 chars, valid email, password ≥6 chars, matching confirmation)
2. Create user with `mAuth.createUserWithEmailAndPassword()`
3. Store user data in Firestore `usuarios` collection with `provider: "email"`
4. **Sign out immediately** after registration → redirect to `Inicio.java` for login

**Google Sign-In** (`Inicio.java`):
1. **Always check Google Play Services availability** before initiating sign-in
2. Configure `GoogleSignInOptions` with `default_web_client_id` from `strings.xml`
3. Launch Google Sign-In intent with `RC_SIGN_IN = 20`
4. Handle result in `onActivityResult()` → exchange for Firebase credential
5. Store/update user data in Firestore with data from Google profile

**Firestore Schema** (`usuarios` collection):
```java
Map<String, Object> userData = new HashMap<>();
userData.put("uid", user.getUid());           // Firebase Auth UID
userData.put("nombre", displayName);          // Full name
userData.put("correo", email);                // Email address
userData.put("fotoURL", photoUrl);            // Profile photo URL or ""
userData.put("provider", "email|google");     // Auth method
```

**Critical Pattern**: Always validate network connectivity before Firebase operations:
```java
private boolean isNetworkAvailable() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
    return activeNetwork != null && activeNetwork.isConnected();
}
```

### 2. UI/UX State Management Patterns

**Loading State Pattern** (see `Inicio.java`):
```java
private void showLoadingState() {
    progressBar.setVisibility(View.VISIBLE);
    textViewSignUp.setVisibility(View.GONE);
    textViewSignUp3.setVisibility(View.GONE);
    btnLogin.setEnabled(false);
    google_sign_in.setEnabled(false);
}

private void hideLoadingState() {
    progressBar.setVisibility(View.GONE);
    textViewSignUp.setVisibility(View.VISIBLE);
    textViewSignUp3.setVisibility(View.VISIBLE);
    btnLogin.setEnabled(true);
    google_sign_in.setEnabled(true);
}
```
**Rule**: Always disable buttons and show ProgressBar during async operations, re-enable on completion/failure.

**Keyboard Management Pattern** (static helper in multiple activities):
```java
public static void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    View view = activity.getCurrentFocus();
    if (view == null) view = new View(activity);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
}
```
**Rule**: Call `hideKeyboard()` before starting login/registration operations.

### 3. Material Design 3 UI Patterns

**TextInputLayout with Native Password Toggle**:
```xml
<com.google.android.material.textfield.TextInputLayout
    style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
    app:endIconMode="password_toggle"
    app:startIconDrawable="@drawable/ic_lock"
    app:boxCornerRadiusTopStart="12dp"
    app:boxCornerRadiusBottomStart="12dp">
    
    <com.google.android.material.textfield.TextInputEditText
        android:inputType="textPassword" />
</com.google.android.material.textfield.TextInputLayout>
```
**Do NOT use custom CheckBox-based password toggles** - Material 3 provides this natively.

**Edge-to-Edge Configuration** (in every Activity):
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);  // Enable edge-to-edge
    setContentView(R.layout.activity_name);
    
    // Handle window insets
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        // Padding handled in layout XML with fitsSystemWindows or manual spacing
        return insets;
    });
}
```

### 4. Navigation Drawer Pattern (`Principal.java`)

**Styled Toolbar Title**:
```java
private void styleToolbarTitle(Toolbar toolbar) {
    SpannableString spannableString = new SpannableString("LSMovil");
    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    getSupportActionBar().setTitle(spannableString);
}
```

**Navigation Item Selection**:
```java
navigationView.setNavigationItemSelectedListener(item -> {
    int itemId = item.getItemId();
    if (itemId == R.id.nav_soporte) {
        startActivity(new Intent(this, SoporteActivity.class));
    }
    // ... other items
    drawerLayout.closeDrawers();
    return true;
});
```

**Dynamic Header Data** (for email vs Google users):
- Google users: Use `user.getDisplayName()` from Firebase Auth
- Email users: Fetch `nombre` from Firestore `usuarios` collection asynchronously

### 5. Error Handling Patterns

**Google Sign-In Error Mapping** (`Inicio.java`):
```java
private String getGoogleSignInErrorMessage(int statusCode) {
    switch (statusCode) {
        case 10: return "Error de configuración. Verifica el SHA-1 en Firebase";
        case 12500: return "Inicio de sesión cancelado";
        case 7: return "Error de red. Verifica tu conexión";
        // ... more cases
    }
}
```

**Firestore Failure Fallback**:
- If Firestore write fails during Google Sign-In: **still allow login** and log error
- If Firestore write fails during registration: **delete the Auth user** and show error

**Firebase Auth Error Messages**:
- Always check `task.getException().getMessage()` for specific errors
- Map common errors to Spanish user-friendly messages
- Examples: "email address is already in use" → "El correo electrónico ya está en uso"

## Build & Development Workflow

### Essential Commands (PowerShell)
```powershell
# Clean + build debug APK
.\gradlew clean assembleDebug

# Install on connected device/emulator
.\gradlew installDebug

# Install and launch immediately
.\gradlew installDebug; adb shell am start -n com.example.lsmovil/.MainActivity

# Run tests
.\gradlew test                    # Unit tests
.\gradlew connectedAndroidTest    # Instrumented tests

# Get SHA-1 for Firebase (debug keystore)
keytool -list -v -keystore C:\Users\<USERNAME>\.android\debug.keystore -alias AndroidDebugKey -storepass android -keypass android
```

### Gradle Version Catalog Pattern
**Dependencies MUST use the version catalog** defined in `gradle/libs.versions.toml`:

✅ **Correct**:
```kotlin
implementation(libs.firebase.firestore)
implementation(libs.play.services.auth)
```

❌ **Incorrect**:
```kotlin
implementation("com.google.firebase:firebase-firestore:24.0.0")
```

**Important**: Firebase modules don't specify versions - they're controlled by the BOM:
```kotlin
implementation(platform(libs.firebase.bom))  // Single BOM declaration
implementation(libs.firebase.firestore)      // No version needed
```

### Key Configuration Files
- `app/build.gradle.kts` - Custom debug signing config references `~/.android/debug.keystore`
- `app/google-services.json` - Firebase config (**NEVER commit to public repos**)
- `gradle/libs.versions.toml` - Version catalog (all dependencies defined here)
- `settings.gradle.kts` - Root project name is `"app"` (unusual but intentional)

## Firebase-Specific Requirements

### Google Sign-In Configuration Checklist
1. **Web Client ID**: Must exist in `res/values/strings.xml` as `default_web_client_id`
2. **SHA-1 Fingerprint**: Must be registered in Firebase Console → Project Settings → Your apps
3. **Google Play Services**: Check availability with `GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable()`
4. **Firebase Auth Provider**: Google must be enabled in Firebase Console → Authentication → Sign-in method

### Session Management
- Check existing session in `onStart()` of `Inicio.java`:
  ```java
  if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      startActivity(new Intent(Inicio.this, Principal.class));
      finish();
  }
  ```
- Auto-redirect authenticated users from login screen to main app

## Common Pitfalls & Solutions

| Issue | Solution |
|-------|----------|
| Google Sign-In Error 10 (DEVELOPER_ERROR) | SHA-1 not configured in Firebase Console |
| "Network error" during auth | Always call `isNetworkAvailable()` first |
| Firestore write hangs | Check network + Firebase rules (currently open for development) |
| Version catalog not found | Use `libs.X.Y` syntax, not direct dependency strings |
| Keyboard covers input fields | Call `hideKeyboard()` before showing dialogs/progress |
| Password toggle not working | Use Material 3's `endIconMode="password_toggle"` |
| Portrait lock not enforced | Add `screenOrientation="portrait"` to ALL activities in manifest |

## Anti-Patterns (DO NOT USE)
- ❌ **Fragments** - Project uses only Activities
- ❌ **Kotlin** - Java-only codebase (even though Kotlin plugin is applied)
- ❌ **ViewModel/LiveData** - Direct Firebase calls from Activities
- ❌ **Repository Pattern** - Not implemented
- ❌ **RxJava/Coroutines** - Use Firebase Task API only
- ❌ **Navigation Component** - Use explicit Intents
- ❌ **Data Binding/View Binding** - Use `findViewById()`
- ❌ **Jetpack Compose** - XML layouts only

## Code Style & Conventions
- **Language**: Java 11 (no Kotlin features)
- **Async**: Firebase `Task<>` API with `.addOnCompleteListener()` / `.addOnSuccessListener()`
- **Null Safety**: Use `!= null` checks, avoid `Objects.requireNonNull()` where possible
- **UI Thread**: Firebase callbacks already run on main thread
- **Resource IDs**: Activity main container always named `@+id/main`
- **Comments**: Spanish for business logic, English for technical code
- **Toasts**: All user-facing messages in Spanish

## Testing Notes
- Test infrastructure configured (JUnit 4.13.2, Espresso 3.5.1)
- No existing test files in workspace
- Firebase Auth requires emulator or real device for integration tests
- Google Sign-In cannot be fully tested in unit tests (requires real Play Services)

---

**When modifying this codebase**: Maintain activity-based architecture, validate network before Firebase calls, use version catalog for dependencies, keep UI state management consistent (loading states + button disabling), and preserve portrait-only orientation. Always test Google Sign-In changes with both debug keystore SHA-1 and real Firebase project configuration.
