package com.example.lsmovil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

import android.widget.ImageView;
import android.widget.TextView;

public class Principal extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseFirestore db;

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
        setContentView(R.layout.activity_principal);
        hideKeyboard(Principal.this);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();

        // Inicializar toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        styleToolbarTitle(toolbar);

        // Configurar drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Obtener los elementos del header
        View headerView = navigationView.getHeaderView(0);
        ImageView imageViewProfile = headerView.findViewById(R.id.imageViewProfile);
        TextView textViewName = headerView.findViewById(R.id.textViewName);
        TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);

        // Obtener información del usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Primero mostrar lo que tenemos de Firebase Auth
            String email = user.getEmail();
            if (email != null) {
                textViewEmail.setText(email);
            }

            // Intentar obtener el nombre de Firebase Auth (para usuarios de Google)
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                // Usuario de Google - usar displayName
                textViewName.setText(displayName);
            } else {
                // Usuario de email/contraseña - obtener nombre de Firestore
                obtenerNombreDeFirestore(user.getUid(), textViewName);
            }
        }

        // Configurar toggle del drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Menú navegación
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_soporte) {
                startActivity(new Intent(this, SoporteActivity.class));
            } else if (itemId == R.id.nav_configuracion) {
                startActivity(new Intent(this, ConfiguracionActivity.class));
            } else if (itemId == R.id.nav_acerca) {
                startActivity(new Intent(this, AcercaActivity.class));
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Google Sign-In (mantenido por si necesitas cerrar sesión después)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Aquí puedes agregar la funcionalidad específica que necesites para esta vista
        // Por ejemplo, si quieres agregar un botón o funcionalidad básica:
        setupBasicFunctionality();
    }

    private void obtenerNombreDeFirestore(String userId, TextView textViewName) {
        db.collection("usuarios").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        String nombre = task.getResult().getString("nombre");
                        if (nombre != null && !nombre.isEmpty()) {
                            textViewName.setText(nombre);
                        } else {
                            // Si no hay nombre en Firestore, usar el email como fallback
                            textViewName.setText("Usuario");
                        }
                    } else {
                        // Si falla Firestore, usar valor por defecto
                        textViewName.setText("Usuario");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al obtener nombre: " + e.getMessage());
                    textViewName.setText("Usuario");
                });
    }

    private void setupBasicFunctionality() {
        // Este método es para que agregues la funcionalidad específica que necesites
        // Por ejemplo, podrías agregar un botón de "Comenzar" o alguna acción principal

        // Ejemplo básico:
        // Button btnStart = findViewById(R.id.btnStart); // Si agregas un botón en el XML
        // btnStart.setOnClickListener(v -> {
        //     Toast.makeText(this, "Funcionalidad por implementar", Toast.LENGTH_SHORT).show();
        // });
    }

    private void styleToolbarTitle(Toolbar toolbar) {
        String fullText = "LSMovil";
        SpannableString spannableString = new SpannableString(fullText);

        int darkBlue = Color.parseColor("#FFFFFF");
        spannableString.setSpan(
                new ForegroundColorSpan(darkBlue),
                0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        int green = Color.parseColor("#FFFFFF");
        spannableString.setSpan(
                new ForegroundColorSpan(green),
                3, 7,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableString.setSpan(
                new StyleSpan(Typeface.BOLD),
                0, 7, // Solo aplica a "LS"
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Establecer el título estilizado
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableString);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aquí puedes recargar datos si es necesario cuando la actividad se reanude
    }
}