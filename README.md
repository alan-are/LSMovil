# LSMovil 📱🤟

Aplicación móvil Android educativa para el aprendizaje de **Lenguaje de Señas Mexicano (LSM)** con inteligencia artificial, desarrollada con Java, Firebase, TensorFlow Lite y OpenCV.

## 📋 Descripción

**LSMovil** es una aplicación Android moderna que combina educación interactiva e inteligencia artificial para facilitar el aprendizaje del Lenguaje de Señas Mexicano (LSM). La aplicación ofrece dos modos principales:

- **🎓 Aprender**: Módulo educativo con el abecedario LSM completo (27 letras) y números del 0 al 10, con imágenes ilustrativas y descripciones detalladas.
- **🤖 Traducir**: Detector de señas LSM en tiempo real usando TensorFlow Lite y OpenCV, con sistema de gamificación para motivar el aprendizaje.

Además, cuenta con un sistema robusto de autenticación mediante Firebase (email/password y Google Sign-In) y una interfaz moderna con Material Design 3.

## ✨ Características

### 🎓 Módulo Aprender (Educativo)
- **Abecedario LSM Completo**
  - 27 letras del alfabeto LSM (A-Z, LL, Ñ, RR)
  - Imágenes ilustrativas en formato WebP optimizado
  - Descripciones detalladas de cómo realizar cada seña
  - Interfaz en grid con Material Cards interactivas
  
- **Números LSM**
  - Números del 0 al 10 en Lenguaje de Señas Mexicano
  - Instrucciones paso a paso para cada número
  - Visualización ampliada con diálogos detallados

### 🤖 Módulo Traducir (IA en Tiempo Real)
- **Detección de Señas con IA**
  - Reconocimiento en tiempo real usando TensorFlow Lite
  - Modelo cuantizado (uint8) para máxima eficiencia
  - Procesamiento de video con OpenCV
  - Detección de 9 clases: Vocales LSM (A, E, I, O, U), Números (1, 2, 3) y Fondo
  
- **Sistema de Gamificación**
  - Contador de señas detectadas
  - Sistema de rachas consecutivas (streak)
  - Puntuación dinámica con multiplicadores
  - Precisión promedio en tiempo real
  - Historial de señas únicas detectadas
  - Celebraciones por logros (rachas de 5, 10, 15+)
  - Resumen de sesión al finalizar

- **Interfaz Landscape Optimizada**
  - Vista de cámara en lado izquierdo (60%)
  - Panel de estadísticas en lado derecho (40%)
  - Material Design 3 con cards y progress indicators
  - Feedback visual instantáneo con código de colores

### 🔐 Autenticación Múltiple
- Inicio de sesión con email y contraseña
- Inicio de sesión con Google Sign-In
- Recuperación de contraseña por email
- Registro de nuevos usuarios con validación completa

### 👤 Gestión de Perfiles
- Perfiles de usuario almacenados en Cloud Firestore
- Soporte para fotos de perfil (Google Sign-In)
- Información personalizada por usuario
- Distinción entre proveedores de autenticación (email/google)

### 🎨 Interfaz de Usuario Moderna
- Diseño Material Design 3 (Material You)
- Edge-to-Edge UI con manejo de WindowInsets
- Navigation Drawer para navegación principal
- Animaciones y transiciones fluidas
- Modo portrait para navegación general
- Modo landscape para detección de señas

### ⚙️ Funcionalidades Adicionales
- Configuración de la aplicación
- Cerrar sesión
- Eliminar cuenta
- Sección de soporte y ayuda
- Información sobre la aplicación y equipo de desarrollo

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 11 (100% Java, sin Kotlin)
- **SDK Android**: Min SDK 24 (Android 7.0) - Target SDK 35
- **Sistema de Build**: Gradle 8.13.0 con Kotlin DSL
- **Arquitectura**: Activity-Based (sin Fragments, sin ViewModel)

### Backend y Servicios
- **Firebase BOM 32.7.0**:
  - Firebase Authentication (Email/Password + Google)
  - Cloud Firestore (perfiles de usuario)
  - Realtime Database (configurado)
  - Firebase Analytics

### Inteligencia Artificial y Visión por Computadora
- **TensorFlow Lite 2.14.0**:
  - TensorFlow Lite Core
  - TensorFlow Lite Support 0.4.4
  - TensorFlow Lite GPU 2.14.0
  - TensorFlow Lite Task Vision 0.4.4
- **OpenCV 3.4.13**: Procesamiento de imágenes y video en tiempo real

