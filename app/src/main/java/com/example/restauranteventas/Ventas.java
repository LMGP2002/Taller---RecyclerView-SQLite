package com.example.restauranteventas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import bd.Constantes;
import bd.DBHelper;

public class Ventas extends AppCompatActivity {

    //Declaración de variables
    TextView txtTitle;
    RecyclerView rcvVista;

    FloatingActionButton fab;

    MyAdapter adapter;

    ArrayList<ProductosItems> arrProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

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
            registrarVenta();
            startActivity(new Intent(this,SeleccionCartaProductos.class));
        });

    }

    //Método para cargar los productos en el RecyclerView dependiendo del tipo seleccionado
    private void cargarProductos(String producto) {

        if(producto.equals("bebidas")){
            arrProductos.add(new ProductosItems(1,"Refresco", "$2.000", R.drawable.bebida,0));
            arrProductos.add(new ProductosItems(2,"Malteada", "$2.001", R.drawable.bebida,0));
            arrProductos.add(new ProductosItems(3,"Café", "$2.002", R.drawable.bebida,0));
        }else{

            arrProductos.add(new ProductosItems(4,"Churrasco", "$2.000", R.drawable.platos,0));
            arrProductos.add(new ProductosItems(5,"Pechuga", "$2.001", R.drawable.platos,0));
            arrProductos.add(new ProductosItems(6,"Pescado", "$2.002", R.drawable.platos,0));
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
                    //Inserción combinación clave-valor
                    ContentValues values = new ContentValues();
                    values.put("id_producto",arrProductos.get(i).getId());
                    values.put("cantidad",arrProductos.get(i).getCantidad());

                    //Inserción de registros
                    Long id = data_base.insert(Constantes.TABLA_VENTA,null,values);

                    //Notificación del proceso

                }
            }
            Toast.makeText(this,"Se ha realizado su compra",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Toast.makeText(this,"Error general " + ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /*public void listar(View view) {
        Toast.makeText(this, "listando", Toast.LENGTH_SHORT).show();

        try{
            //Conexión
            DBHelper helper = new DBHelper(this);
            //Objeto para leer los datos
            SQLiteDatabase data_base = helper.getReadableDatabase();

            //Cursor con el resultado de la consulta
            Cursor cursor = data_base.rawQuery("SELECT id,id_producto,cantidad FROM "+
                    Constantes.TABLA_VENTA,null);



            if(cursor!=null && cursor.getCount()>0){
                while (cursor.moveToNext()){
                    Log.i("venta", "id: "+cursor.getInt(0)+" id_p: "+cursor.getInt(1)+" cantidad: "+cursor.getInt(2));
                }
            }
            else{
                Toast.makeText(this,"VENTAS INEXISTENTES",Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception ex){
            Toast.makeText(this,"Error general" + ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }*/
}