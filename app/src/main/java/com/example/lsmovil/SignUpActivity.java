package com.example.lsmovil;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private Button buttonBack;
    private Button buttonSignUp;
    private EditText editTextPassword, editTextConfirmPassword;
    private EditText editTextEmail;
    private EditText editTextName;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar vistas
        buttonBack = findViewById(R.id.backButton);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        CheckBox checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextEmail = findViewById(R.id.editTextEmailSignUp);
        editTextName = findViewById(R.id.editTextName);

        // Configurar botón de regreso
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configurar mostrar/ocultar contraseña
checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // Mostrar Password en ambos campos
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // Ocultar Password en ambos campos - CORREGIDO
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Cambiado aquí
        }

        // Mover el cursor al final del texto en ambos campos
        editTextPassword.setSelection(editTextPassword.getText().length());
        editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());
    }
});

        // Configurar botón de registro
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name, email, password, confirmPassword;
        name = editTextName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString();
        confirmPassword = editTextConfirmPassword.getText().toString();

        // Validaciones
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() < 3 || name.length() > 20) {
            Toast.makeText(SignUpActivity.this, "El nombre debe tener entre 3 y 20 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUpActivity.this, "Ingrese un email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ocultar teclado
        hideKeyboard(SignUpActivity.this);

        // deshabilitar botones
        buttonSignUp.setEnabled(false);
        buttonBack.setEnabled(false);

        // Crear usuario en Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                buttonSignUp.setEnabled(true);
                buttonBack.setEnabled(true);

                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    // Guardar información adicional en Firestore
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("uid", user.getUid());
                    userData.put("nombre", name);
                    userData.put("correo", email);
                    userData.put("fotoURL", "");
                    userData.put("provider", "email"); // Indicar que se registró con email

                    db.collection("usuarios").document(user.getUid())
                            .set(userData)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Usuario guardado correctamente en Firestore");
                                Toast.makeText(SignUpActivity.this, "Usuario " + name + " creado correctamente", Toast.LENGTH_SHORT).show();

                                // Cerrar sesión y regresar al login
                                mAuth.signOut();
                                startActivity(new Intent(SignUpActivity.this, Inicio.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Error al guardar usuario en Firestore", e);
                                Toast.makeText(SignUpActivity.this, "Error al guardar información del usuario", Toast.LENGTH_SHORT).show();
                                // Eliminar el usuario de Auth si falla Firestore
                                user.delete();
                            });

                } else {
                    String errorMessage = "Error al crear la cuenta";
                    if (task.getException() != null) {
                        if (Objects.requireNonNull(task.getException().getMessage()).contains("email address is already in use")) {
                            errorMessage = "El correo electrónico ya está en uso";
                        } else if (Objects.requireNonNull(task.getException().getMessage()).contains("network error")) {
                            errorMessage = "Error de red. Verifique su conexión";
                        } else {
                            errorMessage = "Error: " + task.getException().getMessage();
                        }
                    }
                    Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar recursos si es necesario
    }
}