### UI y Componentes
- **Material Design Components 1.9.0** (Material Design 3)
- **AndroidX Libraries**: AppCompat, Activity, ConstraintLayout
- **Glide 4.16.0**: Carga y caché de imágenes
- **Google Play Services Auth 20.7.0**

## 📦 Requisitos Previos

### Entorno de Desarrollo
- **Android Studio**: Ladybug | 2024.2.1 o superior
- **JDK**: 11 o superior
- **Android SDK**: SDK 35 instalado
- **Gradle**: 8.13.0 (incluido en el proyecto)

### Servicios Externos
- Cuenta de Firebase con proyecto configurado
- Google Play Services actualizado en el dispositivo/emulador

### Hardware (para módulo Traducir)
- Dispositivo Android con cámara funcional
- Mínimo 2 GB de RAM (recomendado 4 GB)
- Procesador con soporte para cálculos intensivos (recomendado quad-core o superior)

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/alan-are/LSMovil.git
cd LSMovil
```

### 2. Configurar Firebase

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Registra tu aplicación Android con el package name: `com.example.lsmovil`
3. Descarga el archivo `google-services.json`
4. Coloca el archivo en `app/google-services.json`
5. Habilita los siguientes servicios en Firebase:
   - Authentication (Email/Password y Google)
   - Cloud Firestore
   - Realtime Database

### 3. Configurar Google Sign-In

1. En Firebase Console, ve a Authentication → Sign-in method
2. Habilita Google como proveedor
3. Obtén tu SHA-1 fingerprint:

   ```powershell
   # Para debug keystore
   keytool -list -v -keystore C:\Users\<TU_USUARIO>\.android\debug.keystore -alias AndroidDebugKey -storepass android -keypass android
   ```

4. Agrega el SHA-1 en Firebase Console → Project Settings → Your apps

### 4. Compilar el proyecto

```powershell
# Limpiar build anterior
.\gradlew clean

# Compilar debug APK
.\gradlew assembleDebug

