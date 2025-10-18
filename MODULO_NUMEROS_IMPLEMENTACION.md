# Módulo de Números - Implementación LSMovil

## 📊 Estructura Creada

El módulo de **Números en LSM** replica completamente la arquitectura del módulo de Abecedario, siguiendo el patrón establecido.

---

## 📁 Archivos Creados

### **Java Classes (Modelo-Vista-Controlador)**

#### 1. `Numero.java` - Modelo de Datos
```java
package com.example.lsmovil;

public class Numero {
    private String numero;           // "0", "1", "2"... "10"
    private String descripcion;      // Instrucciones de cómo hacer la seña
    private int imagenResId;         // R.drawable.ic_numero_X
}
```

**Propiedades:**
- `numero`: Dígito o número como String
- `descripcion`: Texto explicativo de cómo hacer la seña
- `imagenResId`: ID del recurso drawable (imagen)

**Métodos:** Getters y setters para las 3 propiedades

---

#### 2. `NumeroAdapter.java` - Adaptador RecyclerView
```java
public class NumeroAdapter extends RecyclerView.Adapter<NumeroAdapter.NumeroViewHolder>
```

**Responsabilidades:**
- Inflar el layout `item_numero.xml` para cada elemento
- Vincular datos del objeto `Numero` con las vistas
- Manejar click events para mostrar el diálogo de detalle
- Crear y mostrar `dialog_numero_detalle.xml` con datos completos

**Método clave:**
```java
private void mostrarDialogoDetalle(Numero numero) {
    Dialog dialog = new Dialog(context);
    dialog.setContentView(R.layout.dialog_numero_detalle);
    // Configurar vistas con datos del número
    dialog.show();
}
```

---

#### 3. `NumerosActivity.java` - Activity Principal
```java
public class NumerosActivity extends AppCompatActivity
```

**Características:**
- Edge-to-edge habilitado con `EdgeToEdge.enable()`
- Toolbar Material 3 con botón de regreso
- RecyclerView con `GridLayoutManager(3)` (3 columnas)
- Método `cargarNumeros()` con datos de 0 a 10

**Configuración RecyclerView:**
```java
recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
adapter = new NumeroAdapter(this, numeros);
recyclerView.setAdapter(adapter);
```

---

### **Layouts XML (Material Design 3)**

#### 1. `activity_numeros.xml` - Pantalla Principal
```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout>
    <AppBarLayout>
        <MaterialToolbar /> <!-- Toolbar con título "Números" -->
    </AppBarLayout>
    
    <NestedScrollView>
        <LinearLayout>
            <TextView /> <!-- Título: "Números en LSM" -->
            <TextView /> <!-- Subtítulo: instrucciones -->
            <RecyclerView android:id="@+id/recyclerViewNumeros" />
        </LinearLayout>
    </NestedScrollView>
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

**Características:**
- AppBar con scroll behavior
- Edge-to-edge compatible
- RecyclerView con grid de 3 columnas

---

#### 2. `item_numero.xml` - Item de Grid
```xml
<MaterialCardView android:id="@+id/cardNumero">
    <LinearLayout>
        <ImageView android:id="@+id/imageViewNumero" />  <!-- 80dp × 80dp -->
        <TextView android:id="@+id/textViewNumero" />    <!-- Número en 24sp -->
    </LinearLayout>
</MaterialCardView>
```

**Estilo:**
- Elevated card con 12dp de corner radius
- Padding 12dp, margin 8dp
- Ripple effect con `selectableItemBackground`

---

#### 3. `dialog_numero_detalle.xml` - Diálogo Ampliado
```xml
<CardView>
    <LinearLayout>
        <!-- Header con colorSecondaryContainer -->
        <LinearLayout>
            <TextView android:id="@+id/textViewNumeroDialog" />  <!-- Número grande 32sp -->
            <ImageView android:id="@+id/btnCerrarDialog" />      <!-- Botón X -->
        </LinearLayout>
        
        <!-- Imagen ampliada 250dp -->
        <ImageView android:id="@+id/imageViewNumeroDialog" />
        
        <!-- Descripción completa -->
        <LinearLayout>
            <TextView text="Cómo hacer la seña:" />
            <TextView android:id="@+id/textViewDescripcionDialog" />
        </LinearLayout>
    </LinearLayout>
</CardView>
```

**Diseño:**
- Header con fondo `colorSecondaryContainer` (cyan claro)
- Imagen ampliada con fondo `colorSurfaceVariant`
- Descripción detallada con line spacing extra

---

## 🔗 Integración con AprenderActivity

### **Actualización AndroidManifest.xml**
```xml
<activity
    android:name=".NumerosActivity"
    android:exported="false"
    android:screenOrientation="portrait"/>
