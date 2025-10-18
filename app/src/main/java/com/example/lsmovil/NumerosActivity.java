package com.example.lsmovil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NumerosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NumeroAdapter adapter;
    private List<Numero> numeros;
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

        setContentView(R.layout.activity_numeros);
        hideKeyboard(NumerosActivity.this);

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
            getSupportActionBar().setTitle("Números");
        }

        // Configurar listener para el botón de regreso
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNumeros);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columnas

        // Cargar datos de los números
        cargarNumeros();

        // Configurar adapter
        adapter = new NumeroAdapter(this, numeros);
        recyclerView.setAdapter(adapter);
    }

    private void cargarNumeros() {
        numeros = new ArrayList<>();

        numeros.add(new Numero("0", 
            "Se forma un círculo con el dedo índice y el pulgar, mientras los otros dedos permanecen cerrados.", 
            R.drawable.ic_num_0));

        numeros.add(new Numero("1", 
            "Se estira el índice con la palma al frente.",
            R.drawable.ic_num_1));

        numeros.add(new Numero("2", 
            "Se extienden los dedos índice y medio hacia arriba, separados formando una \"V\", mientras los demás dedos permanecen cerrados.", 
            R.drawable.ic_num_2));

        numeros.add(new Numero("3", 
            "Se extienden el dedo índice, medio y anular hacia arriba, mientras el meñique y el pulgar permanecen cerrados.", 
            R.drawable.ic_num_3));

        numeros.add(new Numero("4", 
            "Se extienden los cuatro dedos (índice, medio, anular y meñique) hacia arriba, mientras el pulgar permanece doblado sobre la palma.", 
            R.drawable.ic_num_4));

        numeros.add(new Numero("5", 
            "Se extienden todos los dedos de la mano, incluido el pulgar, con la palma hacia adelante.", 
            R.drawable.ic_num_5));

        numeros.add(new Numero("6", 
            "Se hace la letra a con el pulgar hacia arriba.",
            R.drawable.ic_num_6));

        numeros.add(new Numero("7", 
            "Se hace una letra g, colocada en forma inclinada.",
            R.drawable.ic_num_7));

        numeros.add(new Numero("8", 
            "Se estiran los dedos pulgar, índice y medio, separados con la palma hacia usted",
            R.drawable.ic_num_8));

        numeros.add(new Numero("9", 
            "Se hace la letra b con la palma hacia usted. Luego se cierran los dedos rodeando el pulgar",
            R.drawable.ic_num_9));

        numeros.add(new Numero("10", 
            "Se hace el número 5 con la palma hacia arriba, y se rota la mano rápidamente hacia abajo.",
            R.drawable.ic_num_10));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