# Instalar en dispositivo conectado
.\gradlew installDebug
```

### 5. Ejecutar la aplicación

```powershell
# Instalar y ejecutar
.\gradlew installDebug; adb shell am start -n com.example.lsmovil/.MainActivity
```

## 📁 Estructura del Proyecto

```plaintext
LSMovil/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/lsmovil/
│   │   │   │   ├── MainActivity.java          # Splash screen (3 segundos)
│   │   │   │   ├── Inicio.java                # Pantalla de login
│   │   │   │   ├── SignUpActivity.java        # Registro de usuarios
│   │   │   │   ├── ForgotPasswordActivity.java # Recuperar contraseña
│   │   │   │   ├── Principal.java             # Pantalla principal con Navigation Drawer
│   │   │   │   ├── AprenderActivity.java      # Hub del módulo educativo
│   │   │   │   ├── AbecedarioActivity.java    # 27 letras LSM
│   │   │   │   ├── NumerosActivity.java       # Números 0-10 LSM
│   │   │   │   ├── TraducirActivity.java      # Detector IA en tiempo real
│   │   │   │   ├── LSMDetector.java           # Clase de detección con TensorFlow Lite
│   │   │   │   ├── Letra.java                 # Modelo POJO para letras
│   │   │   │   ├── LetraAdapter.java          # RecyclerView Adapter
│   │   │   │   ├── Numero.java                # Modelo POJO para números
│   │   │   │   ├── NumeroAdapter.java         # RecyclerView Adapter
│   │   │   │   ├── ConfiguracionActivity.java # Configuración y gestión de cuenta
│   │   │   │   ├── SoporteActivity.java       # Soporte y ayuda
│   │   │   │   └── AcercaActivity.java        # Información del equipo
│   │   │   ├── res/
│   │   │   │   ├── layout/                    # Layouts XML (20+ archivos)
│   │   │   │   ├── drawable/                  # Recursos gráficos e imágenes LSM
│   │   │   │   │   ├── ic_letra_*.webp        # 27 imágenes de letras
│   │   │   │   │   └── ic_num_*.webp          # 11 imágenes de números
│   │   │   │   ├── menu/                      # Menús del Navigation Drawer
│   │   │   │   └── values/                    # Strings, colors, themes (MD3)
│   │   │   ├── assets/
│   │   │   │   ├── model.tflite              # Modelo TensorFlow Lite cuantizado
│   │   │   │   ├── labels.txt                # Etiquetas de clases (9 clases)
│   │   │   │   ├── model_old.tflite          # Backup del modelo anterior
│   │   │   │   └── labels_old.txt            # Backup de etiquetas
│   │   │   └── AndroidManifest.xml            # Configuración de app y permisos
│   │   ├── androidTest/                       # Tests instrumentados (configurado)
│   │   └── test/                              # Tests unitarios (configurado)
│   ├── build.gradle.kts                       # Configuración de la app
│   ├── google-services.json                   # Config de Firebase (NO INCLUIDO EN REPO)
│   └── proguard-rules.pro                     # Reglas de ProGuard
├── openCVLibrary3413/                         # Módulo OpenCV 3.4.13
│   ├── src/main/java/org/opencv/             # Clases de OpenCV
│   └── build.gradle                           # Configuración del módulo
├── gradle/
│   ├── libs.versions.toml                     # Catálogo de versiones (source of truth)
│   └── wrapper/                               # Gradle wrapper
├── .github/
│   └── copilot-instructions.md                # Documentación técnica exhaustiva
├── build.gradle.kts                           # Build principal del proyecto
├── settings.gradle.kts                        # Configuración Gradle
├── PLAN_MVP.md                                # Roadmap y estado del MVP
└── README.md                                  # Este archivo
```

## 🔐 Esquema de Base de Datos

### Firestore - Colección `usuarios`

```javascript
{
  uid: String,           // Firebase Auth UID (usado como Document ID)
  nombre: String,        // Nombre completo del usuario
  correo: String,        // Email del usuario
  fotoURL: String,       // URL de foto de perfil (vacío "" para email, URL para Google)
  provider: String       // "email" o "google" (método de autenticación)
}
```

**Operaciones**:
- **Escritura**: En registro (email) o primer login (Google)
- **Lectura**: En login y en header del Navigation Drawer
- **Actualización**: Automática en cada login de Google (sincroniza nombre/foto)

### Modelo TensorFlow Lite

**Archivo**: `assets/model.tflite` (cuantizado uint8)

**Configuración**:
- **Input Shape**: `[1, 224, 224, 3]` (batch, height, width, channels)
- **Input Type**: `uint8` (0-255)
- **Output Shape**: `[1, 9]` (batch, num_classes)
- **Output Type**: `uint8` (0-255, representando probabilidades cuantizadas)

**Clases Detectables** (`assets/labels.txt`):
```
0: Letra A
1: Letra E
2: Letra I
3: Letra O
4: Letra U
5: Numero 1
6: Numero 2
7: Numero 3
8: Fondo (clase de rechazo)
```

**Performance**:
- Inferencia: ~30-50ms por frame en hardware moderno
- FPS: 15-30 (dependiendo del dispositivo)
- Precisión: >85% en condiciones óptimas de iluminación

## 🎯 Flujos de la Aplicación

### Flujo de Autenticación
```
MainActivity (Splash 3s)
    ↓
Inicio (Login)
    ├→ SignUpActivity (Registro con email/password)
    │   └→ Firestore: Guardar usuario con provider="email"
    │   └→ signOut() → Volver a Inicio para login
    ├→ ForgotPasswordActivity (Recuperar contraseña)
    └→ Google Sign-In (OAuth)
        └→ Firestore: Guardar/actualizar usuario con provider="google"
    ↓
Principal (Pantalla principal autenticada)
```

### Flujo del Módulo Aprender
```
Principal
    ↓
AprenderActivity (Hub educativo)
    ├→ AbecedarioActivity
    │   └→ RecyclerView Grid (3 columnas)
    │       └→ Click en letra → Dialog con imagen ampliada + descripción
    ├→ NumerosActivity
    │   └→ RecyclerView Grid (3 columnas)
    │       └→ Click en número → Dialog con imagen ampliada + descripción
    └→ [Palabras Comunes - Próximamente]
```

### Flujo del Módulo Traducir (IA)
```
Principal
    ↓
TraducirActivity (Landscape)
    ├→ Permisos de cámara (runtime)
    ├→ OpenCV JavaCameraView (60% pantalla)
    ├→ Procesamiento en tiempo real:
    │   1. Captura de frame (Mat)
    │   2. Rotación 90° (landscape fix)
    │   3. LSMDetector.recognizeImage()
    │   4. TensorFlow Lite inference
    │   5. Actualización de UI con resultado
    └→ Panel de gamificación (40% pantalla)
        ├─ Estadísticas en tiempo real
        ├─ Sistema de rachas y puntuación
        ├─ Historial de señas detectadas
        └─ Resumen de sesión al salir
