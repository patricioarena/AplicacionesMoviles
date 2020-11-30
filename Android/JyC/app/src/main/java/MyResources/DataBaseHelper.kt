package MyResources

import Models.UserDb
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

val DATABASENAME = "MY DATABASE"
val TABLENAME = "Users"

val COL_ID = "id"
val COL_ID_USER = "idUsuario"
val COL_NAME = "nombre"
val COL_APELLIDO = "apellido"
val COL_CALLE = "calle"
val COL_NUMERO = "numero"
val COL_CP = "cp"
val COL_LOCALIDAD = "localidad"
val COL_PROV = "provincia"
val COL_PAIS = "pais"
val COL_EMAIL = "email"
val COL_FECHA_REG = "fechaReg"
val COL_FECHA_NAC = "fechaNac"
val COL_TEL = "tel"
val COL_CEL = "cel"

class DataBaseHelper(var context: Context) : SQLiteOpenHelper(
    context, DATABASENAME, null,
    2
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE " + TABLENAME + " (" +
                "" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "" + COL_ID_USER + " VARCHAR(256)," +
                "" + COL_NAME + " VARCHAR(256)," +
                "" + COL_APELLIDO + " VARCHAR(256)," +
                "" + COL_CALLE + " VARCHAR(256)," +
                "" + COL_NUMERO + " INTEGER," +
                "" + COL_CP + " INTEGER," +
                "" + COL_LOCALIDAD + " VARCHAR(256)," +
                "" + COL_PROV + " VARCHAR(256)," +
                "" + COL_PAIS + " VARCHAR(256)," +
                "" + COL_EMAIL + " VARCHAR(256)," +
                "" + COL_FECHA_REG + " VARCHAR(256)," +
                "" + COL_FECHA_NAC + " VARCHAR(256)," +
                "" + COL_TEL + " VARCHAR(256)," +
                "" + COL_CEL + " VARCHAR(256))"

        db?.execSQL(query)
        Toast.makeText(context, "CREATE TABLE", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS " + TABLENAME
        db?.execSQL(DROP_TABLE)
        onCreate(db)
        Toast.makeText(context, "DROP TABLE", Toast.LENGTH_SHORT).show()
    }

    fun insertData(user: UserDb) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COL_ID_USER, user.idUsuario)
        contentValues.put(COL_NAME, user.nombre)
        contentValues.put(COL_APELLIDO, user.apellido)
        contentValues.put(COL_CALLE, user.calle)
        contentValues.put(COL_NUMERO, user.numero)
        contentValues.put(COL_CP, user.cp)
        contentValues.put(COL_LOCALIDAD, user.localidad)
        contentValues.put(COL_PROV, user.provincia)
        contentValues.put(COL_PAIS, user.pais)
        contentValues.put(COL_EMAIL, user.email)
        contentValues.put(COL_FECHA_REG, user.fechaReg)
        contentValues.put(COL_FECHA_NAC, user.fechaNac)
        contentValues.put(COL_TEL, user.tel)
        contentValues.put(COL_CEL, user.cel)

        val result = db.insert(TABLENAME, null, contentValues)

        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed insert", Toast.LENGTH_SHORT).show()
            db.close()
        } else {
            Toast.makeText(context, "Success insert", Toast.LENGTH_SHORT).show()
            db.close()
        }
    }

    fun readData(uid: String?): UserDb? {
//        val list: MutableList<UserDb> = ArrayList()
        try {
            var user = UserDb()
            val db = this.readableDatabase

            val result = db.rawQuery(
                "select * from " + TABLENAME + " where " + COL_ID_USER + "='" + uid + "'",
                null
            );
            if (result != null && result.moveToFirst()) {

                user.idUsuario = result.getString(result.getColumnIndex(COL_ID_USER))
                user.nombre = result.getString(result.getColumnIndex(COL_NAME))
                user.apellido = result.getString(result.getColumnIndex(COL_APELLIDO))
                user.calle = result.getString(result.getColumnIndex(COL_CALLE))
                user.numero = result.getString(result.getColumnIndex(COL_NUMERO)).toInt()
                user.cp = result.getString(result.getColumnIndex(COL_CP)).toInt()
                user.localidad = result.getString(result.getColumnIndex(COL_LOCALIDAD))
                user.provincia = result.getString(result.getColumnIndex(COL_PROV))
                user.pais = result.getString(result.getColumnIndex(COL_PAIS))
                user.email = result.getString(result.getColumnIndex(COL_EMAIL))
                user.fechaReg = result.getString(result.getColumnIndex(COL_FECHA_REG))
                user.fechaNac = result.getString(result.getColumnIndex(COL_FECHA_NAC))
                user.tel = result.getString(result.getColumnIndex(COL_TEL))
                user.cel = result.getString(result.getColumnIndex(COL_CEL))
                return user
            }
            result.close();
            return null
        } catch (e: SQLiteException) {
            Log.e(ContentValues.TAG, "SQLiteException: ${e.stackTraceToString()}")
            return null
        }
    }

}
