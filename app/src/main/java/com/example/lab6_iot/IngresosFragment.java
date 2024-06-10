package com.example.lab6_iot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lab6_iot.Adapter.ListaIngresosAdapter;
import com.example.lab6_iot.Bean.Ingreso;
import com.example.lab6_iot.databinding.FragmentIngresosBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class IngresosFragment extends Fragment {

    List<Ingreso> listaIngresos;

    RecyclerView recyclerView;

    FirebaseFirestore db;
    private final static String TAG = "msg-test";

    ListaIngresosAdapter adapter;

    private FragmentIngresosBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentIngresosBinding.inflate(inflater, container, false);





        listaIngresos = new ArrayList<>();




        recyclerView = binding.recyclerviewIngresos;


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();


        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaIngresosAdapter();
        adapter.setContext(getContext());
        adapter.setListaIngresos(listaIngresos);
        recyclerView.setAdapter(adapter);


        obtenerIngresosDeFirestore();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnadirIngresoActivity.class);
            //intent.putExtra("idUsuario", iduser);
            //iniciar activity
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void obtenerIngresosDeFirestore() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) { //user logged-in
            if (currentUser.isEmailVerified()) {
                Log.d(TAG, "Firebase uid: " + currentUser.getUid());
            }
        }
        db.collection("ingresos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Ingreso ingreso = document.toObject(Ingreso.class);
                                if(ingreso.getIdUsuario().equals(currentUser.getUid())) {
                                    listaIngresos.add(ingreso);
                                }

                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los Ingresos", Toast.LENGTH_LONG).show();
                });
    }


}