```

## 🧪 Testing

### Infraestructura Configurada
```gradle
testImplementation(libs.junit)                  // JUnit 4.13.2
androidTestImplementation(libs.ext.junit)        // AndroidX JUnit 1.3.0
androidTestImplementation(libs.espresso.core)    // Espresso 3.5.1
```

### Comandos
```powershell
# Ejecutar tests unitarios
.\gradlew test

# Ejecutar tests instrumentados (requiere dispositivo/emulador conectado)
.\gradlew connectedAndroidTest

# Ver reporte de tests
.\gradlew test --tests "*" --info
```

**Nota**: Los tests están configurados pero pendientes de implementación. Se recomienda agregar:
- Tests unitarios para `LSMDetector` con imágenes de prueba
- Tests de UI con Espresso para flujos de autenticación
- Tests de integración con Firebase (usando emuladores)

## 📱 Requisitos del Dispositivo

### Requisitos Mínimos
- **Android**: 7.0 (API 24) o superior
- **RAM**: 2 GB mínimo (recomendado 4 GB para módulo Traducir)
- **Almacenamiento**: 100 MB libres
- **Conexión a Internet**: Requerida para autenticación y primera configuración
- **Google Play Services**: Actualizado (para Google Sign-In)

### Requisitos para Módulo Traducir (IA)
- **Cámara**: Cámara trasera o frontal funcional
- **Procesador**: Quad-core o superior recomendado
- **Permisos**: Cámara (solicitado en runtime)

### Funcionalidades Offline
- ✅ **Módulo Aprender**: Funciona 100% offline (imágenes en assets)
- ✅ **Módulo Traducir**: Funciona offline (modelo en assets)
- ❌ **Autenticación**: Requiere conexión a internet
- ❌ **Sincronización de perfil**: Requiere conexión a internet

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Convenciones de Código

### Lenguaje y Arquitectura
- **Lenguaje**: Java 11 puro (sin Kotlin, sin lambdas complejas)
- **Arquitectura**: Activity-Based (sin Fragments, sin ViewModel, sin Repository)
- **Navegación**: Intents explícitos entre Activities
- **Async**: Firebase Tasks API (no RxJava, no Coroutines)
- **Persistencia**: Cloud Firestore + archivos en assets/

### Naming Conventions
```java
// Classes
PascalCase:          MainActivity, LSMDetector, LetraAdapter

// Variables y métodos
camelCase:           editTextEmail, mAuth, btnLogin, updateUI()

// Constantes
UPPER_SNAKE_CASE:    RC_SIGN_IN, TAG, INPUT_SIZE

// Resources XML
snake_case:          activity_main.xml, btn_login, ic_letra_a
```

### Patterns Comunes
```java
// UI State Management
private void showLoadingState() { /* Deshabilitar botones, mostrar progress */ }
private void hideLoadingState() { /* Habilitar botones, ocultar progress */ }

// Network Validation
if (!isNetworkAvailable()) {
    Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
    return;
}

// Keyboard Management (helper estático repetido)
public static void hideKeyboard(Activity activity) { /* ... */ }

