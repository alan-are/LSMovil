# 🚀 PLAN MVP - Detector LSM para LSMovil

## 📊 Estado Actual
- ✅ Proyecto LSMovil con sección "Aprender" funcional
- ✅ Módulo OpenCV copiado al proyecto
- ✅ DatasetCaptureActivity creada (herramienta de captura)
- ⏳ Pendiente: Configurar dependencias
- ⏳ Pendiente: Generar dataset
- ⏳ Pendiente: Entrenar modelo
- ⏳ Pendiente: Implementar sección "Traducir"

---

## 🎯 MVP - Alcance Definido

### Señas a Detectar (5 total):
- **Letras**: A, B, C
- **Números**: 1, 2

### Funcionalidades MVP:
1. **Capturar Dataset** (DatasetCaptureActivity)
   - 100 imágenes por seña
   - Auto-captura cada 1 segundo
   - Progreso visual

2. **Entrenar Modelo** (Google Teachable Machine)
   - Modelo ligero para móvil
   - Export a TensorFlow Lite
   - Precisión objetivo: >85%

3. **Sección Traducir** (TraducirActivity)
   - Detección en tiempo real
   - Mostrar seña detectada + confianza
   - Feedback visual con recuadros

---

## 📋 PASO A PASO - Configuración Inicial

### PASO 1: Actualizar Archivos de Configuración

#### 1.1 Modificar `settings.gradle.kts`
Agregar módulo OpenCV al proyecto

#### 1.2 Modificar `app/build.gradle.kts`
Agregar dependencias:
- OpenCV (módulo local)
- TensorFlow Lite
- TensorFlow Lite Support
- TensorFlow Lite GPU

#### 1.3 Actualizar `AndroidManifest.xml`
- Permiso de CAMERA
- Permiso de WRITE_EXTERNAL_STORAGE
- Registrar DatasetCaptureActivity
- Registrar TraducirActivity (futura)

---

## 📦 PASO 2: Generar Dataset (3-5 días)

### Objetivo:
Capturar 500 imágenes totales (100 por cada seña)

### Proceso:
1. **Abrir DatasetCaptureActivity**
   - Botón en menu Configuración o desarrollo

2. **Por cada seña (A, B, C, 1, 2):**
   - Colocar mano en posición correcta
   - Activar auto-captura
   - Variar:
     - Distancia a la cámara
     - Iluminación
     - Fondo
     - Ángulo de la mano
     - Posición en el frame

3. **Involucrar a otras personas**
   - Ideal: 3-5 personas diferentes
   - Diferentes tonos de piel
   - Diferentes tamaños de mano

4. **Ubicación de imágenes:**
   ```
   /storage/emulated/0/Pictures/LSM_Dataset/
   ├── A/
   │   ├── img_A_20251019_143022_001.jpg
   │   ├── img_A_20251019_143023_002.jpg
   │   └── ... (100 imágenes)
   ├── B/
   ├── C/
   ├── 1/
   └── 2/
   ```

### Tips para Dataset de Calidad:
- ✅ Buena iluminación (pero varía entre capturas)
- ✅ Fondos diferentes (no solo blanco)
- ✅ Mano centrada pero no siempre exactamente
- ✅ Pequeñas variaciones de posición de dedos
- ❌ Evitar imágenes muy borrosas
- ❌ Evitar imágenes donde no se vea la mano completa

---

## 🧠 PASO 3: Entrenar Modelo con Teachable Machine (1 día)

### 3.1 Exportar Dataset del Dispositivo
```powershell
# Conectar dispositivo por USB
# Ejecutar desde PC:
adb pull /sdcard/Pictures/LSM_Dataset C:\Dataset_LSM
```

### 3.2 Usar Google Teachable Machine
1. Ir a: https://teachablemachine.withgoogle.com/train/image
2. Crear "Nuevo Proyecto de Imagen"
3. Para cada seña:
   - "Add a class" → Nombrar (A, B, C, 1, 2)
   - "Upload" → Seleccionar carpeta de imágenes
4. Hacer clic en "Train Model"
   - Epochs: 50
   - Batch size: 16
   - Learning rate: 0.001
5. Esperar entrenamiento (~10-20 minutos)
6. Probar con webcam
7. **Export Model**:
   - TensorFlow → TensorFlow Lite
   - Quantized (Int8) ← Más pequeño y rápido
   - Download

