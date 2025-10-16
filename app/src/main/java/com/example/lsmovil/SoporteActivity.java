package com.example.lsmovil;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SoporteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        Button btnVolver = findViewById(R.id.backButton);

        btnVolver.setOnClickListener(v -> {
            finish(); // Regresa a la actividad anterior (Principal)
        });
    }
}
