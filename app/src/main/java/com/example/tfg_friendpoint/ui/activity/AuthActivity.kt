package com.example.tfg_friendpoint.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.tfg_friendpoint.databinding.ActivityAuthBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : AppCompatActivity() {

    var authRepo = AuthRepository()
    lateinit var mBinding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthBinding.inflate(layoutInflater)
        val view =  mBinding.root
        setContentView(view)
        configAuth()
    }

    private fun configAuth() {
        setupBotonLogin()
        setupBotonRegistro()

    }

    private fun setupBotonLogin(){
        mBinding.btnLogin.setOnClickListener {

            signIn()
            if (mBinding.etEmail.text.isNotEmpty() && mBinding.etContrasena.text.isNotEmpty()) {

            }else{
                showUnfilledFieldsAlert()
            }
        }
    }

    private fun signIn() {
        val email = mBinding.etEmail.text.toString()
        val contraseña = mBinding.etContrasena.text.toString()
        GlobalScope.launch (Dispatchers.IO){
            val userUid = authRepo.signInWithEmailAndPassword(email, contraseña)
            withContext(Dispatchers.Main){
                if(userUid!=null) showMainActivity(userUid)
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

    private fun showMainActivity(uid: String) {
        val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("userUid", uid)
        }
        startActivity(mainActivityIntent)
    }

    private fun showRegisterActivity() {
        val registerIntent = Intent(this, RegisterActivity::class.java).apply {  }
        startActivity(registerIntent)
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if(authRepo.currentUser!=null){
            showMainActivity(authRepo.currentUser!!.uid)
        }

    }

}