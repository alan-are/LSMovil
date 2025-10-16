# LSMovil 📱

Aplicación móvil Android para gestión y servicios móviles, desarrollada con Java y Firebase.

## 📋 Descripción

LSMovil es una aplicación Android moderna que proporciona un sistema completo de autenticación y gestión de usuarios mediante Firebase. La aplicación ofrece múltiples métodos de inicio de sesión y una interfaz intuitiva con Material Design.

## ✨ Características

- 🔐 **Autenticación Múltiple**
  - Inicio de sesión con email y contraseña
  - Inicio de sesión con Google
  - Recuperación de contraseña
  - Registro de nuevos usuarios

- 👤 **Gestión de Perfiles**
  - Perfiles de usuario almacenados en Firestore
  - Soporte para fotos de perfil
  - Información personalizada por usuario

- 🎨 **Interfaz de Usuario**
  - Diseño Material Design
  - Navigation Drawer para navegación principal
  - Animaciones y transiciones fluidas
  - Modo portrait optimizado

- ⚙️ **Funcionalidades Adicionales**
  - Configuración de la aplicación
  - Sección de soporte
  - Información sobre la aplicación

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 11
- **SDK Android**: Min SDK 24 (Android 7.0) - Target SDK 35
- **Sistema de Build**: Gradle (Kotlin DSL)
- **Backend**: Firebase
  - Firebase Authentication
  - Cloud Firestore
  - Realtime Database
  - Firebase Analytics
- **Dependencias Principales**:
  - Google Play Services (Auth)
  - Material Design Components 1.9.0
  - Glide 4.16.0 (Carga de imágenes)
  - AndroidX Libraries

## 📦 Requisitos Previos

- Android Studio Ladybug | 2024.2.1 o superior
- JDK 11 o superior
- Android SDK con SDK 35 instalado
- Cuenta de Firebase con proyecto configurado
- Google Play Services instalado en el dispositivo/emulador

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
│   │   │   │   ├── MainActivity.java          # Splash screen
│   │   │   │   ├── Inicio.java                # Pantalla de login
│   │   │   │   ├── SignUpActivity.java        # Registro de usuarios
│   │   │   │   ├── ForgotPasswordActivity.java # Recuperar contraseña
│   │   │   │   ├── Principal.java             # Pantalla principal
│   │   │   │   ├── ConfiguracionActivity.java # Configuración
│   │   │   │   ├── SoporteActivity.java       # Soporte
│   │   │   │   └── AcercaActivity.java        # Acerca de
│   │   │   ├── res/
│   │   │   │   ├── layout/                    # Layouts XML
│   │   │   │   ├── drawable/                  # Recursos gráficos
│   │   │   │   ├── menu/                      # Menús
│   │   │   │   └── values/                    # Strings, colors, themes
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                       # Tests instrumentados
│   │   └── test/                              # Tests unitarios
│   ├── build.gradle.kts                       # Configuración de la app
│   └── google-services.json                   # Config de Firebase (no incluido)
├── gradle/
│   └── libs.versions.toml                     # Catálogo de versiones
├── build.gradle.kts                           # Build principal
├── settings.gradle.kts                        # Configuración Gradle
└── README.md
```

## 🔐 Esquema de Base de Datos

### Firestore - Colección `usuarios`

```javascript
{
  uid: String,           // Firebase Auth UID
  nombre: String,        // Nombre del usuario
  correo: String,        // Email del usuario
  fotoURL: String,       // URL de foto de perfil (puede estar vacío)
  provider: String       // "email" o "google"
}
```

## 🎯 Flujo de Autenticación

1. **Splash Screen** (`MainActivity`) - 3 segundos
2. **Login** (`Inicio`)
   - Validación de sesión activa
   - Opción de login con email/password
   - Opción de login con Google
3. **Registro** (`SignUpActivity`) - Solo para usuarios de email
4. **Pantalla Principal** (`Principal`) - Acceso a funcionalidades

## 🧪 Testing

```powershell
# Ejecutar tests unitarios
.\gradlew test

# Ejecutar tests instrumentados
.\gradlew connectedAndroidTest
```

## 📱 Requisitos del Dispositivo

- Android 7.0 (API 24) o superior
- Conexión a Internet activa
- Google Play Services actualizado (para Google Sign-In)

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Convenciones de Código

- **Idioma**: Java 11
- **Arquitectura**: Activity-based (sin Fragments)
- **Async**: Firebase Tasks (no RxJava ni Coroutines)
- **Naming**: Camel case para variables, Pascal case para clases
- **Comentarios**: Español para documentación de negocio, inglés para código técnico

## ⚠️ Notas Importantes

- El archivo `google-services.json` NO debe subirse al repositorio
- Todas las Activities están bloqueadas en orientación portrait
- Siempre validar conexión a internet antes de operaciones Firebase
- Los recursos de strings tienen mínima traducción (app principalmente en español)

## 📄 Licencia

Este proyecto es privado y está protegido por derechos de autor.

## 👥 Autores

- **Alan Are** - [@alan-are](https://github.com/alan-are)
- **Diego** - Rama de desarrollo `diego`

## 📧 Contacto

Para preguntas o soporte, contactar al equipo de desarrollo.

---

**Última actualización**: Octubre 2025  
**Versión**: 1.0.0
