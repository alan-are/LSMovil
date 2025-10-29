package com.example.lsmovil;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Detector de señas LSM usando TensorFlow Lite
 * Adaptado para clasificación de imágenes (Teachable Machine)
 */
public class LSMDetector {
    private static final String TAG = "LSMDetector";
    
    // Configuración del modelo
    private Interpreter interpreter;
    private List<String> labelList;
    private int INPUT_SIZE = 224; // Tamaño por defecto de Teachable Machine
    
    // Para almacenar el último resultado
    private String lastDetectedSign = "";
    private float lastConfidence = 0.0f;
    
    /**
     * Constructor del detector LSM
     * @param assetManager AssetManager de la aplicación
     * @param modelPath Ruta del modelo en assets (ej: "model.tflite")
     * @param labelPath Ruta de las etiquetas en assets (ej: "labels.txt")
     */
    public LSMDetector(AssetManager assetManager, String modelPath, String labelPath) throws IOException {
        Log.d(TAG, "Inicializando LSMDetector...");
        
        // Configurar opciones del intérprete
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4); // Usar 4 hilos de CPU
        
        // Cargar modelo
        interpreter = new Interpreter(loadModelFile(assetManager, modelPath), options);
        Log.d(TAG, "Modelo cargado exitosamente");
        
        // Cargar etiquetas
        labelList = loadLabelList(assetManager, labelPath);
        Log.d(TAG, "Etiquetas cargadas: " + labelList.size() + " clases");
    }
    
    /**
     * Carga el archivo del modelo desde assets
     */
    private ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    
    /**
     * Carga la lista de etiquetas desde assets
     */
    private List<String> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        String line;
        
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        
        reader.close();
        return labelList;
    }
    
    /**
     * Reconoce la seña en el frame de la cámara
     * @param mat_image Frame de OpenCV en formato Mat
     * @return Mat sin modificaciones (solo procesa la detección)
     */
    public Mat recognizeImage(Mat mat_image) {
        try {
            // Convertir Mat a Bitmap
            Bitmap bitmap = Bitmap.createBitmap(
                mat_image.cols(),
                mat_image.rows(),
                Bitmap.Config.ARGB_8888
            );
            Utils.matToBitmap(mat_image, bitmap);
            
            // IMPORTANTE: Escalar a tamaño de entrada del modelo (224x224) ANTES de convertir a ByteBuffer
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
            
            // Liberar el bitmap original para ahorrar memoria
            if (bitmap != scaledBitmap) {
                bitmap.recycle();
            }
            
            // Convertir bitmap escalado a ByteBuffer
            ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);
            
            // Preparar salida para modelo cuantizado (UINT8)
            // El modelo devuelve bytes (0-255) que representan probabilidades
            byte[][] output = new byte[1][labelList.size()];
            
            // Ejecutar inferencia
            interpreter.run(byteBuffer, output);
            
            // Encontrar la clase con mayor probabilidad
            // Convertir de byte (0-255) a float (0.0-1.0)
            int maxIndex = 0;
            float maxConfidence = (output[0][0] & 0xFF) / 255.0f;
            
            for (int i = 1; i < labelList.size(); i++) {
                float confidence = (output[0][i] & 0xFF) / 255.0f;
                if (confidence > maxConfidence) {
                    maxConfidence = confidence;
                    maxIndex = i;
                }
            }
            
            // Guardar resultado
            lastDetectedSign = labelList.get(maxIndex);
            lastConfidence = maxConfidence;
            
            // Log para debugging
            Log.d(TAG, "Detección: " + lastDetectedSign + " - Confianza: " + String.format("%.2f", maxConfidence * 100) + "%");
            
            // Liberar el bitmap escalado
            scaledBitmap.recycle();
            
        } catch (Exception e) {
            Log.e(TAG, "Error en reconocimiento: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Retornar frame sin modificaciones (la UI se maneja en la Activity)
        return mat_image;
    }
    
    /**
     * Convierte un Bitmap a ByteBuffer para entrada del modelo
     * El modelo cuantizado usa bytes (uint8) en vez de floats
     * IMPORTANTE: El bitmap DEBE ser 224x224 antes de llamar a este método
     */
    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        // Verificar que el bitmap sea del tamaño correcto
        if (bitmap.getWidth() != INPUT_SIZE || bitmap.getHeight() != INPUT_SIZE) {
            Log.e(TAG, "ERROR: Bitmap debe ser " + INPUT_SIZE + "x" + INPUT_SIZE + 
                       ", pero es " + bitmap.getWidth() + "x" + bitmap.getHeight());
            throw new IllegalArgumentException("Bitmap debe ser escalado a " + INPUT_SIZE + "x" + INPUT_SIZE + " primero");
        }
        
        // Modelo cuantizado: 1 byte por canal (no 4 bytes de float)
        // Total: 224 * 224 * 3 = 150,528 bytes
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(INPUT_SIZE * INPUT_SIZE * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        
        int[] intValues = new int[INPUT_SIZE * INPUT_SIZE];
        // CRÍTICO: Usar INPUT_SIZE explícitamente, NO bitmap.getWidth()/getHeight()
        bitmap.getPixels(intValues, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE);
        
        int pixel = 0;
        for (int i = 0; i < INPUT_SIZE; ++i) {
            for (int j = 0; j < INPUT_SIZE; ++j) {
                final int val = intValues[pixel++];
                
                // Extraer RGB como bytes (0-255) para modelo cuantizado
                byteBuffer.put((byte) ((val >> 16) & 0xFF)); // R
                byteBuffer.put((byte) ((val >> 8) & 0xFF));  // G
                byteBuffer.put((byte) (val & 0xFF));         // B
            }
        }
        
        Log.d(TAG, "ByteBuffer creado: " + byteBuffer.capacity() + " bytes (" + INPUT_SIZE + "x" + INPUT_SIZE + "x3 canales uint8)");
        return byteBuffer;
    }
    
    /**
     * Obtiene la última seña detectada
     */
    public String getLastDetectedSign() {
        return lastDetectedSign;
    }
    
    /**
     * Obtiene la confianza de la última detección
     */
    public float getLastConfidence() {
        return lastConfidence;
    }
    
    /**
     * Libera recursos
     */
    public void close() {
        if (interpreter != null) {
            interpreter.close();
            interpreter = null;
        }
    }
}
