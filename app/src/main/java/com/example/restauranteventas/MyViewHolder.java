package com.example.restauranteventas;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    //Declaración de variables
    TextView txtNombre, txtPrecio,txtCantidad;
    ImageView imgFoto;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        //Relación con elementos del layout
        txtNombre = itemView.findViewById(R.id.txtNombre);
        txtPrecio = itemView.findViewById(R.id.txtPrecio);
        txtCantidad = itemView.findViewById(R.id.txtCantidad);
        imgFoto = itemView.findViewById(R.id.imgFoto);

    }

}
