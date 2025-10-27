package com.example.lsmovil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;

// ¡¡CORRECCIÓN 2.1: IMPORTACIÓN AÑADIDA!!
import org.tensorflow.lite.task.vision.classifier.Classifications;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraducirActivity extends AppCompatActivity {

    private Toolbar toolbar;

    // --- Variables de CameraX ---
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    // --- Variables de TensorFlow Lite ---
    private TextView textView2;
    private ImageClassifier imageClassifier;
    private static final String MODEL_FILE = "model.tflite";
    // Dejamos estos como valores predeterminados
    private static int modelInputWidth = 224;
    private static int modelInputHeight = 224;


    // Constantes de permisos
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final String TAG = "CameraXApp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traducir);

        previewView = findViewById(R.id.viewFinder);
        textView2 = findViewById(R.id.textView2);
        textView2.setText("");

        cameraExecutor = Executors.newSingleThreadExecutor();
        setupToolbar();

        try {
            initTensorFlow();
        } catch (IOException e) {
            Log.e(TAG, "Error al inicializar TensorFlow Lite", e);
            Toast.makeText(this, "Error al cargar el modelo.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbartrad);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Traducir");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * ARREGLO 2 (MODIFICADO):
     * Ahora solo inicializa el clasificador y obtiene las dimensiones.
     * El ImageProcessor ya no se crea aquí.
     */
    private void initTensorFlow() throws IOException {
        ImageClassifier.ImageClassifierOptions options = ImageClassifier.ImageClassifierOptions.builder()
                .setNumThreads(1)
                .setMaxResults(1)
                .setScoreThreshold(0.7f)
                .build();

        imageClassifier = ImageClassifier.createFromFileAndOptions(
                this,
                MODEL_FILE,
                options
        );

        // ¡¡CORRECCIÓN 1: LÍNEAS ELIMINADAS!!
        // Se eliminaron las líneas que usaban 'getInputTensorShape()'
        // ya que no existe en esta versión de ImageClassifier de la Task Library.
        // Usaremos los valores 224x224 definidos como miembros de la clase.
        Log.d(TAG, "Modelo TFLite cargado. Espera entrada de: " + modelInputWidth + "x" + modelInputHeight);
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(modelInputWidth, modelInputHeight)) // Intenta que coincida
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                // 3. Asignar el analizador
                imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
                    int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                    Bitmap bitmap = toBitmap(imageProxy);

                    if (bitmap != null) {
                        TensorImage tensorImage = TensorImage.fromBitmap(bitmap);

                        // ARREGLO 2 (LA SOLUCIÓN):
                        // Creamos el procesador COMPLETO aquí, en el orden correcto.
                        ImageProcessor fullProcessor = new ImageProcessor.Builder()
                                .add(new Rot90Op(rotationDegrees / 90)) // 1. Rotar
                                .add(new ResizeOp(modelInputHeight, modelInputWidth, ResizeOp.ResizeMethod.BILINEAR)) // 2. Cambiar tamaño
                                .add(new NormalizeOp(127.5f, 127.5f)) // 3. Normalizar
                                .build();

                        // Procesamos la imagen con la cadena completa
                        tensorImage = fullProcessor.process(tensorImage);

                        // ¡¡CORRECCIÓN 2.2: TIPO DE 'results' CAMBIADO!!
                        // El método 'classify' devuelve List<Classifications>, no List<Category>
                        List<Classifications> results = imageClassifier.classify(tensorImage);

                        // ¡¡CORRECCIÓN 2.3: OBTENER CATEGORÍAS DESDE 'Classifications'!!
                        // Necesitamos un paso extra para extraer la lista de categorías
                        if (results != null && !results.isEmpty()) {

                            // Obtenemos el primer (y único) objeto de clasificación
                            Classifications classifications = results.get(0);

                            // De ESE objeto, obtenemos la lista de categorías
                            if (classifications.getCategories() != null && !classifications.getCategories().isEmpty()) {

                                Category topResult = classifications.getCategories().get(0);
                                String label = topResult.getLabel();
                                // float score = topResult.getScore(); // Descomenta si quieres ver la confianza

                                // Actualizamos el UI en el hilo principal
                                runOnUiThread(() -> {
                                    textView2.setText(label);
                                });
                            }
                        }
                    }
                    imageProxy.close();
                });

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        (LifecycleOwner) this,
                        cameraSelector,
                        preview,
                        imageAnalysis
                );

            } catch (Exception e) {
                Log.e(TAG, "Error al iniciar la cámara", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }


    /**
     * Convierte un ImageProxy a un Bitmap.
     * Ahora debería funcionar porque ImageProxy será importado (ARREGLO 1).
     */
    @SuppressLint("UnsafeOptInUsageError")
    private Bitmap toBitmap(@NonNull ImageProxy image) {
        // Esta implementación puede ser problemática si el PreviewView no está
        // dibujando exactamente lo mismo que el ImageAnalysis.
        // Una implementación más robusta usaría un conversor YUV_to_RGB.
        // Pero si 'previewView.getBitmap()' funciona para ti, está bien por ahora.
        return previewView.getBitmap();
    }


    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permisos de cámara denegados.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        if (imageClassifier != null) {
            imageClassifier.close();
        }
    }
}