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
import com.example.tfg_friendpoint.databinding.ActivityRegister2Binding
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
    private var nickname: String = ""
    private var email: String = ""
    private var contraseña: String = ""
    private var fechaNacimiento: String = ""
    private lateinit var auth: FirebaseAuth


    //TODO: Limpiar codigo. Contraseña no deberia estar en la cabecera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegister2Binding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        getIntentData()

        mBinding.btnSeleccion.setOnClickListener {
            pickImageGalery()
        }

        mBinding.btnCompletarRegistro.setOnClickListener {
            val userUid: String? = registerUserCredentials(email, contraseña)
            Log.d("Btn", "uid: ${userUid}")
            if (!userUid.isNullOrEmpty()) {

            }
        }
    }

    private fun getIntentData() {
        val bundle = intent.extras
        if (bundle != null) {
            email = bundle.get("email").toString()
            contraseña = bundle.get("contraseña").toString()
            fechaNacimiento = bundle.get("fechaNacimiento").toString()
            nickname = bundle.get("nickname").toString()
        }
    }


    private fun registerUserCredentials(email: String?, contraseña: String?): String? {
        //TODO: Crear repo para auth e implementar corrutinas

        var userUid = ""
        if (!email.isNullOrBlank() && !contraseña.isNullOrBlank()) {
            auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener {
                userUid = it.result.user?.uid.toString()
                showSuccessfulRegisterToast()
                saveUserData(it.result.user!!.uid)
                //TODO Cambiar la forma de subir la imagen. Sustituir repo
                //uploadImage(it.result.user!!.uid, imageUri)
                showAuthActivity()
                Log.d("Main", "uid: ${it.result.user?.uid}")
            }
        } else {
            showMissingCredentialsAlert()
        }
        return userUid;
    }

    private fun saveUserData(uid: String) {
        val db = Firebase.firestore
        //TODO obtener foto
        val user =
            hashMapOf(
            "email" to email,
            "nickname" to nickname,
            "fechaNacimiento" to fechaNacimiento
        )
        val userDocument = db.collection("users").document(uid).set(user)
        userDocument
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
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