### 3.3 Copiar Modelo a LSMovil
```
LSMovil - copia/
└── app/
    └── src/
        └── main/
            └── assets/
                ├── lsm_model.tflite        ← Modelo descargado
                └── lsm_labels.txt          ← Etiquetas (A,B,C,1,2)
```

Contenido de `lsm_labels.txt`:
```
A
B
C
1
2
```

---

## 🎨 PASO 4: Implementar Sección Traducir (2-3 días)

### 4.1 Crear TraducirActivity
Basada en CameraActivity del proyecto hand_detection, pero adaptada:
- Usar modelo LSM en vez de hand_model
- UI mejorada con Material Design
- Mostrar seña + confianza
- Botón para regresar

### 4.2 Crear LSMDetector.java
Adaptación de objectDetectorClass.java:
- Cargar lsm_model.tflite
- Input size: 224x224 (Teachable Machine default)
- Normalización 0-1
- Retornar seña + confianza

### 4.3 Modificar Principal.java
Conectar botón "Traducir" con TraducirActivity

---

## 🧪 PASO 5: Testing y Ajustes (1-2 días)

### Casos de Prueba:
1. Detecta A correctamente
2. Detecta B correctamente  
3. Detecta C correctamente
4. Detecta 1 correctamente
5. Detecta 2 correctamente
6. No confunde señas similares
7. Funciona con diferentes iluminaciones
8. Funciona con diferentes usuarios

### Métricas Objetivo:
- Precisión: >85%
- FPS: >15
- Tiempo respuesta: <200ms

---

## 📅 CRONOGRAMA ESTIMADO

| Fase | Duración | Fecha Estimada |
|------|----------|----------------|
| Configuración Inicial | 0.5 días | 19 Oct |
| Generar Dataset | 3-5 días | 20-24 Oct |
| Entrenar Modelo | 1 día | 25 Oct |
| Implementar Traducir | 2-3 días | 26-28 Oct |
| Testing y Ajustes | 1-2 días | 29-30 Oct |
| **TOTAL MVP** | **7-11 días** | **~1 Nov** |

---

## 🔄 PRÓXIMOS PASOS INMEDIATOS

### HOY (19 Oct):
1. ✅ Actualizar `settings.gradle.kts`
2. ✅ Actualizar `app/build.gradle.kts`
3. ✅ Actualizar `AndroidManifest.xml`
4. ✅ Sincronizar proyecto en Android Studio
5. ✅ Compilar y verificar que no hay errores
6. ✅ Agregar botón temporal para abrir DatasetCaptureActivity

### MAÑANA (20 Oct):
1. Comenzar captura de dataset
2. Capturar 100 imágenes de "A"
3. Capturar 100 imágenes de "B"

### Siguientes Días:
- Completar dataset
- Entrenar modelo
- Implementar Traducir

---

## 📚 RECURSOS

### Teachable Machine:
- Tutorial oficial: https://youtu.be/kwcillcWOg0
- Documentación: https://teachablemachine.withgoogle.com/faq

### TensorFlow Lite:
- Android Guide: https://www.tensorflow.org/lite/android

### OpenCV Android:
- Documentación: https://docs.opencv.org/4.x/d0/d6c/tutorial_android_dev_intro.html

---

## ❓ PREGUNTAS FRECUENTES

**P: ¿Qué pasa si el modelo no es preciso?**
R: Opciones:
1. Capturar más imágenes por seña (150-200)
2. Ajustar parámetros de entrenamiento (más epochs)
3. Usar TensorFlow Lite Model Maker (más avanzado)

**P: ¿Puedo agregar más señas después?**
R: Sí, el proceso es el mismo:
1. Capturar dataset de nueva seña
2. Re-entrenar modelo con todas las señas
3. Actualizar lsm_labels.txt
4. Reemplazar modelo en assets

**P: ¿Funciona sin conexión a internet?**
R: Sí, una vez que el modelo está en assets/ funciona 100% offline.

**P: ¿Qué tan grande es el modelo?**
R: Teachable Machine genera modelos de ~1-5 MB (cuantizados).

---

## 🎉 RESULTADO FINAL MVP

Al completar estos pasos tendrás:
- ✅ App LSMovil con detección de 5 señas LSM
- ✅ Modo Aprender (ya existente)
- ✅ Modo Traducir (nuevo) con detección en tiempo real
- ✅ Herramienta de captura de dataset integrada
- ✅ Base para agregar más señas en el futuro

---

**Última actualización:** 19 de octubre de 2025
**Versión:** 1.0 - MVP

