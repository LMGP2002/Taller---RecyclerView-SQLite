package com.example.restauranteventas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import bd.Constantes;
import bd.DBHelper;

public class Ventas extends AppCompatActivity {

    //Declaración de variables
    TextView txtTitle;
    RecyclerView rcvVista;

    FloatingActionButton fab;

    MyAdapter adapter;

    Boolean huboCompra=false;

    ArrayList<ProductosItems> arrProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        Toolbar tb = findViewById(R.id.toolbar);//Barra con menú
        setSupportActionBar(tb);//Asignación del ToolBar al Layout, osea que se actva a prtir de lo que tb vaya a determinar


        //Relación con elementos del layout
        txtTitle = findViewById(R.id.txtTitle);
        rcvVista = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        //Instancias
        arrProductos = new ArrayList<>();


        // Obtener el Intent que inició esta actividad
        Intent intent = getIntent();

        // Verificar si el Intent no es nulo
        if (intent != null){
            // Obtener el valor que llega de la anterior actividad
            String producto = intent.getStringExtra("producto");
            txtTitle.setText("Carta de "+producto);

            cargarProductos(producto);

        }


        //Activación de reyclerview y asignación del adapter
        rcvVista.setLayoutManager(new LinearLayoutManager(this));

        adapter=new MyAdapter(arrProductos, getApplicationContext());

        //Implementación al método onclik que se genera en el adaptador
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtiene la cantidad actual de cada producto
                int cantidadActual=arrProductos.get(rcvVista.getChildAdapterPosition(v)).getCantidad();

                //Asigna una nueva cantidad y notifica al adapter que hubo cambios
                arrProductos.get(rcvVista.getChildAdapterPosition(v)).setCantidad(cantidadActual+1);
                adapter.notifyDataSetChanged();

            }
        });

        rcvVista.setAdapter(adapter);

        //Funcionalidad del FloatingActionButton
        fab.setOnClickListener(v->{
            huboCompra=false;
            registrarVenta();
            if(huboCompra){
                Toast.makeText(this,"Se ha realizado su compra",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,SeleccionCartaProductos.class));
            }else{
                Toast.makeText(this,"Seleccione por lo menos un producto",Toast.LENGTH_LONG).show();
            }
        });

    }

    //Método para cargar los productos en el RecyclerView dependiendo del tipo seleccionado
    private void cargarProductos(String producto) {

        try {
            // Conexión a la base de datos
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase data_base = helper.getReadableDatabase();

            if(producto.equals("bebidas")){

                // Definir los argumentos para la cláusula WHERE
                String[] condicion = { "Bebida", "Activo" };

                // Realizar la consulta SQL para obtener los productos
                Cursor cursor = data_base.rawQuery("SELECT id, nombre, precio, estado, tipo_producto FROM " +
                        Constantes.TABLA_PRODUCTO+ " WHERE tipo_producto = ? AND estado = ?", condicion);

                // Procesar los resultados de la consulta
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        // Crear un formateador de moneda para el formato colombiano
                        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

                        // Formatear el precio como moneda colombiana y agregarlo al ArrayList de cadenas
                        String precioFormateado = formatoMoneda.format(cursor.getFloat(2));

                        arrProductos.add(new ProductosItems(cursor.getInt(0),cursor.getString(1), precioFormateado, R.drawable.bebida,0));
                    }
                }

            }else{


                // Definir los argumentos para la cláusula WHERE
                String[] condicion = { "Plato", "Activo" };

                // Realizar la consulta SQL para obtener los productos
                Cursor cursor = data_base.rawQuery("SELECT id, nombre, precio, estado, tipo_producto FROM " +
                        Constantes.TABLA_PRODUCTO+ " WHERE tipo_producto = ? AND estado = ?", condicion);

                // Procesar los resultados de la consulta
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        // Crear un formateador de moneda para el formato colombiano
                        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

                        // Formatear el precio como moneda colombiana y agregarlo al ArrayList de cadenas
                        String precioFormateado = formatoMoneda.format(cursor.getFloat(2));

                        arrProductos.add(new ProductosItems(cursor.getInt(0),cursor.getString(1), precioFormateado, R.drawable.bebida,0));
                    }
                }

            }



        } catch (Exception ex) {
            Toast.makeText(this, "Error general" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    //Método para almacenar en la bd las ventas realizadas de cada producto
    private void registrarVenta() {
        try{
            //Conexión
            DBHelper helper = new DBHelper(this);
            //Objeto para realizar la escritura
            SQLiteDatabase data_base = helper.getWritableDatabase();

            for (int i = 0; i < arrProductos.size(); i++) {
                //Valida que la cantidad sea mayor a 0
                if(arrProductos.get(i).getCantidad()>0){
                    //Valida si hay productos a comprar
                    huboCompra=true;
                    //Inserción combinación clave-valor
                    ContentValues values = new ContentValues();
                    values.put("id_producto",arrProductos.get(i).getId());
                    values.put("cantidad",arrProductos.get(i).getCantidad());

                    //Inserción de registros
                    Long id = data_base.insert(Constantes.TABLA_VENTA,null,values);

                }
            }
        }
        catch (Exception ex){
            Toast.makeText(this,"Error general " + ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    //Método para visualizar el menú
    public boolean onCreateOptionsMenu(Menu menu) {
        //Visualización del menú en el ToolBar
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    //Método para manejar las ociones del menú
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