package com.example.lsmovil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Actividad para traducir señas LSM en tiempo real (modo landscape con gamificación)
 * Usa la cámara y el modelo TensorFlow Lite para detectar señas
 */
public class TraducirActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "TraducirActivity";
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    
    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private LSMDetector lsmDetector;
    
    // UI Material Design 3
    private MaterialCardView detectionCard;
    private TextView tvDetectedSign;
    private TextView tvConfidence;
    private LinearProgressIndicator progressConfidence;
    private MaterialButton btnBack;
    
    // UI Gamificación
    private TextView tvDetectionsCount;
    private TextView tvStreakCount;
    private TextView tvScore;
    private TextView tvAvgConfidence;
    private LinearLayout historyContainer;
    private TextView tvEmptyHistory;
    
    // Sistema de gamificación
    private int totalDetections = 0;
    private int currentStreak = 0;
    private int maxStreak = 0;
    private int totalScore = 0;
    private float totalConfidenceSum = 0f;
    private String lastDetectedSign = "";
    private long lastDetectionTime = 0;
    private static final long STREAK_TIMEOUT_MS = 3000; // 3 segundos para mantener racha
    private Set<String> uniqueSignsDetected = new HashSet<>();
    
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
        
        // Configurar pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
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
        
        // UI Material Design 3 - Detección
        detectionCard = findViewById(R.id.detection_card);
        tvDetectedSign = findViewById(R.id.tv_detected_sign);
        tvConfidence = findViewById(R.id.tv_confidence);
        progressConfidence = findViewById(R.id.progress_confidence);
        
        // UI Gamificación
        tvDetectionsCount = findViewById(R.id.tv_detections_count);
        tvStreakCount = findViewById(R.id.tv_streak_count);
        tvScore = findViewById(R.id.tv_score);
        tvAvgConfidence = findViewById(R.id.tv_avg_confidence);
        historyContainer = findViewById(R.id.history_container);
        tvEmptyHistory = findViewById(R.id.tv_empty_history);
        
        // Inicialmente ocultar la card
        detectionCard.setVisibility(View.GONE);
        
        // Botón de regreso
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            showSessionSummary();
            finish();
        });
        
        // Inicializar detector LSM
        try {
            lsmDetector = new LSMDetector(
                getAssets(),
                "model.tflite",
                "labels.txt"
            );
            Log.d(TAG, "Detector LSM inicializado correctamente");
            Toast.makeText(this, "✨ Detector LSM listo - ¡Modo Landscape!", Toast.LENGTH_SHORT).show();
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
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        if (lsmDetector != null) {
            lsmDetector.close();
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
        
        // IMPORTANTE: Rotar frame 90° para landscape (el modelo funciona mejor así)
        Mat rotatedFrame = new Mat();
        Core.rotate(mRgba, rotatedFrame, Core.ROTATE_90_CLOCKWISE);
        
        // Procesar frame rotado con el detector LSM
        if (lsmDetector != null) {
            try {
                // Crear una copia del frame rotado para procesamiento
                Mat frameToProcess = rotatedFrame.clone();
                
                // El detector procesa el frame
                lsmDetector.recognizeImage(frameToProcess);
                
                // Actualizar UI en el hilo principal
                String detectedSign = lsmDetector.getLastDetectedSign();
                float confidence = lsmDetector.getLastConfidence();
                
                runOnUiThread(() -> {
                    updateDetectionUI(detectedSign, confidence);
                    updateGamificationStats(detectedSign, confidence);
                });
                
                // Liberar la copia
                frameToProcess.release();
            } catch (Exception e) {
                Log.e(TAG, "Error procesando frame: " + e.getMessage());
            }
        }
        
        // Liberar frame rotado
        rotatedFrame.release();
        
        // Retornar el frame original sin modificar (para evitar crash)
        return mRgba;
    }
    
    /**
     * Actualiza la UI con Material Design 3 para mostrar la detección
     */
    private void updateDetectionUI(String sign, float confidence) {
        // Solo mostrar detecciones con confianza > 50% y no sea "Fondo"
        if (confidence > 0.5f && !sign.equalsIgnoreCase("Fondo")) {
            // Mostrar card con animación suave
            if (detectionCard.getVisibility() != View.VISIBLE) {
                detectionCard.setVisibility(View.VISIBLE);
                detectionCard.setAlpha(0f);
                detectionCard.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start();
            }
            
            // Actualizar texto de la seña detectada
            tvDetectedSign.setText(sign);
            
            // Actualizar porcentaje de confianza
            int confidencePercent = (int) (confidence * 100);
            tvConfidence.setText(confidencePercent + "%");
            
            // Actualizar progress bar
            progressConfidence.setProgress(confidencePercent);
            
            // Cambiar color según confianza
            int colorRes;
            if (confidence > 0.8f) {
                // Verde - Excelente
                colorRes = com.google.android.material.R.attr.colorTertiary;
            } else if (confidence > 0.65f) {
                // Amarillo - Bueno (usar secondary)
                colorRes = com.google.android.material.R.attr.colorSecondary;
            } else {
                // Naranja - Aceptable (usar error)
                colorRes = com.google.android.material.R.attr.colorError;
            }
            
            // Aplicar color al indicador
            progressConfidence.setIndicatorColor(getColorFromAttr(colorRes));
            
        } else {
            // Ocultar card con animación
            if (detectionCard.getVisibility() == View.VISIBLE) {
                detectionCard.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> detectionCard.setVisibility(View.GONE))
                    .start();
            }
        }
    }
    
    /**
     * Sistema de gamificación: actualiza estadísticas y puntuación
     */
    private void updateGamificationStats(String sign, float confidence) {
        // Solo contar detecciones válidas (no "Fondo" y confianza > 60%)
        if (sign.equalsIgnoreCase("Fondo") || confidence < 0.6f) {
            // Resetear racha si pasa el timeout
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDetectionTime > STREAK_TIMEOUT_MS) {
                currentStreak = 0;
                tvStreakCount.setText("0");
            }
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        
        // Verificar si es una detección nueva (no repetir la misma seña consecutivamente)
        if (!sign.equals(lastDetectedSign) || (currentTime - lastDetectionTime) > 1000) {
            
            // Incrementar contador total
            totalDetections++;
            tvDetectionsCount.setText(String.valueOf(totalDetections));
            
            // Actualizar racha
            if (currentTime - lastDetectionTime <= STREAK_TIMEOUT_MS && totalDetections > 1) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }
            
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak;
            }
            
            tvStreakCount.setText(String.valueOf(currentStreak));
            
            // Calcular puntuación (confianza * multiplicador de racha)
            int streakMultiplier = Math.min(currentStreak, 10); // Max 10x
            int points = (int) (confidence * 100 * streakMultiplier);
            totalScore += points;
            tvScore.setText(String.valueOf(totalScore));
            
            // Actualizar promedio de confianza
            totalConfidenceSum += confidence;
            float avgConfidence = totalConfidenceSum / totalDetections;
            tvAvgConfidence.setText(String.format("%d%%", (int)(avgConfidence * 100)));
            
            // Agregar a historial si es una seña única nueva
            if (uniqueSignsDetected.add(sign)) {
                addSignToHistory(sign, confidence);
            }
            
            // Actualizar última detección
            lastDetectedSign = sign;
            lastDetectionTime = currentTime;
            
            // Animación de celebración para rachas altas
            if (currentStreak % 5 == 0 && currentStreak > 0) {
                showStreakCelebration(currentStreak);
            }
        }
    }
    
    /**
     * Agrega una seña al historial visual
     */
    private void addSignToHistory(String sign, float confidence) {
        // Ocultar mensaje de historial vacío
        if (tvEmptyHistory != null) {
            tvEmptyHistory.setVisibility(View.GONE);
        }
        
        // Crear chip de seña detectada
        TextView signChip = new TextView(this);
        signChip.setText(sign + " ✓");
        signChip.setTextSize(14);
        signChip.setTextColor(getColorFromAttr(com.google.android.material.R.attr.colorOnSecondaryContainer));
        signChip.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        signChip.setPadding(24, 12, 24, 12);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 8);
        signChip.setLayoutParams(params);
        
        // Agregar al inicio del historial
        historyContainer.addView(signChip, 0);
    }
    
    /**
     * Muestra una celebración por racha alta
     */
    private void showStreakCelebration(int streak) {
        String message = "";
        if (streak == 5) {
            message = "🔥 ¡Racha de 5! ¡Sigue así!";
        } else if (streak == 10) {
            message = "⚡ ¡RACHA DE 10! ¡INCREÍBLE!";
        } else if (streak >= 15) {
            message = "🌟 ¡ERES UN MAESTRO LSM! 🌟";
        }
        
        if (!message.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Muestra un resumen de la sesión al salir
     */
    private void showSessionSummary() {
        if (totalDetections > 0) {
            float avgConfidence = totalConfidenceSum / totalDetections;
            String summary = String.format(
                "📊 Resumen de Sesión\n\n" +
                "✅ Señas detectadas: %d\n" +
                "🔥 Racha máxima: %d\n" +
                "⭐ Puntuación total: %d\n" +
                "📈 Precisión promedio: %d%%\n" +
                "🎯 Señas únicas: %d",
                totalDetections,
                maxStreak,
                totalScore,
                (int)(avgConfidence * 100),
                uniqueSignsDetected.size()
            );
            
            Toast.makeText(this, summary, Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Obtiene un color desde un atributo del tema
     */
    private int getColorFromAttr(int attrColor) {
        android.util.TypedValue typedValue = new android.util.TypedValue();
        getTheme().resolveAttribute(attrColor, typedValue, true);
        return typedValue.data;
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
