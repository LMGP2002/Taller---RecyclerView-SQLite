package bd;

public class Constantes {
    // Nombre de la base de datos
    public static final String NOMBRE_BD = "Ventas.bd";
    // Versión de la base de datos
    public static final Integer VERSION_BD = 1;
    // Nombre de la tabla de productos
    public static final String TABLA_PRODUCTO = "Producto";
    // Sentencia SQL para crear la tabla de productos
    public static final String CREAR_TABLA_PRODUCTO = "CREATE TABLE [" + TABLA_PRODUCTO + "] (" +
            "[id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "[nombre] TEXT  NOT NULL," +
            "[precio] FLOAT  NOT NULL," +
            "[estado] TEXT  NOT NULL," +
            "[tipo_producto] TEXT  NOT NULL)";
    // Sentencia SQL para eliminar
    public static final String ELIMINAR_TABLA_PRODUCTO = "DROP TABLE IF EXISTS [" + TABLA_PRODUCTO + "]";

    // Nombre de la tabla de ventas
    public static final String TABLA_VENTA = "Venta";
    // Sentencia SQL para crear la tabla de ventas
    public static final String CREAR_TABLA_VENTA = "CREATE TABLE [" + TABLA_VENTA + "] (" +
            "[id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "[id_producto] INTEGER  NOT NULL," +
            "[cantidad] INTEGER  NOT NULL)";
    // Sentencia SQL para eliminar
    public static final String ELIMINAR_TABLA_VENTA = "DROP TABLE IF EXISTS [" + TABLA_VENTA + "]";
}