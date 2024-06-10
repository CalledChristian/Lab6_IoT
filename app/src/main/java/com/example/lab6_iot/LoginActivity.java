package com.example.lab6_iot;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.lab6_iot.databinding.ActivityLoginBinding;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    EditText correo;
    EditText contrasena;
    Button btnLogin;
    private final static String TAG = "msg-test";

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*correo = findViewById(R.id.editCorreo);
        contrasena = findViewById(R.id.editContra);*/
        btnLogin = findViewById(R.id.buttonIngreso);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) { //user logged-in
            if (currentUser.isEmailVerified()) {
                Log.d(TAG, "Firebase uid: " + currentUser.getUid());
                goToMainActivity();
            }
        }

        btnLogin.setOnClickListener(view -> {

            btnLogin.setEnabled(false);

            AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.registro)
                    .setGoogleButtonId(R.id.buttonGoogle)
                    .setEmailButtonId(R.id.buttonMail)
                    .build();

            //no hay sesión
            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.Base_Theme_Lab6_IoT)
                    .setIsSmartLockEnabled(false)
                    .setAuthMethodPickerLayout(authMethodPickerLayout)
                    .setLogo(R.drawable.gestordinero)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .build();

            signInLauncher.launch(intent);
        });
    }

    /* launchers tienen 2 partes {
        1: contrato,
        2: callback: que hacer luego de finalizado el contrato
    } */

    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Log.d(TAG, "Firebase uid: " + user.getUid());
                        Log.d(TAG, "Display name: " + user.getDisplayName());
                        Log.d(TAG, "Email: " + user.getEmail());


                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()) {
                                goToMainActivity();
                            } else {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(LoginActivity.this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_LONG).show();
                                    //
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, "user == null");
                    }
                } else {
                    Log.d(TAG, "Canceló el Log-in");
                }
                btnLogin.setEnabled(true);
            }
    );

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}



        /*btnLogin.setOnClickListener(v -> {
                inicioSesion();
            });









    private void inicioSesion() {
        String correoStr = correo.getText().toString().trim();
        String contraStr = contrasena.getText().toString().trim();
        String TAG = "msg-auth";

        if (correoStr.isEmpty() || contraStr.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Ingrese correo y contraseña.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(correoStr, contraStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Se obtiene el rol del usuario
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //intent.putExtra("usuario",user);
                                startActivity(intent);
                                finish();
                            } else if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(LoginActivity.this, "Se le Envió un Correo para Validar su Cuenta", Toast.LENGTH_LONG).show();
                                });
                            }
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autenticación fallida.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/



    /*private void redirectToRoleSpecificActivity(String role) {
        if (role == null) {
            // Manejar el caso cuando el rol es nulo
            Log.e("TAG-role", "Role is null");
            return;
        }

        Intent intent;
        switch (role) {
            case "superadmin":
                intent = new Intent(this, SuperAdmin.class);
                startActivity(intent);
                break;
            case "administrador":
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case "supervisor":
                intent = new Intent(this, Supervisor.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected role: " + role);
        }
    }*/
    /*public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/


    /*private FirebaseAuth auth;
    private EditText correo;

    private EditText contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.editCorreo);
        contrasena = findViewById(R.id.editContra);
        Button loginButton = findViewById(R.id.buttonIngreso);
        Button registerButton = findViewById(R.id.buttonRegistro);

        loginButton.setOnClickListener(view ->
                iniciarSesion());

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }



    private void iniciarSesion() {
        String correoStr = correo.getText().toString();
        String contrasenaStr = contrasena.getText().toString();

        //inicio de sesion con correo
        auth.signInWithEmailAndPassword(correoStr, contrasenaStr).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                //Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(LoginActivity.this, "Error en las Credenciales", Toast.LENGTH_LONG).show();
            }
        });
    }
} */
