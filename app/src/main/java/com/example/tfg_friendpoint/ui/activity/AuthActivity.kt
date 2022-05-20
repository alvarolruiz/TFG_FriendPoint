package com.example.tfg_friendpoint.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.tfg_friendpoint.R
import com.google.firebase.auth.FirebaseAuth

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
        btn_login = findViewById<Button>(R.id.btn_login)
        btn_registro = findViewById<Button>(R.id.btn_registrar)
        et_email = findViewById<EditText>(R.id.et_email)
        et_password = findViewById<EditText>(R.id.et_contraseña)
    }

    private fun configAuth() {
        btn_login.setOnClickListener {
            if (et_email.text.isNotEmpty() && et_password.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        et_email.text.toString(),
                        et_password.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showMainActivity(it.result?.user?.email ?: "")
                        } else {
                            showAlert()
                        }
                    }
            }
        }
        btn_registro.setOnClickListener {
            showRegisterActivity()
        }



    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMainActivity(email: String) {
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
        }
    }

    private fun showRegisterActivity() {
        val registerIntent = Intent(this, RegisterActivity::class.java).apply {
        }
    }
}