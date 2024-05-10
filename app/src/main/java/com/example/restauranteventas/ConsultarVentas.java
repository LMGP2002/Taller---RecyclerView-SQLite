package com.example.restauranteventas;

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
    // ArrayLists para almacenar los productos y la lista de productos
    ArrayList<Producto> arrayProductos;
    ArrayList<String> arrayListadoProductos;

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

        // Obtener una instancia de la ventana actual
        Window window = getWindow();

        // Establecer el color de la barra de estado
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.bar_color));

        Toolbar tb = findViewById(R.id.toolbar);//Barra con menú
        setSupportActionBar(tb);//Asignación del ToolBar al Layout, osea que se actva a prtir de lo que tb vaya a determinar

        // Consultar los productos existentes y mostrarlos en la ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListadoProductos);
        lvProductos.setAdapter(adapter);

        // Configurar el adaptador y el listener para el spinner de tipo de producto
        ArrayAdapter<CharSequence> adapterTipoProd = ArrayAdapter.createFromResource(this, R.array.tipo_producto, android.R.layout.simple_spinner_item);
        spinnerTipoProducto.setAdapter(adapterTipoProd);
        spinnerTipoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipo_producto = adapterView.getItemAtPosition(i).toString();
                // Código para manejar la selección del tipo de producto
                arrayProductos.clear(); // Limpiar el array de productos
                arrayListadoProductos.clear(); // Limpiar el array de strings
                consultarProductos(); // Obtener la lista actualizada
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
    private void consultarProductos() {
        try {
            // Conexión a la base de datos
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase data_base = helper.getReadableDatabase();

            // Realizar la consulta SQL para obtener los productos
            Cursor cursor = data_base.rawQuery("SELECT id, nombre, precio, estado, tipo_producto FROM " +
                    Constantes.TABLA_PRODUCTO+" WHERE tipo_producto=?", new String[]{tipo_producto}, null);
            // Procesar los resultados de la consulta
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    arrayProductos.add(new Producto(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getString(3), cursor.getString(4)));
                }
                generarLista();
            }else{
                arrayProductos.clear(); // Limpiar el array de productos
                arrayListadoProductos.clear(); // Limpiar el array de strings
                generarLista(); // Actualizar la lista después de limpiarla
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error general" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void generarLista() {
        // Crear un formateador de moneda para el formato colombiano
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        float totalPorTipo = 0f; // Variable para almacenar la suma de precios por tipo

        if (!arrayProductos.isEmpty()) { // Si el ArrayList de productos no está vacío
            for (Producto producto : arrayProductos) { // Recorrer cada producto en el ArrayList
                // Formatear el precio como moneda colombiana y agregarlo al ArrayList de cadenas
                String precioFormateado = formatoMoneda.format(producto.getPrecio());

                // Sumar el precio al total por tipo
                if (producto.getTipo_producto().equals(tipo_producto)) {
                    totalPorTipo += producto.getPrecio();
                }
                arrayListadoProductos.add(producto.getNombre() + "   " + precioFormateado + "   " + producto.getEstado());

            }
        }
        // Formatear el total por tipo como moneda colombiana
        String totalFormateado = formatoMoneda.format(totalPorTipo);

        // Mostrar el total por tipo en el TextView
        txtTotal.setText(totalFormateado);

        // Notificar al Adapter que los datos han cambiado
        adapter.notifyDataSetChanged();
    }
}