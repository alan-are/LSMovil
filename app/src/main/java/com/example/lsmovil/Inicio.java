package com.example.lsmovil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Inicio extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button google_sign_in;
    private Button btnLogin;
    private EditText editEmailLogin;
    private EditText editPasswordLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private final int RC_SIGN_IN = 20;

    // Variables para controlar la visibilidad
    private TextView textViewSignUp;
    private TextView textViewSignUp2;
    private TextView textViewSignUp3;
    private TextView textView6;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo(): null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(Inicio.this, Principal.class));
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();

        // Configuración de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Inicializar elementos de UI para login con correo
        btnLogin = findViewById(R.id.btnLogin);
        editEmailLogin = findViewById(R.id.editEmailLogin);
        editPasswordLogin = findViewById(R.id.editPasswordLogin);
        CheckBox checkBoxShowPasswordLogin = findViewById(R.id.checkBoxShowPasswordLogin);

        // Inicializar TextViews
        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp2 = findViewById(R.id.textViewSignUp2);
        textViewSignUp3 = findViewById(R.id.textViewSignUp3);
        textView6 = findViewById(R.id.textView6);

        // Configurar mostrar/ocultar contraseña
        checkBoxShowPasswordLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Mostrar contraseña
                    editPasswordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Ocultar contraseña
                    editPasswordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                // Mover el cursor al final del texto
                editPasswordLogin.setSelection(editPasswordLogin.getText().length());
            }
        });

        // Configurar botón de login con correo
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithEmail();
            }
        });

        // Configurar texto de registro
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Inicio.this, SignUpActivity.class));
            }
        });

        textViewSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Inicio.this, ForgotPasswordActivity.class));
            }
        });

        // Configuración de Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        google_sign_in = findViewById(R.id.btnSignInGoogle);
        google_sign_in.setOnClickListener(v -> signInWithGoogle());
    }

    // Método para ocultar los textos de registro y mostrar progressbar
    private void showLoadingState() {
        progressBar.setVisibility(View.VISIBLE);
        textViewSignUp.setVisibility(View.GONE);
        textViewSignUp3.setVisibility(View.GONE);
        textViewSignUp2.setEnabled(false);
        btnLogin.setEnabled(false);
        google_sign_in.setEnabled(false);
    }

    // Método para mostrar los textos de registro y ocultar progressbar
    private void hideLoadingState() {
        progressBar.setVisibility(View.GONE);
        textViewSignUp.setVisibility(View.VISIBLE);
        textViewSignUp3.setVisibility(View.VISIBLE);
        textViewSignUp2.setEnabled(true);
        btnLogin.setEnabled(true);
        google_sign_in.setEnabled(true);
    }

    private void loginWithEmail() {
        String email, password;
        email = editEmailLogin.getText().toString();
        password = editPasswordLogin.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(Inicio.this,"Por favor, ingrese todos los campos",Toast.LENGTH_SHORT).show();
        } else{
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(Inicio.this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show();
                return;
            } else {
                hideKeyboard(Inicio.this);
                showLoadingState();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.collection("usuarios").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String nombre = documentSnapshot.getString("nombre");
                                    Toast.makeText(Inicio.this, "Bienvenido: " + nombre, Toast.LENGTH_SHORT).show();
                                    updateUI(user);
                                } else {
                                    Toast.makeText(Inicio.this,
                                            "Error: " + Objects.requireNonNull(task.getException()).getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    hideLoadingState();
                                }
                            });
                        }
                        else {
                            Toast.makeText(Inicio.this,"Error: Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                            hideLoadingState();
                        }
                    }
                });
            }
        }
    }

    private void signInWithGoogle() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        // VERIFICAR GOOGLE PLAY SERVICES
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            Log.e("GoogleSignIn", "Google Play Services no disponible: " + resultCode);
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000).show();
            } else {
                Toast.makeText(this, "Google Play Services no está disponible en este dispositivo", Toast.LENGTH_LONG).show();
            }
            return;
        }

        Log.d("GoogleSignIn", "Google Play Services disponible, iniciando sign-in...");
        showLoadingState();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null && account.getIdToken() != null) {
                    Log.d("GoogleSignIn", "Cuenta obtenida: " + account.getEmail());
                    firebaseAuthWithGoogle(account.getIdToken());
                } else {
                    Log.e("GoogleSignIn", "Cuenta o token nulo");
                    Toast.makeText(this, "Error: Cuenta de Google no válida", Toast.LENGTH_SHORT).show();
                    hideLoadingState();
                }
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Error en inicio de sesión: " + e.getStatusCode() + " - " + e.getMessage(), e);
                String errorMessage = getGoogleSignInErrorMessage(e.getStatusCode());
                Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                hideLoadingState();
            }
        }
    }

    // Agrega este método para manejar errores específicos
    private String getGoogleSignInErrorMessage(int statusCode) {
        switch (statusCode) {
            case 12500: // SIGN_IN_CANCELLED
                return "Inicio de sesión cancelado";
            case 12501: // SIGN_IN_CURRENTLY_IN_PROGRESS
                return "Inicio de sesión en progreso";
            case 12502: // SIGN_IN_FAILED
                return "Fallo en el inicio de sesión";
            case 10: // DEVELOPER_ERROR
                return "Error de configuración. Verifica el SHA-1 en Firebase";
            case 7: // NETWORK_ERROR
                return "Error de red. Verifica tu conexión";
            case 8: // INTERNAL_ERROR
                return "Error interno del servidor";
            default:
                return "Error desconocido: " + statusCode;
        }
    }

    private void resetUI() {
        hideLoadingState();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        showLoadingState();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("FirebaseAuth", "Usuario autenticado: " + user.getEmail());

                            // Guardar el usuario en Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("uid", user.getUid());
                            userData.put("nombre", user.getDisplayName());
                            userData.put("correo", user.getEmail());
                            userData.put("fotoURL", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);

                            db.collection("usuarios").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "Usuario guardado correctamente");
                                        updateUI(user);
                                        Toast.makeText(Inicio.this, "Bienvenido " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error al guardar usuario", e);
                                        // Aún así permitir el login aunque falle Firestore
                                        updateUI(user);
                                        Toast.makeText(Inicio.this, "Bienvenido " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Log.e("FirebaseAuth", "Usuario nulo después de autenticación");
                            hideLoadingState();
                            Toast.makeText(Inicio.this, "Error: Usuario no válido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("FirebaseAuth", "Error en autenticación con Firebase", task.getException());
                        hideLoadingState();
                        Toast.makeText(Inicio.this, "Error de autenticación: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Desconocido"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Inicio.this, Principal.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(Inicio.this, "Error en la autenticación",
                    Toast.LENGTH_SHORT).show();
            hideLoadingState();
        }
    }

    private void signOutInvalidUser(FirebaseUser user) {
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        googleSignInClient.signOut().addOnCompleteListener(task1 -> {
                            mAuth.signOut();
                            btnLogin.setEnabled(true);
                            google_sign_in.setEnabled(true);
                        });
                    }).addOnFailureListener(e -> {
                        Log.e("Auth", "Error al eliminar usuario", e);
                        googleSignInClient.signOut();
                        mAuth.signOut();
                        btnLogin.setEnabled(true);
                        google_sign_in.setEnabled(true);
                        updateUI(null);
                    });
        } else {
            googleSignInClient.signOut();
            mAuth.signOut();
            updateUI(null);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}