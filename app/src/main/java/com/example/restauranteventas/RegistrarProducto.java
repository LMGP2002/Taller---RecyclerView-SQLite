package com.example.restauranteventas;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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



public class RegistrarProducto extends AppCompatActivity {

    // Declaración de variables para los elementos de la interfaz de usuario
    EditText txtNombre, txtPrecio;
    Spinner spinnerTipoProducto;
    Button btnRegistrar;
    ListView lvProductos;

    // Variables para almacenar los valores seleccionados en los spinners
    String tipo_producto = "";
    String estado = "Activo";

    // ArrayLists para almacenar los productos y la lista de productos
    ArrayList<Producto> arrayProductos;
    ArrayList<String> arrayListadoProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_producto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar las vistas de la interfaz de usuario
        txtNombre = findViewById(R.id.txtNombre);
        txtPrecio = findViewById(R.id.txtPrecio);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        spinnerTipoProducto = findViewById(R.id.spinnerTipoProducto);
        lvProductos = findViewById(R.id.lvProductos);

        Toolbar tb = findViewById(R.id.toolbar);//Barra con menú
        setSupportActionBar(tb);//Asignación del ToolBar al Layout

        // Obtener una instancia de la ventana actual
        Window window = getWindow();

        // Establecer el color de la barra de estado
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.bar_color));

        // Inicializar los ArrayLists
        arrayProductos = new ArrayList<>();
        arrayListadoProductos = new ArrayList<>();

        // Método consultar productos
        consultarProductos();

        // Consultar los productos existentes y mostrarlos en la ListView
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListadoProductos);
        lvProductos.setAdapter(adapter);

        // Configurar el adaptador y el listener para el spinner de tipo de producto
        ArrayAdapter<CharSequence> adapterTipoProd = ArrayAdapter.createFromResource(this, R.array.tipo_producto, android.R.layout.simple_spinner_item);
        spinnerTipoProducto.setAdapter(adapterTipoProd);
        spinnerTipoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Código para manejar la selección del tipo de producto
                tipo_producto = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        // Listener para el botón de registrar
        btnRegistrar.setOnClickListener(v -> {
            //Validaciones
            if (txtNombre.getText().toString().isEmpty() || txtPrecio.getText().toString().isEmpty() ) {
                Toast.makeText(this, "Campos vacíos", Toast.LENGTH_LONG).show();
            }else if(tipo_producto.equals("Seleccione")) {
                Toast.makeText(this, "Verifique el tipo de producto", Toast.LENGTH_LONG).show();
            }else{
                registrarProducto();
            }
        });

        // Listener para la ListView que muestra un diálogo al hacer clic en una fila
        lvProductos.setOnItemClickListener((adapterView, view, position, id) -> {
            Producto producto = arrayProductos.get(position);
            mostrarDialogoEstado(producto);
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
    // Método para registrar un nuevo producto en la base de datos
    private void registrarProducto() {
        try {
            // Conexión a la base de datos
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase data_base = helper.getWritableDatabase();

            // Crear un objeto ContentValues con los valores del nuevo producto
            ContentValues values = new ContentValues();
            values.put("nombre", txtNombre.getText().toString());
            values.put("precio", txtPrecio.getText().toString());
            values.put("estado", estado);
            values.put("tipo_producto", tipo_producto);

            // Insertar el nuevo producto en la base de datos
            Long id = data_base.insert(Constantes.TABLA_PRODUCTO, null, values);

            // Notificar el resultado de la operación
            if (id > 0) {
                Toast.makeText(this, "Producto almacenado con exito", Toast.LENGTH_LONG).show();
                limpiarCampos();
                arrayProductos.clear(); // Limpiar el array de productos
                arrayListadoProductos.clear(); // Limpiar el array de strings
                consultarProductos(); // Obtener la lista actualizada
                ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListadoProductos);
                lvProductos.setAdapter(adapter); // Actualizar el adaptador de la ListView
            } else {
                Toast.makeText(this, "Error al crear el producto", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error general " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Método para consultar los productos existentes en la base de datos
    private void consultarProductos() {
        try {
            // Conexión a la base de datos
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase data_base = helper.getReadableDatabase();

            // Realizar la consulta SQL para obtener los productos
            Cursor cursor = data_base.rawQuery("SELECT id, nombre, precio, estado, tipo_producto FROM " +
                    Constantes.TABLA_PRODUCTO, null);

            // Procesar los resultados de la consulta
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    arrayProductos.add(new Producto(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getString(3), cursor.getString(4)));
                }
                generarLista();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error general" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void generarLista() {
        // Crear un formateador de moneda para el formato colombiano
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        if (!arrayProductos.isEmpty()) { // Si el ArrayList de productos no está vacío
            for (Producto producto : arrayProductos) { // Recorrer cada producto en el ArrayList
                // Formatear el precio como moneda colombiana y agregarlo al ArrayList de cadenas
                String precioFormateado = formatoMoneda.format(producto.getPrecio());
                arrayListadoProductos.add(producto.getNombre() + "   " + precioFormateado + "   " + producto.getEstado());
            }
        }
        // Imprimir el contenido del ArrayList de cadenas en el log
        Log.i("array", "generarLista: " + arrayListadoProductos.toString());
    }

    // Método para limpiar los campos de texto y restablecer los spinners
    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        spinnerTipoProducto.setSelection(0);
    }

    // Método para mostrar un diálogo para cambiar el estado del producto
    private void mostrarDialogoEstado(Producto producto) {
        String nuevoEstado = producto.getEstado().equals("Activo") ? "Inactivo" : "Activo";
        String mensaje = "¿Desea cambiar el estado del producto a " + nuevoEstado.toLowerCase() + "?";

        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar estado")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        actualizarEstadoProducto(producto.getId(), nuevoEstado);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Método para actualizar el estado de un producto en la base de datos
    private void actualizarEstadoProducto(int idProducto, String nuevoEstado) {
        try {

            //Conexión a la base de datos
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("estado", nuevoEstado);

            //id del producto al que se le va a modificar el estado
            String[] clausula = {String.valueOf(idProducto)};

            long modificacion_estado = db.update(Constantes.TABLA_PRODUCTO, values, "id = ?", clausula);

            if (modificacion_estado > 0) {
                arrayProductos.clear(); // Limpiar el array de productos
                arrayListadoProductos.clear(); // Limpiar el array de strings
                consultarProductos(); // Obtener la lista actualizada
                ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListadoProductos);
                lvProductos.setAdapter(adapter); // Actualizar el adaptador de la ListView
            } else {
                Toast.makeText(this, "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error general: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}