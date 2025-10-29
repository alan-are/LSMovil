package com.example.lsmovil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;

/**
 * Actividad para traducir señas LSM en tiempo real (modo landscape)
 * Usa la cámara en formato cuadrado y el modelo TensorFlow Lite para detectar señas
 * Layout optimizado: cámara cuadrada a la izquierda, detección a la derecha
 */
public class TraducirActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "TraducirActivity";
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final long DISPLAY_DURATION_MS = 2000; // 2 segundos para mostrar la detección
    
    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private LSMDetector lsmDetector;
    
    // UI Material Design 3 para mostrar detección
    private MaterialCardView detectionCard;
    private ImageView ivSignImage;
    private TextView tvSignLabel;
    private MaterialButton btnBack;
    private FloatingActionButton btnSwitchCamera;
    
    // Control de cámara
    private int currentCameraId = CameraBridgeViewBase.CAMERA_ID_BACK;
    
    // Control de visualización temporal
    private Handler hideHandler = new Handler(Looper.getMainLooper());
    private Runnable hideRunnable;
    private String lastShownSign = "";
    private long lastShowTime = 0;
    
    
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
        
        // Configurar pantalla (mantener encendida, sin cambiar a fullscreen)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.activity_traducir);
        
        // Verificar permiso de cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST
            );
        }
        
        // Inicializar vista de cámara
        mOpenCvCameraView = findViewById(R.id.camera_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(currentCameraId); // Configurar cámara inicial
        
        // UI Material Design 3 - Card de detección
        detectionCard = findViewById(R.id.detection_card);
        ivSignImage = findViewById(R.id.iv_sign_image);
        tvSignLabel = findViewById(R.id.tv_sign_label);
        
        // Inicialmente ocultar la card
        detectionCard.setVisibility(View.GONE);
        
        // Botón de regreso
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        
        // Botón para cambiar de cámara
        btnSwitchCamera = findViewById(R.id.btn_switch_camera);
        btnSwitchCamera.setOnClickListener(v -> switchCamera());
        
        // Inicializar detector LSM
        try {
            lsmDetector = new LSMDetector(
                getAssets(),
                "model.tflite",
                "labels.txt"
            );
            Log.d(TAG, "Detector LSM inicializado correctamente");
            Toast.makeText(this, "✨ Detector LSM listo", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Error al cargar modelo LSM: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar el modelo", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV inicializado correctamente");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d(TAG, "Inicializando OpenCV...");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }
    
    /**
     * Cambia entre cámara trasera y delantera
     */
    private void switchCamera() {
        if (mOpenCvCameraView != null) {
            // Deshabilitar la vista actual
            mOpenCvCameraView.disableView();
            
            // Cambiar el ID de la cámara
            if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_BACK) {
                currentCameraId = CameraBridgeViewBase.CAMERA_ID_FRONT;
                Toast.makeText(this, "📷 Cámara delantera", Toast.LENGTH_SHORT).show();
            } else {
                currentCameraId = CameraBridgeViewBase.CAMERA_ID_BACK;
                Toast.makeText(this, "📷 Cámara trasera", Toast.LENGTH_SHORT).show();
            }
            
            // Configurar el nuevo índice de cámara
            mOpenCvCameraView.setCameraIndex(currentCameraId);
            
            // Habilitar la vista con la nueva cámara
            mOpenCvCameraView.enableView();
            
            Log.d(TAG, "Cámara cambiada a: " + (currentCameraId == CameraBridgeViewBase.CAMERA_ID_FRONT ? "Frontal" : "Trasera"));
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        if (lsmDetector != null) {
            lsmDetector.close();
        }
        
        // Limpiar handlers
        if (hideRunnable != null) {
            hideHandler.removeCallbacks(hideRunnable);
        }
    }
    
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }
    
    @Override
    public void onCameraViewStopped() {
        if (mRgba != null) {
            mRgba.release();
        }
        if (mGray != null) {
            mGray.release();
        }
    }
    
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        
        // Si es cámara frontal, voltear horizontalmente para efecto espejo
        if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_FRONT) {
            Core.flip(mRgba, mRgba, 1); // 1 = flip horizontal (espejo)
        }
        
        // Procesar frame directamente (modo landscape con vista cuadrada)
        if (lsmDetector != null) {
            try {
                // Crear una copia del frame para procesamiento
                Mat frameToProcess = mRgba.clone();
                
                // El detector procesa el frame
                lsmDetector.recognizeImage(frameToProcess);
                
                // Actualizar UI en el hilo principal
                String detectedSign = lsmDetector.getLastDetectedSign();
                float confidence = lsmDetector.getLastConfidence();
                
                runOnUiThread(() -> {
                    updateDetectionUI(detectedSign, confidence);
                });
                
                // Liberar la copia
                frameToProcess.release();
            } catch (Exception e) {
                Log.e(TAG, "Error procesando frame: " + e.getMessage());
            }
        }
        
        // Retornar el frame (espejado si es frontal)
        return mRgba;
    }
    
    /**
     * Actualiza la UI con Material Design 3 para mostrar la detección
     * Muestra la imagen de la seña y un label con el nombre
     */
    private void updateDetectionUI(String sign, float confidence) {
        // Log para debug
        Log.d(TAG, "updateDetectionUI - Sign: '" + sign + "', Confidence: " + (confidence * 100) + "%");
        
        // Solo mostrar detecciones con confianza > 80% y no sea "Fondo"
        if (confidence > 0.9f && !sign.contains("Fondo")) {
            
            // Verificar si es una seña soportada y si no se mostró recientemente
            int imageRes = getSignImageResource(sign);
            long currentTime = System.currentTimeMillis();
            
            Log.d(TAG, "Image resource: " + imageRes + ", last shown: " + lastShownSign);
            
            if (imageRes != -1 && (!sign.equals(lastShownSign) || (currentTime - lastShowTime) > 3000)) {
                // Cancelar cualquier ocultamiento pendiente
                if (hideRunnable != null) {
                    hideHandler.removeCallbacks(hideRunnable);
                }
                
                // Actualizar la imagen
                ivSignImage.setImageResource(imageRes);
                
                // Actualizar el texto
                String label = getSignLabel(sign);
                tvSignLabel.setText(label);
                
                // Mostrar card con animación
                if (detectionCard.getVisibility() != View.VISIBLE) {
                    detectionCard.setVisibility(View.VISIBLE);
                    detectionCard.setAlpha(0f);
                    detectionCard.setScaleX(0.8f);
                    detectionCard.setScaleY(0.8f);
                    detectionCard.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start();
                }
                
                // Actualizar última seña mostrada
                lastShownSign = sign;
                lastShowTime = currentTime;
                
                Log.i(TAG, "✨ Mostrando seña: " + label);
                
                // Programar ocultamiento después de DISPLAY_DURATION_MS
                hideRunnable = () -> {
                    if (detectionCard.getVisibility() == View.VISIBLE) {
                        detectionCard.animate()
                            .alpha(0f)
                            .scaleX(0.8f)
                            .scaleY(0.8f)
                            .setDuration(300)
                            .withEndAction(() -> detectionCard.setVisibility(View.GONE))
                            .start();
                    }
                };
                hideHandler.postDelayed(hideRunnable, DISPLAY_DURATION_MS);
            }
        }
    }
    
    /**
     * Obtiene el recurso de imagen para una seña específica
     * @param sign Nombre de la seña (ej: "0 Letra A", "5 Numero 1")
     * @return ID del recurso drawable o -1 si no está soportada
     */
    private int getSignImageResource(String sign) {
        // Las etiquetas vienen en formato "0 Letra A", "5 Numero 1", etc.
        // Necesitamos extraer la letra o número
        String signUpper = sign.toUpperCase().trim();
        
        Log.d(TAG, "getSignImageResource - Input: '" + sign + "', Upper: '" + signUpper + "'");
        
        // Mapeo basado en el formato del labels.txt
        if (signUpper.contains("LETRA A")) {
            return R.drawable.ic_letra_a;
        } else if (signUpper.contains("LETRA E")) {
            return R.drawable.ic_letra_e;
        } else if (signUpper.contains("LETRA I")) {
            return R.drawable.ic_letra_i;
        } else if (signUpper.contains("LETRA O")) {
            return R.drawable.ic_letra_o;
        } else if (signUpper.contains("LETRA U")) {
            return R.drawable.ic_letra_u;
        } else if (signUpper.contains("NUMERO 1")) {
            return R.drawable.ic_num_1;
        } else if (signUpper.contains("NUMERO 2")) {
            return R.drawable.ic_num_2;
        } else if (signUpper.contains("NUMERO 3")) {
            return R.drawable.ic_num_3;
        }
        
        Log.w(TAG, "Seña no soportada: " + sign);
        return -1; // Seña no soportada
    }
    
    /**
     * Obtiene el label (texto) para mostrar según la seña
     * @param sign Nombre de la seña (ej: "0 Letra A", "5 Numero 1")
     * @return Texto formateado (ej: "Letra A", "Número 1")
     */
    private String getSignLabel(String sign) {
        // Extraer solo la parte después del número
        // Formato: "0 Letra A" -> "Letra A"
        // Formato: "5 Numero 1" -> "Número 1"
        String label = sign.trim();
        
        // Eliminar el número inicial y el espacio
        if (label.matches("^\\d+\\s+.*")) {
            label = label.replaceFirst("^\\d+\\s+", "");
        }
        
        // Reemplazar "Numero" por "Número" (con tilde)
        label = label.replace("Numero", "Número");
        
        Log.d(TAG, "getSignLabel - Input: '" + sign + "', Output: '" + label + "'");
        
        return label;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Log.d(TAG, "Permiso de cámara concedido");
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso de cámara requerido", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
