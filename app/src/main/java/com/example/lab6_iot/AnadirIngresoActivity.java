package com.example.lab6_iot;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab6_iot.Bean.Ingreso;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class AnadirIngresoActivity extends AppCompatActivity {


    private EditText titulo;

    private final static String TAG = "msg-test";

    private EditText monto;
    private EditText descripcion;
    private Button botonFecha;

    private Button botonGuardar;


    private Context context;

    FirebaseFirestore db;

    private Date fechaIngreso;

    private TextView fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anadir_ingreso);

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();

        fecha = findViewById(R.id.textView6);

        botonFecha = findViewById(R.id.boton);

        botonGuardar = findViewById(R.id.button);


        botonFecha.setOnClickListener(v ->
                fechaDialog()
        );

        botonGuardar.setOnClickListener(v ->
                guardarIngreso());

    }



    //basado en gpt
    private void fechaDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    fechaIngreso = calendar.getTime();
                    fecha.setText(fechaIngreso.toString());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void guardarIngreso() {

        titulo = findViewById(R.id.editTextText4);
        monto = findViewById(R.id.editTextText10);
        descripcion = findViewById(R.id.editTextText5);

        String tituloStr = titulo.getEditableText().toString();
        String montoStr = monto.getEditableText().toString();
        float monto = Float.parseFloat(montoStr);
        String descripcionStr = descripcion.getEditableText().toString();
        //seccion basado en gpt
        if (fechaIngreso == null) {
            fecha.setError("Seleccione una fecha de vencimiento");
            return;
        }
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) { //user logged-in
            if (currentUser.isEmailVerified()) {
                Log.d(TAG, "Firebase uid: " + currentUser.getUid());
            }
        }
        String idIngreso = generarIdIngreso();
        //String idUsuario = "20182758";
        Ingreso ingreso = new Ingreso(idIngreso, tituloStr, monto, descripcionStr, fechaIngreso, currentUser.getUid());

        // Guardar los datos en Firestore
        db.collection("ingresos")
                .document(idIngreso)
                .set(ingreso)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "Ingreso Guardado");
                    Toast.makeText(AnadirIngresoActivity.this, "Ingreso Guardado", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AnadirIngresoActivity.this, IngresosFragment.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al Guardar Ingreso", e);
                    Toast.makeText(AnadirIngresoActivity.this, "Error al Guardar Ingreso", Toast.LENGTH_LONG).show();
                });

    }

    private String generarIdIngreso() {
        String letrasAdmin = "ING";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }
}