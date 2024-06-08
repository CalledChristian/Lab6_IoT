package com.example.lab6_iot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_iot.Adapter.ListaEgresosAdapter;
import com.example.lab6_iot.Adapter.ListaIngresosAdapter;
import com.example.lab6_iot.Bean.Egreso;
import com.example.lab6_iot.Bean.Ingreso;
import com.example.lab6_iot.databinding.FragmentEgresosBinding;
import com.example.lab6_iot.databinding.FragmentIngresosBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EgresosFragment extends Fragment {

    List<Egreso> listaEgresos;

    RecyclerView recyclerView;

    FirebaseFirestore db;

    ListaEgresosAdapter adapter;


    private FragmentEgresosBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentEgresosBinding.inflate(inflater, container, false);

        listaEgresos = new ArrayList<>();




        recyclerView = binding.recyclerviewEgresos;


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();


        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaEgresosAdapter();
        adapter.setContext(getContext());
        adapter.setListaEgresos(listaEgresos);
        recyclerView.setAdapter(adapter);


        obtenerEgresosDeFirestore();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnadirEgresoActivity.class);
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

    private void obtenerEgresosDeFirestore() {
        db.collection("egresos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Egreso egreso = document.toObject(Egreso.class);
                            listaEgresos.add(egreso);

                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los Egresos", Toast.LENGTH_LONG).show();
                });
    }

}