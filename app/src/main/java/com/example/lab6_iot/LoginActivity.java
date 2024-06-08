package com.example.lab6_iot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
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
}
