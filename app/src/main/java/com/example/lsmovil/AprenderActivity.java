package com.example.lsmovil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class AprenderActivity extends AppCompatActivity {

    private MaterialCardView cardAbecedario;
    private MaterialCardView cardNumeros;
    private MaterialCardView cardPalabras;
    private Toolbar toolbar;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar edge-to-edge según guías de Material Design
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_aprender);
        hideKeyboard(AprenderActivity.this);

        // Aplicar WindowInsets para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // El padding se maneja en el layout XML
            return insets;
        });

        // Configurar toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Habilitar botón de regreso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Aprender");
        }

        // Configurar listener para el botón de regreso
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inicializar las cards
        cardAbecedario = findViewById(R.id.cardAbecedario);
        cardNumeros = findViewById(R.id.cardNumeros);
        cardPalabras = findViewById(R.id.cardPalabras);

        // Configurar click listeners para cada módulo
        cardAbecedario.setOnClickListener(v -> {
            // Navegar a módulo Abecedario
            startActivity(new Intent(AprenderActivity.this, AbecedarioActivity.class));
        });

        cardNumeros.setOnClickListener(v -> {
            // Navegar a módulo Números
            startActivity(new Intent(AprenderActivity.this, NumerosActivity.class));
        });

        cardPalabras.setOnClickListener(v -> {
            // TODO: Implementar navegación a módulo Palabras Comunes
            Toast.makeText(AprenderActivity.this, 
                "Módulo Palabras Comunes - Próximamente", 
                Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
