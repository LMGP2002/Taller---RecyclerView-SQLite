package bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, Constantes.NOMBRE_BD, null, Constantes.VERSION_BD);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constantes.CREAR_TABLA_PRODUCTO);
        sqLiteDatabase.execSQL(Constantes.CREAR_TABLA_VENTA);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Constantes.ELIMINAR_TABLA_PRODUCTO);
        sqLiteDatabase.execSQL(Constantes.ELIMINAR_TABLA_VENTA);

        sqLiteDatabase.execSQL(Constantes.CREAR_TABLA_PRODUCTO);
        sqLiteDatabase.execSQL(Constantes.CREAR_TABLA_VENTA);

    }
}