```

### **Actualización AprenderActivity.java**
```java
cardNumeros.setOnClickListener(v -> {
    // Navegar a módulo Números
    startActivity(new Intent(AprenderActivity.this, NumerosActivity.class));
});
```

**Antes:**
```java
Toast.makeText(this, "Módulo Números - Próximamente", Toast.LENGTH_SHORT).show();
```

**Después:**
```java
startActivity(new Intent(AprenderActivity.this, NumerosActivity.class));
```

---

## 📊 Datos Incluidos

### **Números del 0 al 10**

| Número | Descripción de la Seña | Drawable Placeholder |
|--------|------------------------|----------------------|
| **0** | Círculo con índice y pulgar | `ic_school` → `ic_numero_0` |
| **1** | Índice extendido | `ic_school` → `ic_numero_1` |
| **2** | Índice y medio en "V" | `ic_school` → `ic_numero_2` |
| **3** | Índice, medio y anular | `ic_school` → `ic_numero_3` |
| **4** | Cuatro dedos extendidos | `ic_school` → `ic_numero_4` |
| **5** | Todos los dedos abiertos | `ic_school` → `ic_numero_5` |
| **6** | Meñique + pulgar toca anular | `ic_school` → `ic_numero_6` |
| **7** | Meñique + anular + pulgar toca medio | `ic_school` → `ic_numero_7` |
| **8** | Meñique + anular + medio + pulgar toca índice | `ic_school` → `ic_numero_8` |
| **9** | Todos excepto índice + pulgar toca índice | `ic_school` → `ic_numero_9` |
| **10** | Puño con pulgar arriba giratorio | `ic_school` → `ic_numero_10` |

---

## 🎨 Diseño Material Design 3

### **Paleta de Colores**
- **Header Dialog**: `colorSecondaryContainer` (#2BB5BC - cyan claro)
- **Text Header**: `colorOnSecondaryContainer`
- **Cards**: `colorSurface` con elevación
- **Text**: `colorOnSurface` (títulos), `colorOnSurfaceVariant` (descripción)

### **Componentes Material**
- ✅ MaterialToolbar con navigation icon
- ✅ MaterialCardView con corner radius 12dp
- ✅ CoordinatorLayout con AppBarLayout
- ✅ NestedScrollView con fillViewport
- ✅ RecyclerView con GridLayoutManager

---

## 🚀 Cómo Agregar las Imágenes

### **1. Preparar las Imágenes**
Necesitas 11 imágenes de las señas LSM para números:
- `ic_numero_0.png` / `.jpg` / `.xml` (vector)
- `ic_numero_1.png`
- `ic_numero_2.png`
- ... hasta ...
- `ic_numero_10.png`

### **2. Colocar en el Proyecto**
```
LSMovil/
  app/
    src/
      main/
        res/
          drawable/
            ic_numero_0.png    ← Aquí van las imágenes
            ic_numero_1.png
            ic_numero_2.png
            ... etc
```

### **3. Actualizar el Código**
En `NumerosActivity.java`, método `cargarNumeros()`:

**Buscar:**
```java
R.drawable.ic_school  // Placeholder temporal
```

**Reemplazar con:**
```java
R.drawable.ic_numero_0  // Tu imagen real
R.drawable.ic_numero_1
// ... etc
```

**Ejemplo completo:**
```java
numeros.add(new Numero("0", 
    "Se forma un círculo con el dedo índice y el pulgar...", 
    R.drawable.ic_numero_0));  // ← Cambiar aquí

numeros.add(new Numero("1", 
    "Se extiende el dedo índice hacia arriba...", 
    R.drawable.ic_numero_1));  // ← Cambiar aquí
```

### **4. Recomendaciones de Imágenes**
- **Formato**: PNG con transparencia o JPG
- **Resolución**: 512×512px o 1024×1024px
- **Peso**: Optimizar a < 200KB cada una
- **Fondo**: Fondo claro uniforme o transparente
- **Encuadre**: Mano centrada, buen contraste

---

## 🔄 Arquitectura Replicada

### **Patrón Abecedario → Números**

| Componente Abecedario | Componente Números | Estado |
|----------------------|-------------------|--------|
| `Letra.java` | `Numero.java` | ✅ Creado |
| `LetraAdapter.java` | `NumeroAdapter.java` | ✅ Creado |
| `AbecedarioActivity.java` | `NumerosActivity.java` | ✅ Creado |
| `activity_abecedario.xml` | `activity_numeros.xml` | ✅ Creado |
| `item_letra.xml` | `item_numero.xml` | ✅ Creado |
| `dialog_letra_detalle.xml` | `dialog_numero_detalle.xml` | ✅ Creado |
| 29 letras cargadas | 11 números cargados | ✅ Implementado |
| GridLayoutManager(3) | GridLayoutManager(3) | ✅ Igual |
| Edge-to-edge | Edge-to-edge | ✅ Igual |

---

## ✅ Build Status

```bash
.\gradlew assembleDebug
# BUILD SUCCESSFUL in 44s
# 35 actionable tasks: 14 executed, 21 up-to-date
```

**Sin errores de compilación** ✅  
**Módulo completamente funcional** ✅  
**Falta solo agregar imágenes reales** ⚠️

---

## 📝 Próximos Pasos

### **Para el Desarrollador:**
1. ✅ **Código completo** - Todo implementado
2. ⚠️ **Imágenes pendientes** - Reemplazar `ic_school` por imágenes reales
3. ✅ **Navegación funcional** - Desde AprenderActivity → NumerosActivity
4. ✅ **Diálogos operativos** - Click en número muestra detalle

### **Para Agregar Más Números:**
Si deseas ampliar a números mayores (20, 30, 100, etc.):

```java
// En cargarNumeros()
numeros.add(new Numero("20", 
    "Descripción de la seña para 20", 
    R.drawable.ic_numero_20));
```

---

## 🎯 Funcionalidad Final

### **Flujo de Usuario:**
1. Usuario abre app → Principal
2. Tap en "APRENDER" → AprenderActivity
3. Tap en card "Números" → **NumerosActivity** (nuevo)
4. Ve grid 3×4 con números 0-10
5. Tap en cualquier número → Dialog ampliado con imagen y descripción
6. Tap en X o fuera del diálogo → Cierra
7. Botón back en toolbar → Regresa a AprenderActivity

---

**Módulo creado por:** GitHub Copilot  
**Fecha:** 17 de octubre de 2025  
**Basado en:** Módulo Abecedario existente  
**Estado:** ✅ Funcional - Pendiente solo imágenes reales
