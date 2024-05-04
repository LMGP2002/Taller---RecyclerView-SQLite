package bd;

public class Constantes {
    public static final String NOMBRE_BD="Ventas.bd";
    public static final Integer VERSION_BD=1;
    public static final String TABLA_PRODUCTO="Producto";
    public static final String CREAR_TABLA_PRODUCTO = "CREATE TABLE [" + TABLA_PRODUCTO + "] (" +
            "[id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "[nombre] TEXT  NOT NULL," +
            "[precio] FLOAT  NOT NULL,"+
            "[estado] BOOLEAN  NOT NULL,"+
            "[tipo_producto] INTEGER  NOT NULL)";

    public static final String ELIMINAR_TABLA_PRODUCTO = "DROP TABLE IF EXISTS ["+ TABLA_PRODUCTO + "]";

    public static final String TABLA_VENTA="Venta";
    public static final String CREAR_TABLA_VENTA = "CREATE TABLE [" + TABLA_VENTA + "] (" +
            "[id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "[id_producto] INTEGER  NOT NULL," +
            "[cantidad] INTEGER  NOT NULL)";

    public static final String ELIMINAR_TABLA_VENTA = "DROP TABLE IF EXISTS ["+ TABLA_VENTA + "]";

}
