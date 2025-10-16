package com.example.lsmovil;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.AppCompatButton;

public class AcercaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca);

        Button btnVolver = findViewById(R.id.backButtton);

        btnVolver.setOnClickListener(v -> {
            finish(); // Regresa a la actividad anterior (Principal)
        });
    }
}

