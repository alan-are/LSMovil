# LSMovil 📱🤟

Aplicación móvil Android educativa para el aprendizaje de **Lenguaje de Señas Mexicano (LSM)** con inteligencia artificial, desarrollada con Java, Firebase, TensorFlow Lite y OpenCV.

## 📋 Descripción

**LSMovil** es una aplicación Android moderna que combina educación interactiva e inteligencia artificial para facilitar el aprendizaje del Lenguaje de Señas Mexicano (LSM). Desarrollada completamente en **Java** con arquitectura basada en Activities, la aplicación ofrece dos modos principales:

- **🎓 Aprender**: Módulo educativo con el abecedario LSM completo (27 letras incluyendo LL, Ñ, RR) y números del 0 al 10, con imágenes en formato WebP optimizado y descripciones detalladas para cada seña.
- **🤖 Traducir**: Detector de señas LSM en tiempo real usando TensorFlow Lite (modelo cuantizado) y OpenCV, capaz de reconocer 5 vocales (A, E, I, O, U) y 3 números (1, 2, 3) con precisión superior al 90%.

Además, cuenta con un sistema robusto de autenticación mediante Firebase (email/password y Google Sign-In), gestión de perfiles de usuario en Cloud Firestore, y una interfaz moderna siguiendo las guías de Material Design 3 con edge-to-edge UI.

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
  - Reconocimiento en tiempo real usando TensorFlow Lite 2.14.0
  - Modelo cuantizado (uint8) con entrada de 224x224 píxeles
  - Procesamiento de video con OpenCV 3.4.13
  - Detección de 9 clases: Vocales LSM (A, E, I, O, U), Números (1, 2, 3) y clase de rechazo (Fondo)
  - Umbral de confianza >90% para validación de detecciones
  - Modelo entrenado con Google Teachable Machine
  
- **Interfaz Landscape Optimizada**
  - Vista de cámara cuadrada (SurfaceView con OpenCV)
  - Cambio dinámico entre cámara frontal y trasera
  - Efecto espejo automático para cámara frontal
  - Material Design 3 Cards para mostrar detecciones
  - Animaciones suaves con fade-in/fade-out (300ms)
  - Visualización temporal de señas detectadas (2 segundos)
  - Feedback visual con imágenes ilustrativas correspondientes

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

- **Lenguaje**: Java 11 (100% Java, sin Kotlin en el código fuente)
- **SDK Android**: Min SDK 24 (Android 7.0) - Target SDK 35
- **Sistema de Build**: Gradle 8.13.0 con Kotlin DSL (.kts)
- **Arquitectura**: Activity-Based (sin Fragments, sin ViewModel, sin Repository Pattern)

### Backend y Servicios
- **Firebase BOM 32.7.0**:
  - Firebase Authentication (Email/Password + Google Sign-In con OAuth 2.0)
  - Cloud Firestore (almacenamiento de perfiles de usuario)
  - Realtime Database (configurado para uso futuro)
  - Firebase Analytics (seguimiento de eventos)

### Inteligencia Artificial y Visión por Computadora
- **TensorFlow Lite 2.14.0**:
  - TensorFlow Lite Core (runtime principal)
  - TensorFlow Lite Support 0.4.4 (utilidades y procesamiento)
  - TensorFlow Lite GPU 2.14.0 (aceleración por hardware)
  - TensorFlow Lite Task Vision 0.4.4 (tareas de visión)
- **OpenCV 3.4.13**: Procesamiento de imágenes, captura de video, manipulación de Mat
- **Modelo Cuantizado**: uint8 (224x224x3), 9 clases, ~1-2 MB

### UI y Componentes
- **Material Design Components 1.9.0** (Material Design 3 / Material You)
- **AndroidX Libraries**: 
  - AppCompat 1.6.1
  - Activity 1.8.0
  - ConstraintLayout 2.1.4
