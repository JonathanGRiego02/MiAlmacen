package pgl.proyecto.controladores

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MiAlmacen.db"
        private const val DATABASE_VERSION = 1

        // Tabla Usuarios
        const val TABLE_USUARIOS = "usuarios"
        const val COLUMN_ID_USUARIO = "idUsuario"
        const val COLUMN_NAME_USUARIO = "nombreUsuario"
        const val COLUMN_EMAIL_USUARIO = "emailUsuario"
        const val COLUMN_PASSWORD_USUARIO = "passwordUsuario"

        // Tabla Almacen
        const val TABLE_ALMACEN = "almacen"
        const val COLUMN_ID_ALMACEN = "idAlmacen"
        const val COLUMN_NAME_ALMACEN = "nombre"
        const val COLUMN_DIRECCION_ALMACEN = "direccion"

        // Tabla Stock
        const val TABLE_STOCK = "stock"
        const val COLUMN_ID_OBJETO = "idObjeto"
        const val COLUMN_NAME_OBJETO = "nombre"
        const val COLUMN_PESO_OBJETO = "peso"
        const val COLUMN_ANCHO_OBJETO = "ancho"
        const val COLUMN_LARGO_OBJETO = "largo"
        const val COLUMN_ID_ALMACEN_FK = "idAlmacen" // Clave foránea
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsuariosTable = "CREATE TABLE $TABLE_USUARIOS (" +
                "$COLUMN_ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_USUARIO TEXT NOT NULL, " +
                "$COLUMN_EMAIL_USUARIO TEXT NOT NULL, " +
                "$COLUMN_PASSWORD_USUARIO TEXT NOT NULL)"

        val createAlmacenTable = "CREATE TABLE $TABLE_ALMACEN (" +
                "$COLUMN_ID_ALMACEN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_ALMACEN TEXT NOT NULL, " +
                "$COLUMN_DIRECCION_ALMACEN TEXT NOT NULL)"

        val createStockTable = "CREATE TABLE $TABLE_STOCK (" +
                "$COLUMN_ID_OBJETO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_OBJETO TEXT NOT NULL, " +
                "$COLUMN_PESO_OBJETO REAL NOT NULL, " +
                "$COLUMN_ANCHO_OBJETO REAL NOT NULL, " +
                "$COLUMN_LARGO_OBJETO REAL NOT NULL, " +
                "$COLUMN_ID_ALMACEN_FK INTEGER NOT NULL, " +
                "FOREIGN KEY($COLUMN_ID_ALMACEN_FK) REFERENCES $TABLE_ALMACEN($COLUMN_ID_ALMACEN) ON DELETE CASCADE)"

        db.execSQL(createUsuariosTable)
        db.execSQL(createAlmacenTable)
        db.execSQL(createStockTable)

        // Inserción de datos de ejemplo
        db.execSQL("INSERT INTO $TABLE_USUARIOS ($COLUMN_NAME_USUARIO, $COLUMN_EMAIL_USUARIO, $COLUMN_PASSWORD_USUARIO) VALUES ('Jonathan', 'jonathan@gmail.com', 'jonathan123')")
        db.execSQL("INSERT INTO $TABLE_USUARIOS ($COLUMN_NAME_USUARIO, $COLUMN_EMAIL_USUARIO, $COLUMN_PASSWORD_USUARIO) VALUES ('Jorge', 'jorge@gmail.com', 'jorge123')")

        db.execSQL("INSERT INTO $TABLE_ALMACEN ($COLUMN_NAME_ALMACEN, $COLUMN_DIRECCION_ALMACEN) VALUES ('Almacen Central', 'Avenida Trinidad, 3')")

        db.execSQL("INSERT INTO $TABLE_STOCK ($COLUMN_NAME_OBJETO, $COLUMN_PESO_OBJETO, $COLUMN_ANCHO_OBJETO, $COLUMN_LARGO_OBJETO, $COLUMN_ID_ALMACEN_FK) VALUES ('Mesa', 10.5, 5.0, 15.0, 1)")
        db.execSQL("INSERT INTO $TABLE_STOCK ($COLUMN_NAME_OBJETO, $COLUMN_PESO_OBJETO, $COLUMN_ANCHO_OBJETO, $COLUMN_LARGO_OBJETO, $COLUMN_ID_ALMACEN_FK) VALUES ('Silla', 7.8, 3.5, 10.2, 1)")
        db.execSQL("INSERT INTO $TABLE_STOCK ($COLUMN_NAME_OBJETO, $COLUMN_PESO_OBJETO, $COLUMN_ANCHO_OBJETO, $COLUMN_LARGO_OBJETO, $COLUMN_ID_ALMACEN_FK) VALUES ('Lampara', 12.0, 6.0, 20.0, 1)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STOCK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALMACEN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }
}
