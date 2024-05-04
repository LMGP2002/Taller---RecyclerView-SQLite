package com.example.restauranteventas;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView txtNombre, txtPrecio;
    ImageView imgFoto;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        //Relación con elementos del layout
        txtNombre = itemView.findViewById(R.id.txtNombre);
        txtPrecio = itemView.findViewById(R.id.txtPrecio);
        imgFoto = itemView.findViewById(R.id.imgFoto);

    }

}