- **Glide 4.16.0**: Carga optimizada de imágenes con caché en memoria y disco
- **Google Play Services Auth 20.7.0**: Autenticación con Google
- **Google Play Services Base 18.9.0**: APIs base de Google Play Services

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
│   │   │   │   ├── MainActivity.java              # Splash screen (3 segundos)
│   │   │   │   ├── Inicio.java                    # Login (email/password y Google)
│   │   │   │   ├── SignUpActivity.java            # Registro de usuarios con validación
│   │   │   │   ├── ForgotPasswordActivity.java     # Recuperación de contraseña
│   │   │   │   ├── Principal.java                 # Pantalla principal con Navigation Drawer
│   │   │   │   ├── AprenderActivity.java          # Hub del módulo educativo
│   │   │   │   ├── AbecedarioActivity.java        # Grid de 27 letras LSM (RecyclerView)
│   │   │   │   ├── NumerosActivity.java           # Grid de números 0-10 LSM (RecyclerView)
│   │   │   │   ├── TraducirActivity.java          # Detector IA en tiempo real (landscape)
│   │   │   │   ├── LSMDetector.java               # Clase de detección con TensorFlow Lite
│   │   │   │   ├── Letra.java                     # Modelo POJO para letras
│   │   │   │   ├── LetraAdapter.java              # RecyclerView Adapter con ViewHolder
│   │   │   │   ├── Numero.java                    # Modelo POJO para números
│   │   │   │   ├── NumeroAdapter.java             # RecyclerView Adapter con ViewHolder
│   │   │   │   ├── ConfiguracionActivity.java     # Configuración y gestión de cuenta
│   │   │   │   ├── SoporteActivity.java           # Soporte y contacto
│   │   │   │   └── AcercaActivity.java            # Información del equipo
│   │   │   ├── res/
│   │   │   │   ├── layout/                        # 20+ archivos XML con Material Design 3
│   │   │   │   │   ├── activity_traducir.xml      # Layout landscape con cámara + detección
│   │   │   │   │   ├── activity_principal.xml     # DrawerLayout con NavigationView
│   │   │   │   │   ├── item_letra.xml             # Card layout para letras
│   │   │   │   │   └── item_numero.xml            # Card layout para números
│   │   │   │   ├── drawable/                      # Recursos gráficos vectoriales e imágenes
│   │   │   │   │   ├── ic_letra_a.webp            # Imágenes LSM (27 letras)
│   │   │   │   │   ├── ic_letra_ll.webp           # Letra LL especial
│   │   │   │   │   ├── ic_letra_nn.webp           # Letra Ñ
│   │   │   │   │   ├── ic_letra_rr.webp           # Letra RR especial
│   │   │   │   │   ├── ic_num_0.webp              # Imágenes de números (0-10)
│   │   │   │   │   └── ...
│   │   │   │   ├── menu/                          # Menús XML
│   │   │   │   │   └── menu_navigation_drawer.xml # Items del Navigation Drawer
│   │   │   │   ├── values/                        # Recursos de valores
│   │   │   │   │   ├── strings.xml                # Textos en español + default_web_client_id
│   │   │   │   │   ├── colors.xml                 # Paleta de colores Material You
│   │   │   │   │   └── themes.xml                 # Tema Base.Theme.App (MD3)
│   │   │   │   └── mipmap/                        # Iconos de app en múltiples densidades
│   │   │   │       └── icono.png                  # Logo de LSMovil
│   │   │   ├── assets/
│   │   │   │   ├── model.tflite                   # Modelo TensorFlow Lite cuantizado (uint8)
│   │   │   │   └── labels.txt                     # Etiquetas de 9 clases
│   │   │   └── AndroidManifest.xml                # Configuración de app, permisos y Activities
│   │   ├── androidTest/java/com/example/lsmovil/  # Tests instrumentados (infraestructura lista)
│   │   └── test/java/com/example/lsmovil/         # Tests unitarios (infraestructura lista)
│   ├── build.gradle.kts                           # Configuración de módulo app
│   ├── google-services.json                       # Config de Firebase (NO EN REPO PÚBLICO)
│   └── proguard-rules.pro                         # Reglas de ofuscación
├── openCVLibrary3413/                             # Módulo OpenCV 3.4.13
│   ├── src/main/java/org/opencv/                  # Clases Java de OpenCV (300+ archivos)
│   ├── src/main/jniLibs/                          # Librerías nativas (.so) por arquitectura
│   │   ├── arm64-v8a/
│   │   ├── armeabi-v7a/
│   │   ├── x86/
│   │   └── x86_64/
│   └── build.gradle                               # Configuración del módulo OpenCV
├── gradle/
│   ├── libs.versions.toml                         # Catálogo de versiones centralizado
│   └── wrapper/                                   # Gradle wrapper 8.13.0
├── .github/
│   └── copilot-instructions.md                    # Documentación técnica exhaustiva
├── build.gradle.kts                               # Build raíz del proyecto
├── settings.gradle.kts                            # Inclusión de módulos (app + OpenCV)
├── PLAN_MVP.md                                    # Roadmap y plan de desarrollo del MVP
└── README.md                                      # Este archivo
```

## 🔐 Esquema de Base de Datos

### Firestore - Colección `usuarios`

Cada documento representa un usuario registrado, usando el `uid` de Firebase Auth como ID del documento.

**Esquema del documento**:
```javascript
{
  uid: String,           // Firebase Auth UID (usado como Document ID)
  nombre: String,        // Nombre completo del usuario (ej: "Juan Pérez")
  correo: String,        // Email del usuario (ej: "juan@example.com")
  fotoURL: String,       // URL de foto de perfil (vacío "" para email, URL completa para Google)
  provider: String       // Método de autenticación: "email" | "google"
}
```

**Operaciones en el ciclo de vida**:
- **Escritura**: 
  - Durante registro (SignUpActivity): usuarios email/password
  - En primer login (Inicio): usuarios de Google Sign-In
- **Lectura**: 
  - Al iniciar sesión (para validación)
  - En header del Navigation Drawer (mostrar nombre y foto)
- **Actualización**: 
  - Cada login de Google sincroniza automáticamente nombre y foto del perfil de Google

**Reglas de seguridad** (actualmente en modo desarrollo):
```javascript
// NOTA: Cambiar a producción antes de despliegue
allow read, write: if true; // Temporal para desarrollo
```

### Modelo TensorFlow Lite - Clasificación de Señas LSM

**Archivo**: `app/src/main/assets/model.tflite` (cuantizado uint8)

**Especificaciones técnicas**:
- **Tipo de modelo**: Clasificación de imágenes (Image Classification)
- **Framework de entrenamiento**: Google Teachable Machine
- **Cuantización**: INT8 (uint8) para optimización en dispositivos móviles
- **Tamaño del modelo**: ~1-2 MB

**Configuración de entrada**:
- **Input Shape**: `[1, 224, 224, 3]` (batch_size, height, width, channels)
- **Input Type**: `UINT8` (valores 0-255, sin normalización)
- **Input Tensor**: ByteBuffer de 224 × 224 × 3 = 150,528 bytes
- **Formato de color**: RGB (Red-Green-Blue)

**Configuración de salida**:
- **Output Shape**: `[1, 9]` (batch_size, num_classes)
- **Output Type**: `UINT8` (probabilidades cuantizadas 0-255)
- **Conversión**: byte_value / 255.0 = probabilidad (0.0 - 1.0)

**Clases Detectables** (`app/src/main/assets/labels.txt`):
```
0: Letra A         # Vocal LSM
1: Letra E         # Vocal LSM
2: Letra I         # Vocal LSM
3: Letra O         # Vocal LSM
4: Letra U         # Vocal LSM
5: Numero 1        # Número LSM
6: Numero 2        # Número LSM
7: Numero 3        # Número LSM
8: Fondo           # Clase de rechazo (sin seña detectada)
```

**Mapeo de recursos drawable**:
| Clase | Archivo de imagen | Descripción |
|-------|------------------|-------------|
| Letra A | `ic_letra_a.webp` | Puño cerrado con pulgar al lado |
| Letra E | `ic_letra_e.webp` | Dedos doblados hacia palma |
| Letra I | `ic_letra_i.webp` | Meñique extendido |
| Letra O | `ic_letra_o.webp` | Mano formando círculo |
| Letra U | `ic_letra_u.webp` | Índice y medio juntos hacia arriba |
| Numero 1 | `ic_num_1.webp` | Índice extendido |
| Numero 2 | `ic_num_2.webp` | Índice y medio extendidos |
| Numero 3 | `ic_num_3.webp` | Pulgar, índice y medio extendidos |
| Fondo | N/A | Sin imagen (clase de rechazo) |

**Performance en dispositivos reales**:
- **Latencia de inferencia**: ~30-50ms por frame en hardware moderno
- **FPS**: 15-30 (depende del procesador del dispositivo)
- **Precisión**: >90% en condiciones óptimas de iluminación
- **Threads**: 4 hilos de CPU configurados en Interpreter.Options
- **Umbral de confianza**: 90% (0.9) para validar detecciones

**Configuración del intérprete TensorFlow Lite**:
```java
Interpreter.Options options = new Interpreter.Options();
options.setNumThreads(4); // Usar 4 hilos de CPU
interpreter = new Interpreter(loadModelFile(assetManager, modelPath), options);
```

**Proceso de inferencia**:
1. Captura de frame de OpenCV (Mat RGBA)
2. Conversión Mat → Bitmap
3. Escalado a 224x224 píxeles
4. Conversión Bitmap → ByteBuffer (RGB uint8)
5. Ejecución: `interpreter.run(byteBuffer, output)`
6. Dequantización: `(byte & 0xFF) / 255.0f`
7. Selección de clase con máxima probabilidad
8. Validación con umbral de confianza >90%

## 🎯 Flujos de la Aplicación

### Flujo de Autenticación Completo
```
MainActivity (Splash screen 3s)
    ↓
    Verificar si hay sesión activa (FirebaseAuth.getCurrentUser())
    ├─ Usuario autenticado → Principal
    └─ Usuario no autenticado ↓
    
