package com.example.examen_final_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class Actividad_2 : AppCompatActivity() {

    private var listadoCentros = arrayOf(
        "ies claudio moyano", "ies julian marias", "ies virgen espino"
    )
    private lateinit var listadoNuevo  : MutableList<String>

    private lateinit var spinnerCentros : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_2)

        listadoNuevo = mutableListOf()
        // recoger el intent
        val centroBase: String = intent.getStringExtra("centroexistente").toString()
        var centroDestino =""

        // intent de vuelta
        val centroVuelta  : String = intent.getStringExtra("centroexistenteVuelta").toString()
        // añaditos distintos
        listadoCentros.forEach { centro ->
            if(!centro.equals(centroBase) || !centro.equals(centroVuelta)){
                listadoNuevo.add(centro)
            }
        }

        spinnerCentros = findViewById(R.id.spinner_centros)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listadoNuevo )
        spinnerCentros.adapter = adaptador


        spinnerCentros.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               centroDestino= listadoNuevo[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val bGuardar = findViewById<Button>(R.id.b_guardar)
        val bViajes = findViewById<Button>(R.id.b_ver_viajes)
        bGuardar.setOnClickListener {
            val db = DataBaseHelper(this)

            val viaje = Viaje(null, centroBase, centroDestino, Date(), null.toString())

           if(db.crearViaje(viaje)) {
               Toast.makeText(this, "viaje guardado con exito", Toast.LENGTH_SHORT).show()
           }else{
               Toast.makeText(this, "el viaje no se ha podido guardar", Toast.LENGTH_SHORT).show()
           }
        }

        bViajes.setOnClickListener {
            val intentViajes= Intent(this, Actividad_3::class.java)
            intent.putExtra("centroexistenteViaje", centroBase)
            startActivity(intentViajes)
        }


    }
}