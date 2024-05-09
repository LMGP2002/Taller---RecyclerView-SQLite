package com.example.restauranteventas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {
    private List<ProductosItems> listaItems;
    private Context context;
    //Nuestro escuchador
    private View.OnClickListener listener;

    public MyAdapter(List<ProductosItems> listaItems, Context context) {
        this.listaItems = listaItems;
        this.context = context;
    }

    public List<ProductosItems> getListaItems() {
        return listaItems;
    }

    public void setListaItems(List<ProductosItems> listaItems) {
        this.listaItems = listaItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflación o haciendo visible el layout por cada registro
        View view=LayoutInflater.from(context).inflate(
                R.layout.productos_items_layout, parent,false);
        //Permite activar la escucha del listener
        view.setOnClickListener(this);


        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Relación de elementos del layout con la lista
        holder.txtNombre.setText(listaItems.get(position).getNombre());
        holder.txtPrecio.setText(listaItems.get(position).getPrecio());
        holder.txtCantidad.setText("Cantidad: "+listaItems.get(position).getCantidad().toString());
        holder.imgFoto.setImageResource(listaItems.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }
}