Inicio (Login)
    ├→ Opción 1: Email/Password
    │   ├─ Validar conexión a internet (isNetworkAvailable())
    │   ├─ Validar formato de email y contraseña
    │   ├─ FirebaseAuth.signInWithEmailAndPassword()
    │   ├─ Leer datos de Firestore (colección "usuarios")
    │   └→ Principal (si éxito)
    │
    ├→ Opción 2: Google Sign-In
    │   ├─ Verificar Google Play Services disponibles
    │   ├─ Lanzar GoogleSignInIntent (RC_SIGN_IN = 20)
    │   ├─ Obtener GoogleSignInAccount en onActivityResult()
    │   ├─ Intercambiar por Firebase credential
    │   ├─ FirebaseAuth.signInWithCredential()
    │   ├─ Guardar/Actualizar usuario en Firestore (provider="google")
    │   └→ Principal (si éxito)
    │
    ├→ Olvidé mi contraseña
    │   └→ ForgotPasswordActivity
    │       ├─ Validar formato de email
    │       ├─ FirebaseAuth.sendPasswordResetEmail()
    │       └─ Mostrar confirmación
    │
    └→ Crear cuenta
        └→ SignUpActivity
            ├─ Validar nombre (3-20 caracteres)
            ├─ Validar email (formato válido)
            ├─ Validar contraseña (≥6 caracteres)
            ├─ Validar confirmación de contraseña (deben coincidir)
            ├─ FirebaseAuth.createUserWithEmailAndPassword()
            ├─ Guardar usuario en Firestore (provider="email", fotoURL="")
            ├─ FirebaseAuth.signOut() ← IMPORTANTE
            └→ Regresar a Inicio para login manual

