# LSMovil 📱🤟

Aplicación móvil Android educativa para el aprendizaje de **Lenguaje de Señas Mexicano (LSM)** con inteligencia artificial.

## 📋 Descripción

**LSMovil** es una aplicación Android que combina educación interactiva e inteligencia artificial para facilitar el aprendizaje del Lenguaje de Señas Mexicano. Desarrollada completamente en **Java**, ofrece dos modos principales:

- **🎓 Aprender**: Módulo educativo con el abecedario LSM completo (27 letras) y números del 0 al 10.
- **🤖 Traducir**: Detector de señas en tiempo real usando TensorFlow Lite y OpenCV, reconociendo 5 vocales (A, E, I, O, U) y 3 números (1, 2, 3) con precisión superior al 90%.

## ✨ Características Principales

### 🎓 Módulo Aprender
- Abecedario LSM completo (27 letras: A-Z, LL, Ñ, RR)
- Números del 0 al 10
- Imágenes ilustrativas y descripciones detalladas
- Interfaz intuitiva con Material Design 3

### 🤖 Módulo Traducir (IA)
- Detección en tiempo real con TensorFlow Lite
- Reconoce 5 vocales (A, E, I, O, U) y 3 números (1, 2, 3)
- Precisión >90% con umbral de confianza
- Procesamiento de video con OpenCV
- Cambio entre cámara frontal y trasera

### 🔐 Sistema de Autenticación
- Inicio de sesión con email/contraseña
- Google Sign-In
- Recuperación de contraseña
- Gestión de perfiles con Firebase Firestore

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 11
- **SDK Android**: Min SDK 24 (Android 7.0) - Target SDK 35
- **Build**: Gradle 8.13.0 con Kotlin DSL

### Backend
- **Firebase BOM 32.7.0**: Authentication, Cloud Firestore, Analytics

### Inteligencia Artificial
- **TensorFlow Lite 2.14.0**: Modelo cuantizado (uint8, 224x224, 9 clases)
- **OpenCV 3.4.13**: Procesamiento de video y captura de cámara

### UI
- **Material Design 3**: Material Components 1.9.0
- **Glide 4.16.0**: Carga de imágenes
- **Google Play Services Auth**: Google Sign-In

## 📦 Requisitos

- **Android Studio**: Ladybug | 2024.2.1 o superior
- **JDK**: 11 o superior
- **Cuenta de Firebase**: Proyecto configurado
- **Dispositivo Android**: SDK 24+ con cámara funcional

