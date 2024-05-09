package com.example.restauranteventas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class SeleccionCartaProductos extends AppCompatActivity {

    //Declaración de variables
    Button btnBebidas, btnPlatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seleccion_carta_productos);

        //Relación con los elementos del layout
        btnBebidas=findViewById(R.id.btnBebidas);
        btnPlatos=findViewById(R.id.btnPlatos);

        //Asignación de funcionalidad a los botones
        View.OnClickListener seleccionCarta=v->{
            //Se asigna el texto del botón que invoque el evento
            String producto=((Button) v).getText().toString().toLowerCase();
            //Se invoca la actividad de ventas mediante un intent y se le pasa como parámetro el tipo de producto
            startActivity(new Intent(this, Ventas.class).putExtra("producto",producto));
        };

        btnBebidas.setOnClickListener(seleccionCarta);

        btnPlatos.setOnClickListener(seleccionCarta);

    }
}