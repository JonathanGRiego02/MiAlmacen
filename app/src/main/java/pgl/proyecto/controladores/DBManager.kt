package pgl.proyecto.controladores

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pgl.proyecto.actividades.modelos.Almacen
import pgl.proyecto.actividades.modelos.Objeto

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

    // Comprobar los datos en el login
    fun checkUserCredentials(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL_USUARIO = ? AND $COLUMN_PASSWORD_USUARIO = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }

    // Añadir un usuario a la base de datos
    fun addUser(nombre: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL_USUARIO = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        if (cursor.count > 0) {
            cursor.close()
            db.close()
            return false
        }
        val newUser = "INSERT INTO $TABLE_USUARIOS ($COLUMN_NAME_USUARIO, $COLUMN_EMAIL_USUARIO, $COLUMN_PASSWORD_USUARIO) VALUES ('$nombre', '$email', '$password')"
        db.execSQL(newUser)
        cursor.close()
        db.close()
        return true
    }

    // Función para obtener el array de Almacenes
    @SuppressLint("Range") // El android studio me sugería esto para que el rango no fuera -1
    fun getAlmacenes(): ArrayList<Almacen> {
        val almacenes = ArrayList<Almacen>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_ALMACEN"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val idAlmacen = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_ALMACEN))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ALMACEN))
                val direccion = cursor.getString(cursor.getColumnIndex(COLUMN_DIRECCION_ALMACEN))
                almacenes.add(Almacen(idAlmacen, nombre, direccion))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return almacenes
    }

    // Función para obtener el array de Objetos
    @SuppressLint("Range")
    fun getObjetosByAlmacen(idAlmacen: Int): List<Objeto> {
        val objetos = ArrayList<Objeto>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_STOCK WHERE $COLUMN_ID_ALMACEN_FK = ?"
        val cursor = db.rawQuery(query, arrayOf(idAlmacen.toString()))
        if (cursor.moveToFirst()) {
            do {
                val idObjeto = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_OBJETO))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_OBJETO))
                val peso = cursor.getDouble(cursor.getColumnIndex(COLUMN_PESO_OBJETO))
                val ancho = cursor.getDouble(cursor.getColumnIndex(COLUMN_ANCHO_OBJETO))
                val largo = cursor.getDouble(cursor.getColumnIndex(COLUMN_LARGO_OBJETO))
                val idAlmacenFk = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_ALMACEN_FK))
                objetos.add(Objeto(idObjeto, nombre, peso, ancho, largo, idAlmacenFk))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return objetos
    }

    // Añadir un objeto a la base de datos
    fun addObjeto(objeto: Objeto): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME_OBJETO, objeto.nombre)
            put(COLUMN_PESO_OBJETO, objeto.peso)
            put(COLUMN_ANCHO_OBJETO, objeto.ancho)
            put(COLUMN_LARGO_OBJETO, objeto.largo)
            put(COLUMN_ID_ALMACEN_FK, objeto.idAlmacen)
        }
        val result = db.insert(TABLE_STOCK, null, contentValues)
        db.close()
        return result != -1L
    }

    fun deleteObjeto(idObjeto: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_STOCK, "$COLUMN_ID_OBJETO = ?", arrayOf(idObjeto.toString()))
        db.close()
        return result > 0
    }

    fun addAlmacen(almacen: Almacen): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME_ALMACEN, almacen.nombre)
            put(COLUMN_DIRECCION_ALMACEN, almacen.direccion)
        }
        val result = db.insert(TABLE_ALMACEN, null, contentValues)
        db.close()
        return result != -1L
    }

    fun deleteAlmacen(idAlmacen: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_ALMACEN, "$COLUMN_ID_ALMACEN = ?", arrayOf(idAlmacen.toString()))
        db.close()
        return result > 0
    }

    @SuppressLint("Range")
    fun getUserNameByEmail(email: String): String? {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_NAME_USUARIO FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL_USUARIO = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USUARIO))
        }
        cursor.close()
        db.close()
        return userName
    }



}
