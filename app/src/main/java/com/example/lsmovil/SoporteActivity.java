package com.example.lsmovil;

import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SoporteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_soporte);

        Button btnVolver = findViewById(R.id.backButton);

        btnVolver.setOnClickListener(v -> {
            finish(); // Regresa a la actividad anterior (Principal)
        });
    }
}
