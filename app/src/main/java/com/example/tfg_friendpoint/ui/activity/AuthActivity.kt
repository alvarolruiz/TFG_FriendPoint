package com.example.tfg_friendpoint.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.ActivityAuthBinding
import com.example.tfg_friendpoint.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
lateinit var mAuth : FirebaseAuth
lateinit var mBinding: ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthBinding.inflate(layoutInflater)
        val view =  mBinding.root
        setContentView(view)
        configAuth()
    }

    private fun configAuth() {
        mAuth = FirebaseAuth.getInstance()
        setupBotonLogin()
        setupBotonRegistro()

    }

    private fun setupBotonLogin(){
        mBinding.btnLogin.setOnClickListener {
            if (mBinding.etEmail.text.isNotEmpty() && mBinding.etContrasena.text.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(
                    mBinding.etEmail.text.toString(),
                    mBinding.etContrasena.text.toString()
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
            mBinding.btnRegistrar.setOnClickListener {
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
        builder.setMessage("Introduce tu email y contraseña o pulsa el boton Registrarse si no tienes cuenta")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

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