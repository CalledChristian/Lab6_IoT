package com.example.lab6_iot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6_iot.Bean.Ingreso;
import com.example.lab6_iot.databinding.FragmentIngresosBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class VerIngresoActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private Ingreso ingreso;


    private TextView titulo;

    private EditText monto;

    private EditText descripcion;

    private TextView fecha;

    private TextView ingresoTitle;

    private Button guardar;

    private Button editar;

    private Button eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_ingreso);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


        db=FirebaseFirestore.getInstance();

        ingreso = (Ingreso) getIntent().getSerializableExtra("ingreso");

        ingresoTitle = findViewById(R.id.textView2);
        titulo=findViewById(R.id.titulo);
        monto=findViewById(R.id.editTextText10);
        descripcion=findViewById(R.id.editTextText5);
        fecha=findViewById(R.id.fecha);
        editar = findViewById(R.id.btnEditar);
        guardar = findViewById(R.id.btnGuardar);
        eliminar = findViewById(R.id.btnEliminar);


        ingresoTitle.setText("INGRESO: "+ingreso.getIdIngreso());
        titulo.setText(ingreso.getTitulo());
        String montoStr = String.valueOf(ingreso.getMonto());
        monto.setText(montoStr);
        descripcion.setText(ingreso.getDescripcion());
        fecha.setText(dateFormat.format(ingreso.getFecha()));

        editar.setOnClickListener(v -> toggleEditMode(true));
        guardar.setOnClickListener(v -> {
            toggleEditMode(false);
            guardarCambios();
        });

        eliminar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerIngresoActivity.this);
            builder.setMessage("¿Desea eliminar definitivamente el ingreso " + ingreso.getIdIngreso() + "?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Eliminar el sitio de la base de datos
                            db.collection("ingresos")
                                    .document(ingreso.getIdIngreso())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(VerIngresoActivity.this,IngresosFragment.class);
                                            startActivity(intent);
                                            Toast.makeText(VerIngresoActivity.this, "Ingreso Eliminado", Toast.LENGTH_LONG).show();
                                            finish(); // Cerrar la actividad actual
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("IngresoDelete", "Error al Eliminar Ingreso", e);
                                            Toast.makeText(VerIngresoActivity.this, "Error al Eliminar Ingreso", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }


    private void toggleEditMode(boolean enable) {
        monto.setEnabled(enable);
        descripcion.setEnabled(enable);

        // Mostrar el botón de guardar solo cuando esté en modo edición
        guardar.setVisibility(enable ? View.VISIBLE : View.GONE);
        // Ocultar el botón de editar cuando esté en modo edición
        editar.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void guardarCambios() {

        db=FirebaseFirestore.getInstance();
        String montoStr = monto.getText().toString();
        float monto = Float.parseFloat(montoStr);
        String descripcionStr = descripcion.getText().toString();

        // Actualizar los valores en el objeto ingreso
        ingreso.setMonto(monto);
        ingreso.setDescripcion(descripcionStr);

        // Guardar los cambios en la base de datos
        db.collection("ingresos")
                .document(ingreso.getIdIngreso())
                .set(ingreso)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VerIngresoActivity.this, "Cambios Guardados", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("VerIngreso", "Error al Actualizar Ingreso", e);
                        Toast.makeText(VerIngresoActivity.this, "Error al Actualizar Ingreso", Toast.LENGTH_LONG).show();
                    }
                });
    }

}