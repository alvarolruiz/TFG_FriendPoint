package com.example.tfg_friendpoint.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.ActivityRegister2Binding

class RegisterActivity2 : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegister2Binding
    private lateinit var nickName :String
    private lateinit var email :String
    private lateinit var contrasena :String
    private lateinit var fechaNacimiento :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegister2Binding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        /*TODO: recibir la info del intent. Implementar la lógica para poder seleccionar una foto
        Y que se pueda guardar en firestore toda la información del usuario, a la vez que en firebase auth
        se registra un nuevo usuario. Una vez hecho esto se redigirá al usuario a la pagina principal
        */




    }

    private fun getIntentData(){
       email = intent.getStringExtra("email")
    }
}