Principal (Pantalla principal autenticada)
    ├─ Cargar datos de usuario en Navigation Drawer Header
    │   ├─ Email (de FirebaseAuth.currentUser.email)
    │   ├─ Nombre (de Firestore o displayName si es Google)
    │   └─ Foto de perfil (solo para usuarios de Google)
    ├─ Opciones del Navigation Drawer:
    │   ├→ Aprender
    │   ├→ Traducir (requiere permiso de cámara)
    │   ├→ Configuración
    │   ├→ Soporte
    │   ├→ Acerca de
    │   └→ Cerrar sesión (signOut → Inicio)
    └─ Botones principales en pantalla:
        ├→ Aprender (AprenderActivity)
        └→ Traducir (TraducirActivity)
```

### Flujo del Módulo Aprender
```
Principal
    ↓
AprenderActivity (Hub educativo)
    ├─ Material Cards con opciones:
    │   ├→ Abecedario LSM
    │   ├→ Números LSM
    │   └─ [Palabras Comunes - Próximamente]
    ↓
├─ AbecedarioActivity
│   ├─ RecyclerView con GridLayoutManager (3 columnas)
│   ├─ LetraAdapter con 27 items (A-Z, LL, Ñ, RR)
│   ├─ Datos en array estático de objetos Letra
│   │   └─ Letra(nombre, recursoImagen, descripcion)
│   └─ Click en item:
│       ├─ Crear AlertDialog personalizado
│       ├─ Mostrar imagen ampliada (ImageView)
│       ├─ Mostrar nombre de la letra (TextView)
│       ├─ Mostrar descripción detallada (TextView scrollable)
│       └─ Botón "Cerrar" para dismiss
│
└─ NumerosActivity
    ├─ RecyclerView con GridLayoutManager (3 columnas)
    ├─ NumeroAdapter con 11 items (0-10)
    ├─ Datos en array estático de objetos Numero
    │   └─ Numero(valor, recursoImagen, descripcion)
    └─ Click en item:
        ├─ Crear AlertDialog personalizado
        ├─ Mostrar imagen ampliada (ImageView)
        ├─ Mostrar número (TextView)
        ├─ Mostrar descripción de cómo realizar la seña (TextView)
        └─ Botón "Cerrar" para dismiss
```

### Flujo del Módulo Traducir (IA en Tiempo Real)
```
Principal
    ↓
TraducirActivity (Landscape)
    ├─ Verificar permiso de cámara
    │   ├─ No concedido → Solicitar en runtime (Android 6.0+)
    │   └─ Denegado → Toast + finish()
    ↓
    ├─ Inicializar OpenCV
    │   ├─ OpenCVLoader.initDebug() o initAsync()
    │   └─ BaseLoaderCallback.onManagerConnected()
    ↓
    ├─ Inicializar LSMDetector
    │   ├─ Cargar model.tflite desde assets
    │   ├─ Cargar labels.txt desde assets
    │   ├─ Configurar Interpreter.Options (4 threads)
    │   └─ Crear Interpreter de TensorFlow Lite
    ↓
    ├─ Inicializar CameraBridgeViewBase (OpenCV)
    │   ├─ setCameraIndex(CAMERA_ID_BACK) por defecto
    │   ├─ setCvCameraViewListener(this)
    │   └─ enableView()
    ↓
    ├─ Ciclo de procesamiento de frames (onCameraFrame):
    │   ├─ 1. Capturar frame (Mat RGBA)
    │   ├─ 2. Si cámara frontal: flip horizontal (efecto espejo)
    │   ├─ 3. Clonar frame para procesamiento
    │   ├─ 4. LSMDetector.recognizeImage(frame)
    │   │   ├─ a. Convertir Mat → Bitmap
    │   │   ├─ b. Escalar a 224x224 píxeles
    │   │   ├─ c. Convertir Bitmap → ByteBuffer RGB
    │   │   ├─ d. interpreter.run(input, output)
    │   │   ├─ e. Dequantizar salida (byte → float)
    │   │   ├─ f. Encontrar clase con max probabilidad
    │   │   └─ g. Almacenar lastDetectedSign + lastConfidence
    │   ├─ 5. Obtener resultado de detector
    │   ├─ 6. runOnUiThread() → updateDetectionUI()
    │   └─ 7. Liberar frame clonado
    ↓
    ├─ Actualizar UI de detección (updateDetectionUI):
    │   ├─ Validar confianza >90%
    │   ├─ Validar que no sea "Fondo"
    │   ├─ Validar que la seña esté soportada (A,E,I,O,U,1,2,3)
    │   ├─ Evitar duplicados recientes (3 segundos)
    │   ├─ Obtener recurso de imagen (getSignImageResource)
    │   ├─ Formatear label (getSignLabel: "Letra A", "Número 1")
    │   ├─ Mostrar MaterialCardView con animación:
    │   │   ├─ Fade in (alpha 0→1)
    │   │   ├─ Scale up (0.8→1.0)
    │   │   └─ Duración: 300ms
    │   ├─ Programar ocultamiento automático (2 segundos)
    │   └─ Ocultar con animación inversa
    ↓
    ├─ Funcionalidades adicionales:
    │   ├─ Botón cambiar cámara (FloatingActionButton)
    │   │   ├─ disableView()
    │   │   ├─ Alternar CAMERA_ID_BACK ↔ CAMERA_ID_FRONT
    │   │   ├─ setCameraIndex(newCameraId)
    │   │   └─ enableView()
    │   └─ Botón regresar (MaterialButton) → finish()
    ↓
    └─ Al salir (onDestroy):
        ├─ mOpenCvCameraView.disableView()
        ├─ lsmDetector.close() → liberar Interpreter
        ├─ Liberar Mats (mRgba, mGray)
        └─ Cancelar Handlers pendientes
