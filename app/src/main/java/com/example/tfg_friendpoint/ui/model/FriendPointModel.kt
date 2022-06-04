package com.example.tfg_friendpoint.ui.model

import android.net.Uri
import androidx.core.net.toUri

class FriendPointModel() {
    var nombre: String = ""
    var plan: String = ""
    var descripcion: String = ""
    var photoUrl: String = ""
    var ubicacion: String = ""
    var maxNumeroMiembros: Int = 0
    var aficiones: ArrayList<String> = ArrayList()
    var miembros: ArrayList<UserModel> = ArrayList()


    fun getNumeroMiembros(): Int {
        return miembros.size
    }

    fun getEdadmedia(): Int? {
        var result = 0
        if(miembros.size>0){
            var sumaEdad = 0
            miembros.forEach { user ->
                sumaEdad += user.getEdad()
                result = sumaEdad/miembros.size
            }
        }

        return result
    }

}


