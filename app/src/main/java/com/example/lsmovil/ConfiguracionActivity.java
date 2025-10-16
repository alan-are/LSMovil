package com.example.lsmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracionActivity extends AppCompatActivity {
    private Button btnCerrarSesion, btnVolverPrincipal, btnAyuda, btnEliminarCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Bind views
        btnCerrarSesion = findViewById(R.id.btnCerrarSesionConfig);
        btnVolverPrincipal = findViewById(R.id.backButton);
        btnAyuda = findViewById(R.id.btnAyuda);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Inicio.class));
            finish();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });

        // Eliminar cuenta
        btnEliminarCuenta.setOnClickListener(v -> {
            FirebaseAuth.getInstance().getCurrentUser().delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, Inicio.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Error al eliminar cuenta", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Volver al principal
        btnVolverPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver directamente a la actividad anterior
                onBackPressed(); // Llama al método que maneja el retroceso
            }
        });

        // Ir a SoporteActivity
        btnAyuda.setOnClickListener(v -> {
            startActivity(new Intent(this, SoporteActivity.class));
        });
    }
}
