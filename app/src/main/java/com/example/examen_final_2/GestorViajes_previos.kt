package com.example.examen_final_2

import android.content.Context
import android.content.SharedPreferences

class GestorViajes_previos (contexto : Context) {

    // instancia de las preferencias
    // se toma el modo_private para la gestion de cuentas(?)
    private val sharedPreferences : SharedPreferences = contexto.getSharedPreferences("AppPreference", Context.MODE_PRIVATE)

    companion object{
        private const val VIAJE_DELA1_GUARDADO ="esta_registrado"
        private const val NOMBREDELA1 ="id_usuario"
    }

    // funcion para guardar que en las preferencias que esta registrado
    fun existreViaje1 (estaRegistrado: Boolean){

        // por clave valor se indica que el refistro es true o false
        sharedPreferences.edit().putBoolean(VIAJE_DELA1_GUARDADO, estaRegistrado).apply()
    }

    fun comprobarViaje1(): Boolean{

        // toma el valor guardado, por defecto duelve false
        return sharedPreferences.getBoolean(VIAJE_DELA1_GUARDADO, false)
    }

    fun guardarViaje1 (centro: String){
        sharedPreferences.edit().putString(NOMBREDELA1, centro).apply()
    }
    fun obetenerCentro(): String? {
        return sharedPreferences.getString(NOMBREDELA1, "")
    }
}