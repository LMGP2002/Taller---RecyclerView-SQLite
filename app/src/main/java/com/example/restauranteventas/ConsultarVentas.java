package com.example.restauranteventas;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import bd.Constantes;
import bd.DBHelper;

public class ConsultarVentas extends AppCompatActivity {
    // Declaración de variables para los elementos de la interfaz de usuario
    ListView lvProductos;
    TextView txtTotal;
    ArrayAdapter<String> adapter;
    Spinner spinnerTipoProducto;
    String tipo_producto = "";

    float totalVentas;
    String totalFormateado ="";
    // ArrayLists para almacenar los productos y la lista de productos
    ArrayList<Producto> arrayProductos;
    ArrayList<String> arrayListadoProductos;
    ArrayList<Venta> arrayVentas;
    ArrayList<String> arrayListadoVentas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_ventas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar las vistas de la interfaz de usuario
        spinnerTipoProducto = findViewById(R.id.spinnerTipoProducto);
        txtTotal = findViewById(R.id.txtTotal);
        lvProductos = findViewById(R.id.lvProductos);

        // Inicializar los ArrayLists
        arrayProductos = new ArrayList<>();
        arrayListadoProductos = new ArrayList<>();
        arrayVentas = new ArrayList<>();
        arrayListadoVentas = new ArrayList<>();

        // Obtener una instancia de la ventana actual
        Window window = getWindow();

        // Establecer el color de la barra de estado
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        Toolbar tb = findViewById(R.id.toolbar);//Barra con menú
        setSupportActionBar(tb);//Asignación del ToolBar al Layout, osea que se actva a prtir de lo que tb vaya a determinar

        // Consultar los productos existentes y mostrarlos en la ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListadoVentas);
        lvProductos.setAdapter(adapter);

        // Configurar el adaptador y el listener para el spinner de tipo de producto
        ArrayAdapter<CharSequence> adapterTipoProd = ArrayAdapter.createFromResource(this, R.array.tipo_producto, android.R.layout.simple_spinner_item);
        spinnerTipoProducto.setAdapter(adapterTipoProd);
        spinnerTipoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipo_producto = adapterView.getItemAtPosition(i).toString();
                // Código para manejar la selección del tipo de producto
                arrayVentas.clear(); // Limpiar el array de productos
                arrayListadoVentas.clear(); // Limpiar el array de strings

                consultarVentas(); // Obtener la lista actualizada


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        //Visualización del menú en el ToolBar
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Click sobre el menú
        if(item.getItemId()==R.id.id_item_config){
            //llama a la actividad deseada según el item seleccionado en el menú
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

    // Método para consultar los productos existentes en la base de datos
       private void consultarVentas() {
        try {
            // Conexión a la base de datos
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase data_base = helper.getReadableDatabase();

            // Realizar la consulta SQL para obtener los productos
            Cursor cursor = data_base.rawQuery("SELECT id, nombre, precio, estado, tipo_producto FROM " +
                    Constantes.TABLA_PRODUCTO + " WHERE tipo_producto=?", new String[]{tipo_producto}, null);

            // Procesar los resultados de la consulta de productos
            arrayProductos.clear(); // Limpiar el array de productos
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    arrayProductos.add(new Producto(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getString(3), cursor.getString(4)));
                } while (cursor.moveToNext());
            }

            // Cerrar el cursor de productos
            if (cursor != null) {
                cursor.close();
            }

            // Realizar la consulta SQL para obtener las ventas
            arrayVentas.clear(); // Limpiar el array de ventas
            for (Producto producto : arrayProductos) {
                Cursor cursor2 = data_base.rawQuery("SELECT id, id_producto, cantidad FROM " +
                        Constantes.TABLA_VENTA + " WHERE id_producto=?", new String[]{String.valueOf(producto.getId())}, null);

                if (cursor2 != null && cursor2.moveToFirst()) {
                    do {
                        arrayVentas.add(new Venta(cursor2.getInt(0), cursor2.getInt(1), cursor2.getInt(2)));
                    } while (cursor2.moveToNext());
                }

                // Cerrar el cursor de ventas
                if (cursor2 != null) {
                    cursor2.close();
                }
            }
            txtTotal.setText("");
            totalFormateado ="";
            generarLista();
        } catch (Exception ex) {
            Toast.makeText(this, "Error general" + ex.getMessage(), Toast.LENGTH_LONG).show();

        }
    }
    private void generarLista() {
        // Crear un formateador de moneda para el formato colombiano
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        // Reiniciar totalVentas a 0 antes de calcular las ventas para el nuevo tipo de producto
        totalVentas = 0f;

        arrayListadoVentas.clear(); // Limpiar el array de strings

        if (!arrayVentas.isEmpty()) { // Si el ArrayList de ventas no está vacío
            for (Venta venta : arrayVentas) {
                // Conexión a la base de datos
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase data_base = helper.getReadableDatabase();

                // Realizar la consulta SQL para obtener el producto correspondiente a la venta
                Cursor cursor = data_base.rawQuery("SELECT nombre, precio FROM " +
                        Constantes.TABLA_PRODUCTO + " WHERE id=?", new String[]{String.valueOf(venta.getId_producto())}, null);

                if (cursor != null && cursor.moveToFirst()) {
                    String nombreProducto = cursor.getString(0);
                    float precioProducto = cursor.getFloat(1);

                    float precioVenta = precioProducto * venta.getCantidad(); // Calcular el precio de esta venta
                    totalVentas += precioVenta; // Sumar al total de ventas

                    // Formatear el precio como moneda colombiana
                    String precioFormateado = formatoMoneda.format(precioProducto);

                    // Agregar la venta al ArrayList de cadenas con el nombre del producto
                    arrayListadoVentas.add(nombreProducto + " Cantidad: " + venta.getCantidad() + " Precio Unitario: " + precioFormateado);

                    cursor.close();
                }

                data_base.close();
            }
        }

        // Formatear el total de ventas como moneda colombiana
        totalFormateado = formatoMoneda.format(totalVentas);

        // Mostrar el total de ventas en el TextView
        txtTotal.setText(totalFormateado);

        // Notificar al Adapter que los datos han cambiado
        adapter.notifyDataSetChanged();
    }

}