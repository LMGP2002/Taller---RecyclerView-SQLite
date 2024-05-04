package com.example.restauranteventas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcvVista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ProductosItems> alItems = new ArrayList<>();
        alItems.add(new ProductosItems("Refresco", "$2.000", R.drawable.bebida));
        alItems.add(new ProductosItems("Malteada", "$2.001", R.drawable.bebida));
        alItems.add(new ProductosItems("Café", "$2.002", R.drawable.bebida));

        //Activación de reyclerview y asignación del adaapter
        rcvVista = findViewById(R.id.recyclerView);
        rcvVista.setLayoutManager(new LinearLayoutManager(this));
        rcvVista.setAdapter(new MyAdapter(alItems, getApplicationContext()));

    }
}