```

### Flujo de Gestión de Cuenta
```
ConfiguracionActivity
    ├─ Mostrar información del usuario
    │   ├─ Email (no editable)
    │   ├─ Proveedor de autenticación (email/google)
    │   └─ Fecha de registro (si aplica)
    ├─ Opciones:
    │   ├→ Cerrar sesión
    │   │   ├─ FirebaseAuth.getInstance().signOut()
    │   │   ├─ GoogleSignInClient.signOut() (si es Google)
    │   │   ├─ Limpiar caché local
    │   │   └→ Regresar a Inicio (finish all)
    │   └→ Eliminar cuenta
    │       ├─ Mostrar AlertDialog de confirmación
    │       ├─ Verificar autenticación reciente
    │       ├─ Eliminar documento de Firestore
    │       ├─ FirebaseAuth.currentUser.delete()
    │       └→ Regresar a Inicio (finish all)
    └→ Regresar (finish)
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
- **Lenguaje**: Java 11 puro (sin Kotlin, sin lambdas complejas, sin features modernos)
- **Arquitectura**: Activity-Based estricta
  - ❌ Sin Fragments
  - ❌ Sin ViewModel / LiveData
  - ❌ Sin Repository Pattern
  - ❌ Sin Data Binding / View Binding
  - ✅ Llamadas directas a Firebase SDK desde Activities
- **Navegación**: Intents explícitos entre Activities (`startActivity(new Intent(this, Target.class))`)
- **Async**: Firebase Tasks API con `.addOnCompleteListener()` / `.addOnSuccessListener()`
- **Persistencia**: 
  - Cloud Firestore para datos de usuario
  - Assets para recursos estáticos (modelo, imágenes)
  - SharedPreferences no utilizado en MVP actual

### Naming Conventions
```java
// Classes (PascalCase)
MainActivity
LSMDetector
LetraAdapter
TraducirActivity

// Variables y métodos (camelCase)
editTextEmail
mAuth
btnLogin
updateUI()
isNetworkAvailable()

// Variables de instancia privadas (prefijo 'm' opcional pero común en OpenCV)
private Mat mRgba;
private Mat mGray;
private CameraBridgeViewBase mOpenCvCameraView;

// Constantes (UPPER_SNAKE_CASE)
private static final String TAG = "TraducirActivity";
private static final int RC_SIGN_IN = 20;
private static final int CAMERA_PERMISSION_REQUEST = 100;
private static final long DISPLAY_DURATION_MS = 2000;

// Resources XML (snake_case)
activity_main.xml
btn_login
ic_letra_a
detection_card
tv_sign_label
```

### Patterns Comunes en el Proyecto

**1. UI State Management (Loading States)**:
```java
// Pattern repetido en Inicio.java, SignUpActivity.java
private void showLoadingState() {
    progressBar.setVisibility(View.VISIBLE);
    textViewSignUp.setVisibility(View.GONE);
    btnLogin.setEnabled(false);
    google_sign_in.setEnabled(false);
}

private void hideLoadingState() {
    progressBar.setVisibility(View.GONE);
    textViewSignUp.setVisibility(View.VISIBLE);
    btnLogin.setEnabled(true);
    google_sign_in.setEnabled(true);
}
```

**2. Network Validation (antes de operaciones Firebase)**:
```java
private boolean isNetworkAvailable() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
    return activeNetwork != null && activeNetwork.isConnected();
}

// Uso:
if (!isNetworkAvailable()) {
    Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
    return;
}
```

**3. Keyboard Management (helper estático)**:
```java
public static void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    View view = activity.getCurrentFocus();
    if (view == null) {
        view = new View(activity);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
}

// Llamada antes de operaciones async:
hideKeyboard(this);
```

**4. Edge-to-Edge Pattern (en todas las Activities)**:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);  // Habilitar edge-to-edge
    setContentView(R.layout.activity_name);
    
    // Manejar window insets
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        // El padding se maneja en layout XML con fitsSystemWindows o spacing manual
        return insets;
    });
}
```

**5. Firebase Authentication Pattern**:
```java
// Email/Password Login
mAuth.signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(this, task -> {
        hideLoadingState();
        if (task.isSuccessful()) {
            FirebaseUser user = mAuth.getCurrentUser();
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Principal.class));
            finish();
        } else {
            String errorMsg = task.getException() != null ? 
                task.getException().getMessage() : "Error desconocido";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        }
    });
