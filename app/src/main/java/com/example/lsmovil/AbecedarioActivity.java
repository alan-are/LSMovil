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

public class AbecedarioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LetraAdapter adapter;
    private List<Letra> letras;
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

        setContentView(R.layout.activity_abecedario);
        hideKeyboard(AbecedarioActivity.this);

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
            getSupportActionBar().setTitle("Abecedario");
        }

        // Configurar listener para el botón de regreso
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAbecedario);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columnas

        // Cargar datos del abecedario
        cargarAbecedario();

        // Configurar adapter
        adapter = new LetraAdapter(this, letras);
        recyclerView.setAdapter(adapter);
    }

    private void cargarAbecedario() {
        letras = new ArrayList<>();

        letras.add(new Letra("A", 
            "La mano se cierra en un puño con el dedo pulgar apoyado en el costado del dedo índice.", 
            R.drawable.ic_letra_a));

        letras.add(new Letra("B", 
            "La mano está abierta y plana. Los cuatro dedos (índice, medio, anular y meñique) se mantienen juntos y extendidos hacia arriba, mientras que el pulgar se cruza por delante de la palma.", 
            R.drawable.ic_letra_b));

        letras.add(new Letra("C", 
            "Los dedos y el pulgar se curvan para formar la letra \"C\".", 
            R.drawable.ic_letra_c));

        letras.add(new Letra("D", 
            "El dedo índice apunta hacia arriba. Las puntas del dedo medio y el pulgar se tocan, formando un círculo. Los dedos anular y meñique permanecen doblados.", 
            R.drawable.ic_letra_d));

        letras.add(new Letra("E", 
            "Los cuatro dedos se doblan hacia la palma y el pulgar se dobla sobre ellos, tocando o muy cerca de las yemas.", 
            R.drawable.ic_letra_e));

        letras.add(new Letra("F", 
            "Las puntas del dedo índice y el pulgar se juntan formando un círculo. Los otros tres dedos se mantienen extendidos y ligeramente separados.", 
            R.drawable.ic_letra_f));

        letras.add(new Letra("G", 
            "La mano se cierra en un puño y el dedo índice se extiende de forma horizontal, apuntando hacia un lado.", 
            R.drawable.ic_letra_g));

        letras.add(new Letra("H", 
            "Similar a la \"G\", pero se extienden los dedos índice y medio juntos de forma horizontal.", 
            R.drawable.ic_letra_h));

        letras.add(new Letra("I", 
            "La mano se cierra en un puño y el dedo meñique se extiende recto hacia arriba.", 
            R.drawable.ic_letra_i));

        letras.add(new Letra("J", 
            "Se comienza con la forma de la \"I\" (meñique extendido) y se dibuja la curva de una \"J\" en el aire.", 
            R.drawable.ic_letra_j));

        letras.add(new Letra("K", 
            "Se levantan los dedos índice y medio formando una \"V\". El pulgar se coloca en medio de ambos dedos.", 
            R.drawable.ic_letra_k));

        letras.add(new Letra("L", 
            "El dedo pulgar y el índice se extienden para formar una \"L\". Los demás dedos permanecen doblados.", 
            R.drawable.ic_letra_l));

        letras.add(new Letra("LL", 
            "Se hace la seña de la \"L\" y se realiza un movimiento rápido y corto hacia un lado.", 
            R.drawable.ic_letra_ll));

        letras.add(new Letra("M", 
            "Se cierra el puño y se coloca el pulgar debajo de los dedos índice, medio y anular, de manera que se vean los nudillos de estos tres dedos.", 
            R.drawable.ic_letra_m));

        letras.add(new Letra("N", 
            "Similar a la \"M\", pero el pulgar se coloca solo debajo de los dedos índice y medio.", 
            R.drawable.ic_letra_n));

        letras.add(new Letra("Ñ", 
            "Se realiza la seña de la \"N\" y se añade un pequeño giro o sacudida de la muñeca de lado a lado.", 
            R.drawable.ic_letra_nn));

        letras.add(new Letra("O", 
            "La punta de todos los dedos se junta con la punta del pulgar para formar un círculo, representando la letra \"O\".", 
            R.drawable.ic_letra_o));

        letras.add(new Letra("P", 
            "Es la misma forma de la \"K\" (dedos índice y medio en \"V\" con el pulgar en medio), pero la mano se orienta apuntando hacia abajo.", 
            R.drawable.ic_letra_p));

        letras.add(new Letra("Q", 
            "Los dedos índice y pulgar se juntan como una pinza apuntando hacia abajo, y se realiza un ligero movimiento de muñeca hacia abajo.", 
            R.drawable.ic_letra_q));

        letras.add(new Letra("R", 
            "Se levantan los dedos índice y medio y se cruzan uno sobre el otro.", 
            R.drawable.ic_letra_r));

        letras.add(new Letra("RR", 
            "Se realiza la seña de la \"R\" (dedos cruzados) y se mueve la mano de un lado a otro.", 
            R.drawable.ic_letra_rr));

        letras.add(new Letra("S", 
            "La mano se cierra en un puño, pero el pulgar se cruza por encima de los otros dedos.", 
            R.drawable.ic_letra_s));

        letras.add(new Letra("T", 
            "La mano se cierra en un puño y la punta del pulgar se asoma entre el dedo índice y el medio.", 
            R.drawable.ic_letra_t));

        letras.add(new Letra("U", 
            "Los dedos índice y medio se extienden hacia arriba, manteniéndose juntos.", 
            R.drawable.ic_letra_u));

        letras.add(new Letra("V", 
            "Los dedos índice y medio se extienden hacia arriba, pero separados, formando la letra \"V\".", 
            R.drawable.ic_letra_v));

        letras.add(new Letra("W", 
            "Se extienden los dedos índice, medio y anular, manteniéndolos separados.", 
            R.drawable.ic_letra_w));

        letras.add(new Letra("X", 
            "El dedo índice se dobla en forma de gancho, mientras los otros dedos permanecen en un puño. Se realiza un ligero movimiento hacia atrás, como si se enganchara algo.", 
            R.drawable.ic_letra_x));

        letras.add(new Letra("Y", 
            "El pulgar y el meñique se extienden, mientras que los tres dedos del medio permanecen doblados sobre la palma.", 
            R.drawable.ic_letra_y));

        letras.add(new Letra("Z", 
            "Con el dedo índice extendido, se dibuja la forma de la letra \"Z\" en el aire.", 
            R.drawable.ic_letra_z));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
