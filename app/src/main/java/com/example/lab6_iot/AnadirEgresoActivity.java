package com.example.lab6_iot;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab6_iot.Bean.Egreso;
import com.example.lab6_iot.Bean.Ingreso;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AnadirEgresoActivity extends AppCompatActivity {

    private EditText titulo;

    private EditText monto;
    private EditText descripcion;
    private Button botonFecha;

    private Button botonGuardar;


    private Context context;

    FirebaseFirestore db;

    private Date fechaEgreso;

    private TextView fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anadir_egreso);

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();

        fecha = findViewById(R.id.textView6);

        botonFecha = findViewById(R.id.boton);

        botonGuardar = findViewById(R.id.button);


        botonFecha.setOnClickListener(v ->
                fechaDialog()
        );

        botonGuardar.setOnClickListener(v ->
                guardarEgreso());

    }



    //basado en gpt
    private void fechaDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    fechaEgreso = calendar.getTime();
                    fecha.setText(fechaEgreso.toString());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void guardarEgreso() {

        titulo = findViewById(R.id.editTextText4);
        monto = findViewById(R.id.editTextText10);
        descripcion = findViewById(R.id.editTextText5);

        String tituloStr = titulo.getEditableText().toString();
        String montoStr = monto.getEditableText().toString();
        float monto = Float.parseFloat(montoStr);
        String descripcionStr = descripcion.getEditableText().toString();
        //seccion basado en gpt
        if (fechaEgreso == null) {
            fecha.setError("Seleccione una fecha");
            return;
        }
        String idEgreso = generarIdEgreso();
        String idUsuario = "20182758";
        Egreso egreso = new Egreso(idEgreso, tituloStr, monto, descripcionStr, fechaEgreso, idUsuario);

        // Guardar los datos en Firestore
        db.collection("egresos")
                .document(idEgreso)
                .set(egreso)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "Egreso Guardado");
                    Toast.makeText(AnadirEgresoActivity.this, "Egreso Guardado", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AnadirEgresoActivity.this, EgresosFragment.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al Guardar Egreso", e);
                    Toast.makeText(AnadirEgresoActivity.this, "Error al Guardar Egreso", Toast.LENGTH_LONG).show();
                });

    }

    private String generarIdEgreso() {
        String letrasAdmin = "EGRE";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }
}