## 🚀 Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/alan-are/LSMovil.git
cd LSMovil
```

### 2. Configurar Firebase
1. Crear proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Registrar app Android: `com.example.lsmovil`
3. Descargar `google-services.json` y colocar en `app/`
4. Habilitar: Authentication (Email/Password y Google), Cloud Firestore

### 3. Configurar Google Sign-In
Obtener SHA-1 del keystore:
```powershell
keytool -list -v -keystore C:\Users\<USUARIO>\.android\debug.keystore -alias AndroidDebugKey -storepass android -keypass android
```
Agregar SHA-1 en Firebase Console → Project Settings → SHA certificate fingerprints

### 4. Compilar y ejecutar
```powershell
.\gradlew clean assembleDebug
.\gradlew installDebug
```

## 📁 Estructura del Proyecto

```plaintext
LSMovil/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/lsmovil/
│   │   │   ├── MainActivity.java           # Splash screen
│   │   │   ├── Inicio.java                # Login
│   │   │   ├── SignUpActivity.java        # Registro
│   │   │   ├── Principal.java             # Pantalla principal
│   │   │   ├── AprenderActivity.java      # Hub educativo
│   │   │   ├── AbecedarioActivity.java    # Grid de 27 letras
│   │   │   ├── NumerosActivity.java       # Números 0-10
│   │   │   ├── TraducirActivity.java      # Detector IA
│   │   │   ├── LSMDetector.java           # TensorFlow Lite
│   │   │   └── ConfiguracionActivity.java
│   │   ├── res/
│   │   │   ├── layout/                    # Layouts XML
│   │   │   ├── drawable/                  # Imágenes LSM (WebP)
│   │   │   └── values/
│   │   ├── assets/
│   │   │   ├── model.tflite              # Modelo IA
│   │   │   └── labels.txt
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json              # NO SUBIR AL REPO
├── openCVLibrary3413/                    # Módulo OpenCV
├── gradle/libs.versions.toml
└── README.md
```

## 🔐 Esquema de Base de Datos

### Firestore - Colección `usuarios`
Cada documento usa el `uid` de Firebase Auth como ID.

```javascript
{
  uid: String,        // Firebase Auth UID
  nombre: String,     // Nombre completo
  correo: String,     // Email
  fotoURL: String,    // URL de foto (vacío para email, URL para Google)
  provider: String    // "email" | "google"
}
```

### Modelo TensorFlow Lite
- **Archivo**: `app/src/main/assets/model.tflite`
- **Tipo**: Clasificación de imágenes (cuantizado uint8)
- **Input**: 224x224x3 RGB
- **Output**: 9 clases (A, E, I, O, U, 1, 2, 3, Fondo)
- **Precisión**: >90%
- **Entrenamiento**: Google Teachable Machine

## 📱 Flujo de la Aplicación

1. **Splash Screen** → Verificar sesión activa
2. **Login/Registro** → Firebase Authentication (email o Google)
3. **Pantalla Principal** → Navigation Drawer con opciones
4. **Aprender** → Abecedario (27 letras) y Números (0-10)
5. **Traducir** → Detector en tiempo real con cámara
6. **Configuración** → Gestión de cuenta

## ⚠️ Notas Importantes

### Seguridad
- ⚠️ **NO subir `google-services.json` al repositorio**
- ⚠️ **SHA-1 del keystore debe estar en Firebase Console** para Google Sign-In
- ⚠️ **Firestore rules en modo desarrollo**: Cambiar antes de producción

### Limitaciones
- Google Sign-In requiere Google Play Services
- Detección de IA: Solo 9 clases (A, E, I, O, U, 1, 2, 3, Fondo)
- Autenticación requiere conexión a internet
- Mejores resultados con buena iluminación y fondo uniforme

## 📄 Licencia

Este proyecto es privado y está protegido por derechos de autor.

## 🎯 Estado del Proyecto

**Versión**: 1.0.0  
**Estado**: ✅ MVP Completado y Funcional  
**Última actualización**: Octubre 2025

### Características Implementadas
- ✅ Sistema de autenticación (email + Google)
- ✅ Módulo Aprender (27 letras + 11 números)
- ✅ Módulo Traducir con IA en tiempo real
- ✅ UI Material Design 3
- ✅ Integración OpenCV y TensorFlow Lite

### Próximas Mejoras
- [ ] Expansión del modelo (más letras y números)
- [ ] Tests unitarios e instrumentados
- [ ] Tutorial para nuevos usuarios
- [ ] Sistema de progreso del usuario

## 👥 Equipo de Desarrollo

- **Alan Raul Arellano Gonzalez** - [@alan-are](https://github.com/alan-are)
- **Alberto Leonel Mejía Hernández**
- **Diego Alejandro Guzmán Paniagua** - [@MrBreadWater73](https://github.com/MrBreadWater73)

**Universidad de Guadalajara**  
**Proyecto Modular 2025B**

## 📚 Recursos

- [Firebase Console](https://console.firebase.google.com/)
- [TensorFlow Lite](https://www.tensorflow.org/lite)
- [Material Design 3](https://m3.material.io/)
- [Google Teachable Machine](https://teachablemachine.withgoogle.com/)

## 📄 Licencia
Este proyecto se distribuye bajo la Licencia MIT

---

<p align="center">
  <strong>Hecho con ❤️ y 🤟 para la comunidad LSM</strong>
</p>

---
