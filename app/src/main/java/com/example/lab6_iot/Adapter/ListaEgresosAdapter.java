package com.example.lab6_iot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_iot.Bean.Egreso;
import com.example.lab6_iot.Bean.Ingreso;
import com.example.lab6_iot.R;
import com.example.lab6_iot.VerEgresoActivity;
import com.example.lab6_iot.VerIngresoActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ListaEgresosAdapter extends RecyclerView.Adapter<ListaEgresosAdapter.EgresoViewHolder> {

    private Context context;

    private List<Egreso> listaEgresos;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public class EgresoViewHolder extends RecyclerView.ViewHolder {

        Egreso egreso;

        public EgresoViewHolder(@NonNull View itemView) {

            super(itemView);

        }

    }

    @NonNull
    @Override
    //Para inflar la vista
    public ListaEgresosAdapter.EgresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_egreso, parent, false);
        return new ListaEgresosAdapter.EgresoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEgresosAdapter.EgresoViewHolder holder, int position) {


        Egreso egre = listaEgresos.get(position);
        holder.egreso = egre;
        TextView titulo = holder.itemView.findViewById(R.id.textTitle1);
        String tituloStr = egre.getTitulo();
        if (tituloStr.length() > 17) {
            //concatenear titulo hasta 17 caracteres
            tituloStr = tituloStr.substring(0, 17) + "...";
            titulo.setText("Titulo: " + tituloStr);
        } else {
            titulo.setText("Titulo: " + egre.getTitulo());
        }
        TextView monto = holder.itemView.findViewById(R.id.textSubtitle1);
        String montoStr = String.valueOf(egre.getMonto());
        monto.setText("Monto: -S/" + montoStr);
        TextView fecha = holder.itemView.findViewById(R.id.textSubtitle2);
        fecha.setText("Fecha: " + dateFormat.format(egre.getFecha()));


        holder.itemView.findViewById(R.id.boton1).setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), VerEgresoActivity.class);
                    intent.putExtra("egreso", egre);
                    //iniciar activity
                    context.startActivity(intent);
                }
        );


    }

    @Override
    public int getItemCount() {
        //Este método debe indicar la cantidad total de elementos, en nuestro caso, del
        //arreglo “data”.
        return listaEgresos.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Egreso> getListaEgresos() {
        return listaEgresos;
    }

    public void setListaEgresos(List<Egreso> listaEgresos) {
        this.listaEgresos = listaEgresos;
    }
}
