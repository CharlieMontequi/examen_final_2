package com.example.examen_final_2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater

import android.widget.ImageView
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var listadoActividad1: ListView
private var listadoCentros = arrayOf(
    "ies claudio moyano", "ies julian marias", "ies virgen espino"
)
private var listadoTelefonos = arrayOf("9833335472", "9833335472", "9833335472")

private val iconos = intArrayOf(
    R.drawable.claudio_moyano_logo,
    R.drawable.julian_marias_logo,
    R.drawable.virgen_espino_logo
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = DataBaseHelper(this)
        if(db.tablaVacia()){
            db.datosMinimos()
            Toast.makeText(this, "se ha creado nueva", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "tabla ya creada", Toast.LENGTH_SHORT).show()
        }

        listadoActividad1 = findViewById(R.id.listView_actividad1)

        val txtLLamar = findViewById<TextView>(R.id.txt_llamar)
        var imagen = findViewById<ImageView>(R.id.image_moverse)
        val adaptador = AdaptadorPropio(this, R.layout.listado_actividad1, listadoCentros)
        var centro :String? = null
        listadoActividad1.adapter = adaptador

        listadoActividad1.setOnItemClickListener { adapterView, view, i, l ->
            imagen.setImageResource(iconos[i])
            txtLLamar.text = listadoTelefonos[i]
            centro = listadoCentros[i]
        }

        txtLLamar.setOnClickListener {
            val intentChooser = Intent().apply {
                action = Intent.ACTION_CALL
            }

            val chooser = Intent.createChooser(intentChooser, "Abrir con:")
            startActivity(chooser)
        }

        imagen.setOnClickListener {
            val intent = Intent(this, Actividad_2::class.java)
            intent.putExtra("centroexistente", centro)
            if (centro != null) {
                startActivity(intent)

            } else {
                Toast.makeText(this, "no has seleccionado ningun centro", Toast.LENGTH_LONG).show()
            }

        }

    }

    private inner class AdaptadorPropio(
        context: Context,// contexto que es la main
        resource: Int, // el id de la vista que se va a usar
        listado: Array<String>
    ) : ArrayAdapter<String>(context, resource, listado) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crecarFilaPersonal(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crecarFilaPersonal(position, convertView, parent)
        }

        fun crecarFilaPersonal(position: Int, convertView: View?, parent: ViewGroup): View {
            val vista = convertView ?: LayoutInflater.from(context)


            val filaVista =
                convertView ?: layoutInflater.inflate(R.layout.listado_actividad1, parent, false)
            val txtNombre = filaVista.findViewById<TextView>(R.id.txt_nombre_listado_actividad1)
            val imagen = filaVista.findViewById<ImageView>(R.id.image_listado)
            val telefono = filaVista.findViewById<TextView>(R.id.txt_telefono_actividad1)

            txtNombre.text = listadoTelefonos[position]
            imagen.setImageResource(iconos[position])
            telefono.text = listadoTelefonos[position]





            return filaVista
        }
    }
}