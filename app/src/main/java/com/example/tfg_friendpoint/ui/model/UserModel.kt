package com.example.tfg_friendpoint.ui.model

import java.util.*

class UserModel {

    fun getEdad(): Int {
        val dob: Calendar = Calendar.getInstance()
        val today: Calendar = Calendar.getInstance()
        val list: List<String> = fechaNacimiento.split("/").toList()
        dob.set(Integer.parseInt(list[2]), Integer.parseInt(list[1]),Integer.parseInt(list[0]))

        var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    val nickName : String = ""
    val email: String = ""
    val photoUrl: String = ""
    val fechaNacimiento : String = ""

}