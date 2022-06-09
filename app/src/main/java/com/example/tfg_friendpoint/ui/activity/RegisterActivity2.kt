package com.example.tfg_friendpoint.ui.activity

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.tfg_friendpoint.databinding.ActivityRegister2Binding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.model.Photo
import com.example.tfg_friendpoint.ui.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class RegisterActivity2 : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegister2Binding

    private var imageUri: Uri? = null
    private var externalImageUri: String? = null
    private val pickImage = 100
    private var userData = UserModel()
    private lateinit var auth: FirebaseAuth


    //TODO: Limpiar codigo. Contraseña no deberia estar en la cabecera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegister2Binding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        getIntentData()
        setupButtons()


    }

    private fun setupButtons() {
        setupButtonSelectPhoto()
        setupRegisterButton()

    }

    private fun setupRegisterButton() {
        mBinding.btnCompletarRegistro.setOnClickListener {
            if (!userData.email.isNullOrBlank() && !getPassword().isNullOrBlank()) {
                registerAndSaveUserData()
                showSuccessfulRegisterToast()
                showAuthActivity()
            }else{
                showMissingCredentialsAlert()
            }
        }
    }

    private fun setupButtonSelectPhoto() {
        mBinding.btnSeleccion.setOnClickListener {
            pickImageGalery()
        }
    }

    private fun registerAndSaveUserData() {
        var authRepo = AuthRepository()
        GlobalScope.launch(Dispatchers.IO) {
            val userUid: String? = authRepo.registerWithEmailAndPassword(userData.email, getPassword())
            if (!userUid.isNullOrEmpty()) {
                saveUserData(userUid)
            }else{
                showRegisterAlert()
            }
        }
    }

    fun getPassword(): String {
        var bundle = intent.extras
        var password = ""
        if (bundle != null) {
           password = bundle.get("password").toString()
        }
        Log.e("pswd", password)
        return password
    }

    private fun getIntentData() {
        var bundle = intent.extras
        if (bundle != null) {
            var email = bundle.get("email").toString()
            var fechaNacimiento = bundle.get("fechaNacimiento").toString()
            var nickname = bundle.get("nickname").toString()
            userData = UserModel(nickname, email, imageUri.toString(), fechaNacimiento)
        }
    }


    private fun saveUserData(uid: String) {
        //Guarda toda la informacion del usuario
        var userRepo = UsersRepository()
        GlobalScope.launch(Dispatchers.IO) {
            userRepo.uploadUserImage(uid, userData.photoUrl.toUri())
            userRepo.saveUserData(uid, userData)
            userRepo.updateUserImage(uid)
        }
    }

    private fun showSuccessfulRegisterToast() {
        Toast.makeText(
            this,
            "El registro se ha realizado correctamente. Introduce tus credenciales en el login",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showMissingCredentialsAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error ")
        builder.setMessage(
            "Faltan campos obligatorios por rellenar"
        )
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showRegisterAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error ")
        builder.setMessage(
            "No se ha podido realizar el registro correctamente. " +
                    "Asegurate de que no queden campos obligatorios por rellenar"
        )
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAuthActivity() {
        val authActivityIntent = Intent(this, AuthActivity::class.java).apply { }
        startActivity(authActivityIntent)
    }

    private fun pickImageGalery() {
        val gallery = Intent()
        gallery.action = Intent.ACTION_GET_CONTENT
        gallery.type = "image/*"
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), pickImage);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val photo = Photo(localUri = imageUri.toString())
            mBinding.ivCircle.setImageURI(imageUri)
        }
    }

    // El nombre de la imagen guardada es el que hay en el parentesis de child, darle un identificador unico
    /*fun uploadImage(userUid: String?, localImageUri :Uri?) {

        storageRef = FirebaseStorage.getInstance().reference.child("userImages/${userUid!!}")
        imageUri?.let { uri ->
            mBinding?.let {
                storageRef.putFile(uri)
                    .addOnSuccessListener {
                        GlobalScope.launch (Dispatchers.IO){
                            externalImageUri  = it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                                Log.i("URL", downloadUrl.toString())
                            }.await().path
                        }
                    }
            }
        }
    }*/


    /**
     * Vamos a tener una coleccion de usuarios en firestore, y por cada uno tendremos un documento.
     * Por otro lado tendremos por cada uno de los usuarrios una entrada en firebase storage con el mismo id.
     * Primero habrá que guardar la foto del  usuario en firestore, y despues crear un documento
     * con el mismo nombre para el usuario. Una vez alli se guardará el resto de la info del usuario
     */
}



