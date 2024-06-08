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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab6_iot.Bean.Egreso;
import com.example.lab6_iot.Bean.Ingreso;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class VerEgresoActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private Egreso egreso;


    private TextView titulo;

    private EditText monto;

    private EditText descripcion;

    private TextView fecha;

    private TextView egresoTitle;

    private Button guardar;

    private Button editar;

    private Button eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_egreso);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


        db=FirebaseFirestore.getInstance();

        egreso = (Egreso) getIntent().getSerializableExtra("egreso");

        egresoTitle = findViewById(R.id.textView2);
        titulo=findViewById(R.id.titulo);
        monto=findViewById(R.id.editTextText10);
        descripcion=findViewById(R.id.editTextText5);
        fecha=findViewById(R.id.fecha);
        editar = findViewById(R.id.btnEditar);
        guardar = findViewById(R.id.btnGuardar);
        eliminar = findViewById(R.id.btnEliminar);


        egresoTitle.setText("EGRESO: "+egreso.getIdEgreso());
        titulo.setText(egreso.getTitulo());
        String montoStr = String.valueOf(egreso.getMonto());
        monto.setText(montoStr);
        descripcion.setText(egreso.getDescripcion());
        fecha.setText(dateFormat.format(egreso.getFecha()));

        editar.setOnClickListener(v -> toggleEditMode(true));
        guardar.setOnClickListener(v -> {
            toggleEditMode(false);
            guardarCambios();
        });

        eliminar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerEgresoActivity.this);
            builder.setMessage("¿Desea eliminar definitivamente el egreso " + egreso.getIdEgreso() + "?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Eliminar el sitio de la base de datos
                            db.collection("egresos")
                                    .document(egreso.getIdEgreso())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(VerEgresoActivity.this,EgresosFragment.class);
                                            startActivity(intent);
                                            Toast.makeText(VerEgresoActivity.this, "Egreso Eliminado", Toast.LENGTH_LONG).show();
                                            finish(); // Cerrar la actividad actual*/
                                            /*FragmentManager fragmentManager = getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            EgresosFragment egresosFragment = new EgresosFragment();
                                            fragmentTransaction.replace(R.id.fragment_container, egresosFragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();*/
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("EgresoDelete", "Error al Eliminar Egreso", e);
                                            Toast.makeText(VerEgresoActivity.this, "Error al Eliminar Egreso", Toast.LENGTH_LONG).show();
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

        // Actualizar los valores en el objeto egreso
        egreso.setMonto(monto);
        egreso.setDescripcion(descripcionStr);

        // Guardar los cambios en la base de datos
        db.collection("egresos")
                .document(egreso.getIdEgreso())
                .set(egreso)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VerEgresoActivity.this, "Cambios Guardados", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("VerEgreso", "Error al Actualizar Egreso", e);
                        Toast.makeText(VerEgresoActivity.this, "Error al Actualizar Egreso", Toast.LENGTH_LONG).show();
                    }
                });
    }
}