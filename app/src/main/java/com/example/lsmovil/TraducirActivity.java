package com.example.lsmovil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraducirActivity extends AppCompatActivity {

    private Toolbar toolbar;

    // Variables para CameraX
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    // Constante para la solicitud de permisos
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final String TAG = "CameraXApp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // El layout ya tiene el PreviewView
        setContentView(R.layout.activity_traducir);

        // Inicializar la vista previa de la cámara y el executor
        previewView = findViewById(R.id.viewFinder);
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Configuración del Toolbar
        setupToolbar();

        // Verificar permisos de la cámara
        if (allPermissionsGranted()) {
            startCamera(); // Si los permisos están concedidos, iniciar la cámara
        } else {
            // Si no, solicitar permisos
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

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // Obtener el proveedor de la cámara
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Configurar el caso de uso de la vista previa
                Preview preview = new Preview.Builder().build();

                // Seleccionar la cámara trasera por defecto
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                // Conectar la vista previa a la superficie de nuestro PreviewView
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Desvincular cualquier caso de uso anterior y vincular el nuevo
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);

            } catch (Exception e) {
                Log.e(TAG, "Error al iniciar la cámara", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // Método para verificar si los permisos necesarios han sido concedidos
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // Callback que se ejecuta después de que el usuario responde a la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera(); // Permisos concedidos, iniciar cámara
            } else {
                // Permisos denegados
                Toast.makeText(this, "Permisos de cámara denegados.", Toast.LENGTH_SHORT).show();
                finish(); // Cerrar la actividad si no se conceden los permisos
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Es importante apagar el executor para liberar recursos
        cameraExecutor.shutdown();
    }
}
