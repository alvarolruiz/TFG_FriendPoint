package com.example.tfg_friendpoint.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.tfg_friendpoint.R
import com.google.firebase.auth.FirebaseAuth
lateinit var mAuth : FirebaseAuth
lateinit var btn_login: Button
lateinit var btn_registro: Button
lateinit var et_email: EditText
lateinit var et_password: EditText

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setViewElements()
        configAuth()
    }

    private fun setViewElements() {
        btn_login = findViewById(R.id.btn_login)
        btn_registro = findViewById(R.id.btn_registrar)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_contraseña)
    }

    private fun configAuth() {
        mAuth = FirebaseAuth.getInstance()
        setupBotonLogin()
        setupBotonRegistro()




    }

    private fun setupBotonLogin(){
        btn_login.setOnClickListener {
            if (et_email.text.isNotEmpty() && et_password.text.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(
                    et_email.text.toString(),
                    et_password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMainActivity(it.result?.user?.email ?: "")
                    } else {
                        showLoginFailAlert()
                    }
                }
            }else{
                showUnfilledFieldsAlert()
            }
        }
    }

    private fun setupBotonRegistro(){
        btn_registro.setOnClickListener {
            showRegisterActivity()
        }
    }

    private fun showLoginFailAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario. " +
                "Asegurate de que tus credenciales sean correctas")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showUnfilledFieldsAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Faltan campos por rellenar. " +
                "Introduce tu email y contraseña o pulsa el boton Registrarse si no tienes cuenta")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /*
    <
     */
    private fun showMainActivity(email: String) {
        val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(mainActivityIntent)
    }

    private fun showRegisterActivity() {
        val registerIntent = Intent(this, RegisterActivity::class.java).apply {  }
        startActivity(registerIntent)
    }
}