```

**6. Firestore CRUD Pattern**:
```java
// Escribir documento
Map<String, Object> userData = new HashMap<>();
userData.put("uid", user.getUid());
userData.put("nombre", displayName);
userData.put("correo", email);
userData.put("fotoURL", photoUrl != null ? photoUrl : "");
userData.put("provider", "email"); // o "google"

db.collection("usuarios").document(user.getUid())
    .set(userData)
    .addOnSuccessListener(aVoid -> {
        Log.d(TAG, "Usuario guardado en Firestore");
    })
    .addOnFailureListener(e -> {
        Log.e(TAG, "Error al guardar usuario: " + e.getMessage());
    });

// Leer documento
db.collection("usuarios").document(user.getUid())
    .get()
    .addOnSuccessListener(documentSnapshot -> {
        if (documentSnapshot.exists()) {
            String nombre = documentSnapshot.getString("nombre");
            textViewName.setText(nombre);
        }
    });
```

**7. RecyclerView Pattern con ViewHolder**:
```java
// Adapter (LetraAdapter.java, NumeroAdapter.java)
public class LetraAdapter extends RecyclerView.Adapter<LetraAdapter.LetraViewHolder> {
    private List<Letra> letras;
    private OnLetraClickListener listener;
    
    @NonNull
    @Override
    public LetraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_letra, parent, false);
        return new LetraViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull LetraViewHolder holder, int position) {
        Letra letra = letras.get(position);
        holder.bind(letra, listener);
    }
    
    static class LetraViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        
        void bind(Letra letra, OnLetraClickListener listener) {
            textView.setText(letra.getNombre());
            imageView.setImageResource(letra.getImagenResId());
            itemView.setOnClickListener(v -> listener.onLetraClick(letra));
        }
    }
}
```

**8. OpenCV Camera Pattern** (TraducirActivity.java):
```java
// Implementar CvCameraViewListener2
@Override
public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
    mRgba = inputFrame.rgba();
    mGray = inputFrame.gray();
    
    // Si es cámara frontal, aplicar efecto espejo
    if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_FRONT) {
        Core.flip(mRgba, mRgba, 1); // 1 = flip horizontal
    }
    
    // Procesamiento del frame
    if (lsmDetector != null) {
        Mat frameToProcess = mRgba.clone();
        lsmDetector.recognizeImage(frameToProcess);
        frameToProcess.release();
    }
    
    return mRgba; // Retornar frame procesado para visualización
}
```

**9. TensorFlow Lite Inference Pattern** (LSMDetector.java):
```java
// Preparar entrada
Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);

// Preparar salida (modelo cuantizado)
byte[][] output = new byte[1][labelList.size()];

// Ejecutar inferencia
interpreter.run(byteBuffer, output);

// Procesar salida
int maxIndex = 0;
float maxConfidence = (output[0][0] & 0xFF) / 255.0f; // Dequantización

for (int i = 1; i < labelList.size(); i++) {
    float confidence = (output[0][i] & 0xFF) / 255.0f;
    if (confidence > maxConfidence) {
        maxConfidence = confidence;
        maxIndex = i;
    }
}

String detectedSign = labelList.get(maxIndex);
```

### Comentarios y Documentación
- **Español**: Mensajes de usuario, Toast, AlertDialog, logs de negocio
- **Inglés**: Comentarios técnicos, nombres de variables, logs de debugging
- **Javadoc**: En clases principales públicas (LSMDetector, Adapters)
```java
/**
 * Detector de señas LSM usando TensorFlow Lite
 * Adaptado para clasificación de imágenes (Teachable Machine)
 */
public class LSMDetector {
    // ...
}
```

### Manejo de Errores
```java
// Try-catch para operaciones críticas
try {
    lsmDetector = new LSMDetector(getAssets(), "model.tflite", "labels.txt");
    Log.d(TAG, "Detector LSM inicializado correctamente");
} catch (IOException e) {
    Log.e(TAG, "Error al cargar modelo LSM: " + e.getMessage());
    e.printStackTrace();
    Toast.makeText(this, "Error al cargar el modelo", Toast.LENGTH_LONG).show();
    finish();
}

// Validación de nulls
if (user != null && user.getEmail() != null) {
    textViewEmail.setText(user.getEmail());
}
```

### Gestión de Recursos
```java
// Siempre liberar recursos en onDestroy
@Override
public void onDestroy() {
    super.onDestroy();
    if (mOpenCvCameraView != null) {
        mOpenCvCameraView.disableView();
    }
    if (lsmDetector != null) {
        lsmDetector.close(); // Llama a interpreter.close()
    }
    if (mRgba != null) {
        mRgba.release();
    }
    if (mGray != null) {
        mGray.release();
    }
}

