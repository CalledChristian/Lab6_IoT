package com.example.lab6_iot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_iot.AnadirIngresoActivity;
import com.example.lab6_iot.Bean.Ingreso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.example.lab6_iot.R;
import com.example.lab6_iot.VerIngresoActivity;

public class ListaIngresosAdapter extends RecyclerView.Adapter<ListaIngresosAdapter.IngresoViewHolder> {

    private Context context;

    private List<Ingreso> listaIngresos;



    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public class IngresoViewHolder extends RecyclerView.ViewHolder{

        Ingreso ingreso;
        public IngresoViewHolder(@NonNull View itemView) {

            super(itemView);

        }

    }

    @NonNull
    @Override
    //Para inflar la vista
    public ListaIngresosAdapter.IngresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingreso, parent, false);
        return new ListaIngresosAdapter.IngresoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ListaIngresosAdapter.IngresoViewHolder holder, int position) {


        Ingreso ing = listaIngresos.get(position) ;
        holder.ingreso = ing;
        TextView titulo = holder.itemView.findViewById(R.id.textTitle1);
        String tituloStr = ing.getTitulo();
        if (tituloStr.length() > 17) {
            //concatenear titulo hasta 17 caracteres
            tituloStr = tituloStr.substring(0, 17)+"...";
            titulo.setText("Titulo: "+tituloStr);
        }else{
            titulo.setText("Titulo: "+ing.getTitulo());
        }
        TextView monto = holder.itemView.findViewById(R.id.textSubtitle1);
        String montoStr = String.valueOf(ing.getMonto());
        monto.setText("Monto: +S/"+montoStr);
        TextView fecha = holder.itemView.findViewById(R.id.textSubtitle2);
        fecha.setText("Fecha: "+dateFormat.format(ing.getFecha()));


        holder.itemView.findViewById(R.id.boton1).setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), VerIngresoActivity.class);
                intent.putExtra("ingreso",ing);
                //iniciar activity
                context.startActivity(intent);
                }
        );



    }

    @Override
    public int getItemCount() {
        //Este método debe indicar la cantidad total de elementos, en nuestro caso, del
        //arreglo “data”.
        return listaIngresos.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Ingreso> getListaIngresos() {
        return listaIngresos;
    }

    public void setListaIngresos(List<Ingreso> listaIngresos) {
        this.listaIngresos = listaIngresos;
    }
}
