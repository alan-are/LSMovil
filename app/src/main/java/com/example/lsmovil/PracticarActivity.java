package com.example.lsmovil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.IOException;

/**
 * Actividad para practicar señas LSM con validación en tiempo real
 * El usuario debe repetir la seña correcta 3 veces
 * Cada repetición se valida durante 5 segundos con confianza >90%
 */
public class PracticarActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "PracticarActivity";
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int REPETICIONES_REQUERIDAS = 3;
    private static final long DURACION_VALIDACION_MS = 3000; // 3 segundos
    private static final float CONFIANZA_MINIMA = 0.90f; // 90%
    
    // OpenCV y detector LSM
    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private LSMDetector lsmDetector;
    
    // UI Elements
    private MaterialCardView cardObjetivo;
    private ImageView ivObjetivo;
    private TextView tvObjetivo;
    private TextView tvDescripcion;
    private MaterialButton btnVolver;
    private MaterialCardView cardProgreso;
    private TextView tvProgreso;
    private ProgressBar progressBarTiempo;
    private TextView tvFeedback;
    private ImageView ivFeedback;
    
    // Datos de la seña a practicar
    private String tipo; // "letra" o "numero"
    private String valor; // "A", "B", "1", "2", etc.
    private int imagenResId;
    private String descripcion;
    private String labelEsperado; // El label del modelo TFLite que debemos detectar
    
    // Control de práctica
    private int repeticionesCompletadas = 0;
    private boolean validando = false;
    private long tiempoInicioValidacion = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable validacionRunnable;
    
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV cargado correctamente");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configurar pantalla (mantener encendida)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.activity_practicar);
        
        // Obtener datos del Intent
        Intent intent = getIntent();
        tipo = intent.getStringExtra("tipo");
        valor = intent.getStringExtra("valor");
        imagenResId = intent.getIntExtra("imagen", 0);
        descripcion = intent.getStringExtra("descripcion");
        
        // Generar el label esperado según el tipo y valor
        labelEsperado = generarLabelEsperado(tipo, valor);
        
        Log.d(TAG, "Practicar - Tipo: " + tipo + ", Valor: " + valor + ", Label esperado: " + labelEsperado);
        
        // Inicializar UI
        initUI();
        
        // Verificar permiso de cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST
            );
        } else {
            iniciarCamara();
        }
    }
    
    /**
     * Genera el label esperado del modelo TFLite según el tipo y valor
     */
    private String generarLabelEsperado(String tipo, String valor) {
        // Según labels.txt:
        // 0 Letra A, 1 Letra E, 2 Letra I, 3 Letra O, 4 Letra U
        // 5 Numero 1, 6 Numero 2, 7 Numero 3
        
        if ("letra".equals(tipo)) {
            // Solo tenemos A, E, I, O, U en el modelo
            switch (valor.toUpperCase()) {
                case "A": return "0 Letra A";
                case "E": return "1 Letra E";
                case "I": return "2 Letra I";
                case "O": return "3 Letra O";
                case "U": return "4 Letra U";
                default:
                    Log.w(TAG, "Letra " + valor + " no está en el modelo TFLite actual");
                    return null;
            }
        } else if ("numero".equals(tipo)) {
            // Solo tenemos 1, 2, 3 en el modelo
            switch (valor) {
                case "1": return "5 Numero 1";
                case "2": return "6 Numero 2";
                case "3": return "7 Numero 3";
                default:
                    Log.w(TAG, "Número " + valor + " no está en el modelo TFLite actual");
                    return null;
            }
        }
        
        return null;
    }
    
    private void initUI() {
        // Referencias a las vistas
        cardObjetivo = findViewById(R.id.cardObjetivo);
        ivObjetivo = findViewById(R.id.ivObjetivo);
        tvObjetivo = findViewById(R.id.tvObjetivo);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        btnVolver = findViewById(R.id.btnVolver);
        cardProgreso = findViewById(R.id.cardProgreso);
        tvProgreso = findViewById(R.id.tvProgreso);
        progressBarTiempo = findViewById(R.id.progressBarTiempo);
        tvFeedback = findViewById(R.id.tvFeedback);
        ivFeedback = findViewById(R.id.ivFeedback);
        
        // Configurar datos del objetivo
        tvObjetivo.setText(tipo.equals("letra") ? "Letra " + valor : "Número " + valor);
        ivObjetivo.setImageResource(imagenResId);
        tvDescripcion.setText(descripcion);
        
        // Actualizar progreso inicial
        actualizarProgreso();
        
        // Botón volver
        btnVolver.setOnClickListener(v -> finish());
        
        // Verificar si la seña está disponible en el modelo
        if (labelEsperado == null) {
            mostrarMensaje("Esta seña aún no está disponible para práctica", true);
            tvFeedback.setText("Seña no disponible en el modelo actual");
            tvFeedback.setTextColor(Color.parseColor("#F44336")); // Rojo
            ivFeedback.setVisibility(View.VISIBLE);
            ivFeedback.setImageResource(android.R.drawable.ic_dialog_alert);
            
            // Deshabilitar la cámara para señas no disponibles
            return;
        }
        
        // Mostrar instrucciones iniciales
        tvFeedback.setText("Realiza la seña y mantenla durante 5 segundos");
        tvFeedback.setTextColor(Color.parseColor("#2196F3")); // Azul
        ivFeedback.setVisibility(View.GONE);
    }
    
    private void iniciarCamara() {
        // Inicializar OpenCV Camera View
        mOpenCvCameraView = findViewById(R.id.camera_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        
        try {
            // Inicializar detector LSM con el modelo TFLite
            lsmDetector = new LSMDetector(
                getAssets(),
                "model.tflite",
                "labels.txt"
            );
            Log.d(TAG, "LSMDetector inicializado correctamente");
        } catch (IOException e) {
            Log.e(TAG, "Error al inicializar LSMDetector: " + e.getMessage());
            mostrarMensaje("Error al cargar el modelo de detección", true);
            finish();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarCamara();
            } else {
                mostrarMensaje("Se necesita permiso de cámara para practicar", true);
                finish();
            }
        }
    }
    
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGray = new Mat();
    }
    
    @Override
    public void onCameraViewStopped() {
        if (mRgba != null) mRgba.release();
        if (mGray != null) mGray.release();
    }
    
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        
        // Si la seña no está disponible o ya completamos todas las repeticiones, no detectar
        if (labelEsperado == null || repeticionesCompletadas >= REPETICIONES_REQUERIDAS) {
            return mRgba;
        }
        
        // Ejecutar detección
        lsmDetector.recognizeImage(mRgba);
        
        // Obtener resultado
        String deteccion = lsmDetector.getLastDetectedSign();
        float confianza = lsmDetector.getLastConfidence();
        
        // Procesar validación en el hilo principal
        runOnUiThread(() -> procesarDeteccion(deteccion, confianza));
        
        return mRgba;
    }
    
    /**
     * Procesa la detección y maneja la validación de 3 segundos
     */
    private void procesarDeteccion(String deteccion, float confianza) {
        // Verificar si la detección coincide con lo esperado
        boolean esCorrecta = deteccion.equals(labelEsperado) && confianza >= CONFIANZA_MINIMA;
        
        if (esCorrecta && !validando) {
            // Iniciar validación
            iniciarValidacion();
        } else if (!esCorrecta && validando) {
            // Cancelar validación si se pierde la seña
            cancelarValidacion();
        } else if (esCorrecta && validando) {
            // Actualizar progreso de validación
            actualizarValidacion();
        }
        
        // Actualizar feedback visual
        if (!validando) {
            if (esCorrecta) {
                tvFeedback.setText("¡Correcto! Mantén la seña...");
                tvFeedback.setTextColor(Color.parseColor("#4CAF50")); // Verde
            } else {
                String msg = deteccion.equals("8 Fondo") 
                    ? "Realiza la seña " + valor
                    : "Detectado: " + obtenerNombreSeña(deteccion) + " (" + String.format("%.0f%%", confianza * 100) + ")";
                tvFeedback.setText(msg);
                tvFeedback.setTextColor(Color.parseColor("#FF9800")); // Naranja
            }
            ivFeedback.setVisibility(View.GONE);
        }
    }
    
    /**
     * Inicia la validación de 3 segundos
     */
    private void iniciarValidacion() {
        validando = true;
        tiempoInicioValidacion = System.currentTimeMillis();
        progressBarTiempo.setMax(100);
        progressBarTiempo.setProgress(0);
        progressBarTiempo.setVisibility(View.VISIBLE);
        
        tvFeedback.setText("¡Mantén la seña! (" + DURACION_VALIDACION_MS / 1000 + " segundos)");
        tvFeedback.setTextColor(Color.parseColor("#2196F3")); // Azul
        
        // Programar verificación al final de los 3 segundos
        validacionRunnable = () -> {
            if (validando) {
                repeticionCompletada();
            }
        };
        
        handler.postDelayed(validacionRunnable, DURACION_VALIDACION_MS);
    }
    
    /**
     * Actualiza el progreso de la validación
     */
    private void actualizarValidacion() {
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioValidacion;
        int progreso = (int) ((tiempoTranscurrido * 100) / DURACION_VALIDACION_MS);
        progressBarTiempo.setProgress(Math.min(progreso, 100));
        
        long segundosRestantes = (DURACION_VALIDACION_MS - tiempoTranscurrido) / 1000;
        tvFeedback.setText("️¡Mantén la seña! (" + (segundosRestantes + 1) + " segundos)");
    }
    
    /**
     * Cancela la validación si se pierde la seña
     */
    private void cancelarValidacion() {
        validando = false;
        handler.removeCallbacks(validacionRunnable);
        progressBarTiempo.setVisibility(View.GONE);
        
        tvFeedback.setText("Seña perdida. Inténtalo de nuevo");
        tvFeedback.setTextColor(Color.parseColor("#FF9800")); // Naranja
    }
    
    /**
     * Marca una repetición como completada
     */
    private void repeticionCompletada() {
        validando = false;
        handler.removeCallbacks(validacionRunnable);
        progressBarTiempo.setVisibility(View.GONE);
        
        repeticionesCompletadas++;
        actualizarProgreso();
        
        if (repeticionesCompletadas >= REPETICIONES_REQUERIDAS) {
            practicaCompletada();
        } else {
            tvFeedback.setText("¡Excelente! Repetición " + repeticionesCompletadas + " completada");
            tvFeedback.setTextColor(Color.parseColor("#4CAF50")); // Verde
            ivFeedback.setVisibility(View.VISIBLE);
            ivFeedback.setImageResource(android.R.drawable.btn_star);
            ivFeedback.setColorFilter(Color.parseColor("#e6d92a"));

            
            // Ocultar el check después de 2 segundos
            handler.postDelayed(() -> ivFeedback.setVisibility(View.GONE), 2000);
        }
    }
    
    /**
     * Se ejecuta cuando se completan las 3 repeticiones
     */
    private void practicaCompletada() {
        tvFeedback.setText("⭐⭐⭐¡Felicidades! Has completado la práctica");
        tvFeedback.setTextColor(Color.parseColor("#4CAF50")); // Verde


        mostrarMensaje("¡Práctica completada exitosamente!", false);
        
        // Cerrar la activity después de 3 segundos
        handler.postDelayed(() -> finish(), 5000);
    }
    
    /**
     * Actualiza el indicador de progreso
     */
    private void actualizarProgreso() {
        tvProgreso.setText("Progreso: " + repeticionesCompletadas + " / " + REPETICIONES_REQUERIDAS);
    }
    
    /**
     * Obtiene el nombre legible de una seña
     */
    private String obtenerNombreSeña(String label) {
        // Remover el número inicial del label
        if (label.contains(" ")) {
            return label.substring(label.indexOf(" ") + 1);
        }
        return label;
    }
    
    private void mostrarMensaje(String mensaje, boolean largo) {
        Toast.makeText(this, mensaje, largo ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Error al inicializar OpenCV");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV ya inicializado");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        
        if (lsmDetector != null) {
            lsmDetector.close();
        }
        
        handler.removeCallbacksAndMessages(null);
    }
}