// Reciclar bitmaps después de uso
if (bitmap != scaledBitmap) {
    bitmap.recycle();
}
scaledBitmap.recycle();
```

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
- ⚠️ **CRÍTICO**: El archivo `app/google-services.json` NO debe subirse al repositorio público
  - Contiene API keys y configuración sensible de Firebase
  - Añadido a `.gitignore` para prevenir commits accidentales
  - Cada desarrollador debe descargar su propia copia de Firebase Console
- ⚠️ **CRÍTICO**: Verificar que el SHA-1 del debug keystore esté registrado en Firebase Console
  - Ubicación del keystore: `C:\Users\<TU_USUARIO>\.android\debug.keystore`
  - Generar SHA-1: `keytool -list -v -keystore debug.keystore -alias AndroidDebugKey -storepass android -keypass android`
  - Agregar en Firebase Console → Project Settings → Your apps → SHA certificate fingerprints
  - Sin esto, Google Sign-In generará error 10 (DEVELOPER_ERROR)
- ⚠️ **CRÍTICO**: Firebase Firestore rules están en modo development (open read/write)
  ```javascript
  // Reglas actuales (INSEGURO para producción)
  match /{document=**} {
    allow read, write: if true;
  }
  
  // Reglas recomendadas para producción:
  match /usuarios/{userId} {
    allow read: if request.auth != null && request.auth.uid == userId;
    allow write: if request.auth != null && request.auth.uid == userId;
  }
  ```
- ⚠️ Validar siempre conexión a internet antes de operaciones Firebase (`isNetworkAvailable()`)
- ⚠️ La constante `default_web_client_id` en `strings.xml` es auto-generada por `google-services.json`
  - No editar manualmente este valor
  - Se regenera automáticamente en cada sincronización de Gradle

### Permisos y Privacy
- **Permisos requeridos**:
  - `CAMERA`: Solicitado en runtime al abrir TraducirActivity (Android 6.0+)
  - `INTERNET`: Declarado en manifest, concedido automáticamente
  - `ACCESS_NETWORK_STATE`: Para validar conectividad
  - `WRITE_EXTERNAL_STORAGE`: Solo para SDK ≤28 (deprecado en Android 10+)
  - `READ_EXTERNAL_STORAGE`: Solo para SDK ≤32 (scoped storage en Android 11+)
- **Datos recopilados**:
  - Nombre completo (solo en registro)
  - Email (Firebase Authentication)
  - Foto de perfil (solo Google Sign-In)
  - UID de Firebase (identificador único)
- **Datos NO recopilados**:
  - Frames de video de la cámara (procesados localmente, no se envían a servidor)
  - Ubicación geográfica
  - Contactos o información del dispositivo
  - Historial de señas detectadas (no se persiste)
- **Cumplimiento de privacidad**:
  - Imágenes de cámara procesadas 100% en el dispositivo
  - Modelo TensorFlow Lite ejecutado localmente (no cloud)
  - No se envían datos personales a terceros excepto Firebase (Google)

### Limitaciones Conocidas
1. **Google Sign-In requiere Google Play Services**
   - No funciona en dispositivos sin Google Play Services (ej: Huawei sin GMS, emuladores sin Google APIs)
   - Solución: Usar email/password como fallback
2. **Modelo de IA solo detecta 9 clases** (alcance MVP)
   - Vocales: A, E, I, O, U
   - Números: 1, 2, 3
   - Fondo (clase de rechazo)
   - Expandible en futuras versiones agregando más datos al dataset
3. **Sin modo offline para autenticación**
   - Login requiere conexión a internet (Firebase Authentication)
   - Posible mejora: Implementar cache de sesión con Room
4. **Sin internacionalización** (app solo en español)
   - Todos los strings hardcodeados o en `strings.xml` en español
   - Posible mejora: Agregar archivos `strings-en.xml`, `strings-fr.xml`, etc.
5. **Orientación de pantalla forzada**
   - TraducirActivity: Solo landscape (optimizado para cámara + panel lateral)
   - Resto de Activities: Solo portrait (mejor UX para formularios y listas)
   - No hay soporte para modo libre o rotación dinámica
6. **Detección afectada por condiciones ambientales**:
   - Requiere buena iluminación (luz natural o artificial)
   - Fondo uniforme mejora precisión (evitar fondos complejos)
   - Distancia óptima: 30-60 cm de la cámara
   - Mano centrada en el frame

### Performance y Optimización
- **Modelo TensorFlow Lite optimizado**:
  - Cuantización INT8 (uint8) reduce tamaño de ~5MB a ~1-2MB
  - Reduce latencia de inferencia en ~40-60% vs modelo float32
  - Input/output en bytes directos (sin conversión float)
- **Imágenes en formato WebP**:
  - Tamaño ~30-50% menor que PNG
  - Calidad visual equivalente
  - Carga más rápida en RecyclerViews
- **RecyclerView con ViewHolder pattern**:
  - Reutilización de vistas (no inflar layouts repetidamente)
  - Smooth scrolling incluso con 27-30 items
- **Bitmaps gestionados correctamente**:
  - `bitmap.recycle()` después de uso
  - Glide maneja caché automáticamente
  - OpenCV Mat liberados con `mat.release()`
- **Threading**:
  - TensorFlow Lite usa 4 threads de CPU (`Interpreter.Options.setNumThreads(4)`)
  - Firebase callbacks ejecutados automáticamente en main thread
  - OpenCV `onCameraFrame()` ejecutado en background thread
  - `runOnUiThread()` usado para actualizar UI desde callbacks de OpenCV

### Debugging y Logs
- **Tags de Log** consistentes:
  ```java
  private static final String TAG = "NombreActivity";
  Log.d(TAG, "Mensaje de debug");
  Log.e(TAG, "Mensaje de error: " + e.getMessage());
  ```
- **Niveles de log usados**:
  - `Log.d()`: Debugging general (flujo de la app)
  - `Log.i()`: Información importante (detecciones exitosas)
  - `Log.e()`: Errores (excepciones, fallos de Firebase)
  - `Log.w()`: Advertencias (señas no soportadas)
- **Filtrar logs en Logcat**:
  ```
  tag:LSMDetector           # Solo logs del detector
  tag:TraducirActivity      # Solo logs de traducción
  tag:Firebase              # Logs de Firebase SDK
  ```

### Resolución de Problemas Comunes

| Problema | Causa Probable | Solución |
|----------|---------------|----------|
| Google Sign-In Error 10 | SHA-1 no configurado en Firebase Console | Generar SHA-1 con keytool y agregarlo en Firebase |
| "Network error" durante auth | Sin conexión a internet | Validar `isNetworkAvailable()` antes de login |
| Firestore write cuelga/falla | Network timeout o reglas de seguridad | Verificar internet + reglas en Firebase Console |
| TensorFlow Lite crash | Archivo model.tflite corrupto o missing | Re-descargar modelo desde assets |
| Cámara no inicia | Permiso denegado o OpenCV no inicializado | Verificar permisos + OpenCVLoader.initDebug() |
| Detecciones incorrectas | Iluminación pobre o fondo complejo | Mejorar iluminación + usar fondo uniforme |
| Password toggle no funciona | Uso de custom CheckBox en vez de Material | Usar `app:endIconMode="password_toggle"` en TextInputLayout |
| Portrait lock no aplicado | `screenOrientation` faltante en manifest | Agregar `android:screenOrientation="portrait"` a todas las Activities |
| Gradle sync falla | Version catalog mal configurado | Usar `libs.X.Y` en vez de strings directos |
| App crash en inicio | google-services.json faltante | Descargar desde Firebase Console y colocar en `app/` |

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
- [x] Modo oscuro completo

### 🚧 Próximas Features (Post-MVP)
- [ ] Módulo "Palabras Comunes" en Aprender
- [ ] Expansión de señas detectables (consonantes, más números)
- [ ] Tutorial de primera vez para nuevos usuarios
- [ ] Tests unitarios e instrumentados
- [ ] Leaderboard con Firebase Firestore
- [ ] Compartir logros en redes sociales
- [ ] Feedback háptico en detecciones exitosas

### 📊 Estado Actual
- **Versión**: 1.0.0
- **Branch Principal**: `main`
- **Último Commit**: Octubre 2025
- **Estado**: ✅ **MVP Completado y Funcional**
- **Coverage de Tests**: 0% (infraestructura lista, implementación pendiente)
- **Tamaño del APK**: ~15-20 MB (sin ProGuard)
- **Min SDK**: 24 (Android 7.0 - Nougat)
- **Target SDK**: 35 (Android 15)
- **Dispositivos compatibles**: 95%+ de dispositivos Android activos

### 🎯 Objetivos a Corto Plazo (Próximos 3 meses)
1. ✅ Completar MVP (HECHO)
2. 🔄 Implementar tests básicos (En progreso)
3. 📱 Beta testing con 10-20 usuarios reales
4. 🤖 Expandir modelo a todas las letras del abecedario
5. 🎮 Agregar sistema de gamificación básico

## 👥 Equipo de Desarrollo

### Desarrolladores
- **Alan Raul Arellano Gonzalez** - [@alan-are](https://github.com/alan-are)
  - Líder de proyecto y arquitectura
- **Alberto Leonel Mejía Hernández**
  - Desarrollo de features y UI/UX
- **Diego Alejandro Guzmán Paniagua** [@MrBreadWater73](https://github.com/MrBreadWater73)
  - Integración de IA y módulo Traducir

### Información del Proyecto
- **Universidad/Institución**: [Universidad de Guadalajara]
- **Proyecto Modular 2025B**: [Aplicación móvil para el aprendizaje y traducción de la lengua de señas mexicana: LSMovil]
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
- **Autores de los Datasets**: Por los recursos necesarios para el entrenamiento del modelo de IA

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

**Última actualización**: 29 de octubre de 2025  
**Versión**: 1.0.0  
**Estado**: ✅ MVP Completado y Funcional

**Modelo de IA**: TensorFlow Lite cuantizado (uint8)  
**Clases detectables**: 9 (Vocales: A,E,I,O,U | Números: 1,2,3 | Fondo)  
**Precisión**: >90% con umbral de confianza

---

<p align="center">
  <strong>Hecho con ❤️ y 🤟 para la comunidad LSM</strong>
</p>
