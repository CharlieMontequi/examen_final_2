package com.example.examen_final_2

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import java.util.Date

class Actividad_3 : AppCompatActivity() {

    private lateinit var listViewListadoViajes: ListView
    private lateinit var db: DataBaseHelper
    private lateinit var viajesLista: MutableList<Viaje>

    // mierdas para la notificacion
    private val ID_CANAL_NOTIFICACION = "canal_notificacion_1"
    private val idNotificacion = 101
    private val REQUEST_CODE_NOTIFICATIONS = 1// constante del codigo para pedir permiso de la notifiacion

    private lateinit var viajeMarcado :Viaje

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_3)

        // pedir los permisos de la notificacion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprueba si el permiso no ha sido concedido
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicita el permiso al usuario
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATIONS)
            }
        }

        val centroBase: String = intent.getStringExtra("centroexistenteViaje").toString()

        viajesLista = mutableListOf()

        db = DataBaseHelper(this)
        viajesLista = db.obtenerTodos()

        val b_modificar = findViewById<Button>(R.id.b_modificar)
        val b_borrar = findViewById<Button>(R.id.b_borrar)
        val b_volver = findViewById<Button>(R.id.b_volver)


        listViewListadoViajes = findViewById(R.id.list_actividad3)

        val adaptadorPrioActividad3 = AdaptadorListActividad3(this, R.layout.listado_actividad3, viajesLista)
        listViewListadoViajes.adapter = adaptadorPrioActividad3

        listViewListadoViajes.setOnItemClickListener { adapterView, view, i, l ->
            viajeMarcado = Viaje(viajesLista[i].id, viajesLista[i].origen,  viajesLista[i].destino,  viajesLista[i].fecha,  viajesLista[i].hora)
        }

        b_volver.setOnClickListener {
            val intetnVolver = Intent(this, Actividad_2::class.java)
            intent.putExtra("centroexistenteVuelta", centroBase)
            startActivity(intetnVolver)
        }

        b_modificar.setOnClickListener {

            if (db.actualizarViaje(viajeMarcado)) {
                Toast.makeText(this, "viaje modificado con exito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "el viaje no se ha podido modificar", Toast.LENGTH_SHORT)
                    .show()
            }
            val intetnVolverActualizar = Intent(this, Actividad_2::class.java)
            intent.putExtra("centroexistente", centroBase)
            startActivity(intetnVolverActualizar)
        }

        b_borrar.setOnClickListener {

            if (db.borrarViaje(viajeMarcado)) {
                Toast.makeText(this, "viaje borrado con exito", Toast.LENGTH_SHORT).show()
                viajesLista= db.obtenerTodos()

                val adaptadorPrioActividadActualizado = AdaptadorListActividad3(this, R.layout.listado_actividad3, viajesLista)
                listViewListadoViajes.adapter = adaptadorPrioActividadActualizado

                crearCanalNotificacion()
                val administradorNotificaciones = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val notificacion = NotificationCompat.Builder(this, ID_CANAL_NOTIFICACION)
                    .setContentTitle("Viaje borrado")
                    .setContentText("Se ha borrado un viaje.")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)  // La notificación se cierra automáticamente al hacer clic en ella
                    .build()

                // Muestra la notificación
                administradorNotificaciones.notify(idNotificacion, notificacion)
            } else {
                Toast.makeText(this, "el viaje no se ha podido borrar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class AdaptadorListActividad3(
        contexto: Context,
        posiciones: Int,
        listado: MutableList<Viaje>
    ) : ArrayAdapter<Viaje>(contexto, posiciones, listado) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crecarFilaPersonal(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crecarFilaPersonal(position, convertView, parent)
        }

        fun crecarFilaPersonal(position: Int, convertView: View?, parent: ViewGroup): View {
            val vista = convertView ?: LayoutInflater.from(context)
            val filaVista =
                convertView ?: layoutInflater.inflate(R.layout.listado_actividad3, parent, false)

            val fecha = filaVista.findViewById<TextView>(R.id.txt_fecha)
            val hora = filaVista.findViewById<TextView>(R.id.txt_hora)
            val origen = filaVista.findViewById<TextView>(R.id.txt_origen)
            val destino = filaVista.findViewById<TextView>(R.id.txt_destino)
            val vaije = viajesLista[position]
            fecha.text = vaije.fecha.toString()
            hora.text= vaije.hora
            origen.text = vaije.origen
            destino.text= vaije.destino
            return filaVista
        }
    }

    fun crearCanalNotificacion(){
        // pedir los permisos
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            // definir nombre y descripcion del canal
            val nombreCanalNotificaion ="Canal bares nuevos"
            val descripionCnalNotificacion ="Canal para avisar de que se ha creado un bar nuevo"
            // dar de importancia a la notifiacion para que el sistema la reconozca y la muerte por encima de otras apps
            val importanciaCanalNotificacion = NotificationManager.IMPORTANCE_HIGH

            // crear el canal de la notificacion
            val canalCreado = NotificationChannel(ID_CANAL_NOTIFICACION, nombreCanalNotificaion, importanciaCanalNotificacion).apply { description = descripionCnalNotificacion }

            // registrar el canal de notificacion
            val notificationManager : NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canalCreado)



        }
    }
}