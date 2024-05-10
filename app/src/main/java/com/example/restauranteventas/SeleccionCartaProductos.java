package com.example.restauranteventas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


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

        Toolbar tb = findViewById(R.id.toolbar);//Barra con menú
        setSupportActionBar(tb);//Asignación del ToolBar al Layout, osea que se actva a prtir de lo que tb vaya a determinar

        // Obtener una instancia de la ventana actual
        Window window = getWindow();

        // Establecer el color de la barra de estado
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.bar_color));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        //Visualización del menú en el ToolBar
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Click sobre el menú
        if(item.getItemId()==R.id.id_item_config){
            //llama la actividad de vender con el fragment
            //esto sirve para que cuandos e haga clic en el item, se hace visible la  actividad
            startActivity(new Intent(this, SeleccionCartaProductos.class));
        }else if(item.getItemId()==R.id.id_item_config2){
            startActivity(new Intent(this, RegistrarProducto.class));
        }else if(item.getItemId()==R.id.id_item_config3){
            startActivity(new Intent(this, ConsultarVentas.class));
        }else if(item.getItemId()==R.id.id_item_config4){
            startActivity(new Intent(this, Login.class));
     }
        return super.onOptionsItemSelected(item);
    }
}