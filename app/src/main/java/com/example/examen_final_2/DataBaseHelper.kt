package com.example.examen_final_2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date

class DataBaseHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Educacion"
        private const val DATABASE_VERSION = 1

        private const val TABLE_VIAJES = "Viajes"
        private const val KEY_ID_VIAJE = "idViaje"
        private const val KEY_ORIGEN = "origen"
        private const val KEY_DESTINO = "destino"
        private const val KEY_FECHA = "fecha"
        private const val KEY_HORA = "hora"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaBares = ("CREATE TABLE IF NOT EXISTS $TABLE_VIAJES (" +
                "$KEY_ID_VIAJE INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_ORIGEN TEXT," +
                "$KEY_DESTINO TEXT," +
                "$KEY_FECHA DATE," +
                "$KEY_HORA TIME)")
        if (db != null) {
            db.execSQL(crearTablaBares)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_VIAJES")
            onCreate(db)
        }
    }

    fun crearViaje(viaje:Viaje): Boolean{
        try {
            val db = this.writableDatabase
            val campos = ContentValues()
            campos.put(KEY_ID_VIAJE, viaje.id)
            campos.put(KEY_ORIGEN, viaje.origen)
            campos.put(KEY_DESTINO, viaje.destino)
            campos.put(KEY_FECHA, fechaATexto(viaje.fecha!!))
            campos.put(KEY_HORA, fechaATexto(viaje.fecha!!))
            db.insert(TABLE_VIAJES, null, campos)
            db.close()
            return true
        }catch (e: Exception) {
            Log.e("ErrorCrear", "Error al crear el viaje", e)
            return false
        }
    }

    fun borrarViaje(viaje:Viaje ): Boolean{

        try {
            val db = this.writableDatabase
           db.delete(TABLE_VIAJES, "$KEY_ID_VIAJE =?", arrayOf(viaje.id.toString()))
            db.close()
            return true
        }catch (e: Exception) {
            Log.e("ErrorBorrar", "Error al borrar el viaje", e)
            return false
        }
    }

    fun actualizarViaje (viaje:Viaje):Boolean{
        try {
            val db = this.writableDatabase
            val campos = ContentValues()

            campos.put(KEY_ORIGEN, viaje.origen)
            campos.put(KEY_DESTINO, viaje.destino)
            campos.put(KEY_FECHA, fechaATexto(viaje.fecha!!))
            campos.put(KEY_HORA, fechaATexto(viaje.fecha!!))
            db.update(TABLE_VIAJES, campos,"WHERE $KEY_ID_VIAJE =?", arrayOf(viaje.id.toString()))
            db.close()
            return true
        }catch (e: Exception) {
            Log.e("ErrorActualizar", "Error al actualizar viaje", e)
            return false
        }
    }

    fun obtenerTodos():MutableList<Viaje>{
        val db = this.readableDatabase
        val sentencia = "SELECT * FROM $TABLE_VIAJES"
        var cursor : Cursor? = null
        cursor = db.rawQuery(sentencia, null)
        var listado :MutableList<Viaje> = mutableListOf()
        if(cursor.moveToFirst()){
            do{
                val idX = cursor.getColumnIndex(KEY_ID_VIAJE)
                val origenX = cursor.getColumnIndex(KEY_ORIGEN)
                val destinoX = cursor.getColumnIndex(KEY_DESTINO)
                val fechaX = cursor.getColumnIndex(KEY_FECHA)
                val horaX = cursor.getColumnIndex(KEY_HORA)
                val viaje = Viaje(
                    id = cursor.getInt(idX),
                    origen =  cursor.getString(origenX),
                    destino = cursor.getString(destinoX),
                    fecha = textoAFecha(cursor.getString(fechaX)),
                    hora = cursor.getString(horaX)
                )
                listado.add(viaje)

            }while(cursor.moveToNext())
        }
        return listado
    }
    // conversor de fechas
    fun fechaATexto(fecha: Date): String {
        val formatoFecha = SimpleDateFormat("dd-MM-yy")
        return formatoFecha.format(fecha)
    }

    fun textoAFecha(textoFechado: String): Date {
        val formatoFecha = SimpleDateFormat("dd-MM-yy")
        return formatoFecha.parse(textoFechado)
    }

    fun horaATexto(hora: Time):String{
        val formatohora =SimpleDateFormat("hh:mm:ss")
        return  formatohora.format(hora)
    }

    fun textoAHora(textoHora:String): Time{
        val formatohora =SimpleDateFormat("hh:mm:ss")
        return  formatohora.parse(textoHora) as Time
    }

    // TABLA VACIA
    fun tablaVacia(): Boolean{
        var estaVacia = false
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_VIAJES", null)

        var cantidadDatosVueltos = 0

        if (cursor.moveToFirst()) {
            cantidadDatosVueltos =
                cursor.getInt(0)// se recoge el valor del cursor para no perder el dato al cerrar el cursor
            if (cantidadDatosVueltos == 0) {
                estaVacia = true
            } else {
                estaVacia = false
            }
            cursor.close()
            db.close()

        } else {
            estaVacia = false
        }
        return estaVacia
    }

    fun datosMinimos(){
        val viajes = listOf(
            Viaje(null, "julian marias", "caluidio moyano", null, null.toString()),
            Viaje(null, "julian marias", "caluidio moyano", null, null.toString()),
            Viaje(null, "julian marias", "caluidio moyano", null, null.toString()),
            Viaje(null, "julian marias", "caluidio moyano", null, null.toString())
        )
    }
}