// Edge-to-Edge (en todas las Activities)
EdgeToEdge.enable(this);
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), ...);
```

### Comentarios
- **Español**: Documentación de negocio y mensajes de usuario
- **Inglés**: Código técnico y comentarios de implementación
- **Javadoc**: En clases públicas principales (LSMDetector, adapters)

## 🎨 Diseño y UX

### Material Design 3
- **Tema Base**: `Base.Theme.App` con Material 3
- **Color Scheme**: Basado en Material You
- **Componentes**: MaterialToolbar, MaterialCardView, MaterialButton, TextInputLayout
- **Edge-to-Edge**: Habilitado en todas las Activities
- **Elevaciones**: Dinámicas según scroll (AppBar)

### Orientación de Pantalla
- **Portrait**: Todas las Activities EXCEPTO TraducirActivity
- **Landscape**: Solo TraducirActivity (optimizado para cámara + panel lateral)

### Animaciones
- Fade in/out para cards de detección (200ms)
- Ripple effects en todos los clicks (nativo de Material)
- AppBar lift on scroll (elevación dinámica)

## ⚠️ Notas Importantes

### Seguridad y Configuración
- ⚠️ **CRÍTICO**: El archivo `google-services.json` NO debe subirse al repositorio público
- ⚠️ **CRÍTICO**: Verificar que el SHA-1 del debug keystore esté registrado en Firebase Console
- ⚠️ Validar siempre conexión a internet antes de operaciones Firebase
- ⚠️ Firebase Firestore rules están en modo development (permitir lectura/escritura sin autenticación)

### Permisos y Privacy
- Permisos de cámara solicitados en runtime (Android 6.0+)
- No se recopilan datos personales más allá de Firebase Auth
- Imágenes de cámara procesadas localmente (no se envían a servidor)

### Limitaciones Conocidas
- Google Sign-In requiere Google Play Services (no funciona en todos los dispositivos)
- Modelo de IA solo detecta 9 clases (alcance MVP)
- Sin modo offline para autenticación
- Sin internacionalización (app solo en español)

### Performance
- Modelo TensorFlow Lite optimizado (cuantizado uint8)
- Imágenes en formato WebP para menor tamaño
- RecyclerView con ViewHolder pattern para eficiencia
- Bitmaps reciclados después de procesamiento

## 📄 Licencia

Este proyecto es privado y está protegido por derechos de autor.

## � Roadmap y Estado del Proyecto

### ✅ MVP Completado (v1.0.0)
- [x] Sistema de autenticación completo (email + Google)
- [x] Módulo Aprender (Abecedario 27 letras + Números 0-10)
- [x] Módulo Traducir con IA en tiempo real
- [x] Modelo TensorFlow Lite entrenado (9 clases)
- [x] Sistema de gamificación
- [x] UI con Material Design 3
- [x] Integración de OpenCV para procesamiento de video

### 🚧 Próximas Features (Post-MVP)
- [ ] Módulo "Palabras Comunes" en Aprender
- [ ] Expansión de señas detectables (consonantes, más números)
- [ ] Tutorial de primera vez para nuevos usuarios
- [ ] Modo oscuro completo
- [ ] Sistema de logros y badges
- [ ] Tests unitarios e instrumentados
- [ ] Leaderboard con Firebase Firestore
- [ ] Compartir logros en redes sociales
- [ ] Feedback háptico en detecciones exitosas

### 📊 Estado Actual
- **Versión**: 1.0.0
- **Branch Principal**: `diego`
- **Último Commit**: Octubre 2025
- **Estado**: ✅ Producción-Ready para MVP
- **Coverage de Tests**: 0% (infraestructura lista, implementación pendiente)

## 👥 Equipo de Desarrollo

### Desarrolladores
- **Alan Raul Arellano Gonzalez** - [@alan-are](https://github.com/alan-are)
  - Líder de proyecto y arquitectura
- **Alberto Leonel Mejía Hernández**
  - Desarrollo de features y UI/UX
- **Diego Alejandro Guzmán Paniagua**
  - Integración de IA y módulo Traducir

### Información del Proyecto
- **Universidad/Institución**: [Agregar si aplica]
- **Curso/Materia**: [Agregar si aplica]
- **Año**: 2025

## 📧 Contacto y Soporte

Para preguntas, sugerencias o reporte de bugs:
- **Issues**: [GitHub Issues](https://github.com/alan-are/LSMovil/issues)
- **Email**: [Agregar email de contacto si aplica]

## 📄 Licencia

Este proyecto es privado y está protegido por derechos de autor.  
Todos los derechos reservados © 2025 - Equipo LSMovil

## 🙏 Agradecimientos

- **Google Teachable Machine**: Por facilitar el entrenamiento del modelo de IA
- **Firebase**: Por proporcionar backend completo gratuito
- **OpenCV**: Por las herramientas de visión por computadora
- **TensorFlow**: Por el framework de Machine Learning
- **Comunidad LSM**: Por los recursos educativos de Lenguaje de Señas Mexicano

## 📚 Recursos Adicionales

### Documentación del Proyecto
- [`.github/copilot-instructions.md`](.github/copilot-instructions.md) - Guía técnica exhaustiva
- [`PLAN_MVP.md`](PLAN_MVP.md) - Roadmap detallado del MVP

### Enlaces Útiles
- [Firebase Console](https://console.firebase.google.com/)
- [TensorFlow Lite](https://www.tensorflow.org/lite)
- [OpenCV Android](https://opencv.org/android/)
- [Material Design 3](https://m3.material.io/)
- [Google Teachable Machine](https://teachablemachine.withgoogle.com/)

---

**Última actualización**: 22 de octubre de 2025  
**Versión**: 1.0.0  
**Estado**: ✅ MVP Completado y Funcional  

---

<p align="center">
  <strong>Hecho con ❤️ y 🤟 para la comunidad LSM</strong>
</p>
