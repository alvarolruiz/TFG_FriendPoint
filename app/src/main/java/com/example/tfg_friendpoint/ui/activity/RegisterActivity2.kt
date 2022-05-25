package com.example.tfg_friendpoint.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_friendpoint.databinding.ActivityRegister2Binding
import com.example.tfg_friendpoint.ui.model.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class RegisterActivity2 : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegister2Binding

    private var imageUri: Uri? = null
    private val pickImage = 100
    private lateinit var nickname: String
    private lateinit var email: String
    private lateinit var contraseña: String
    private lateinit var fechaNacimiento: String


    //TODO: recibir la info del intent. Que se segistre al usuario y se almacene su info en firestore.
    //Despues que se guarde su foto en storage con el id de usuario de firebase auth.
    //se registra un nuevo usuario. Una vez hecho esto se redigirá al usuario al login principal.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegister2Binding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        getIntentData()
        mBinding.btnSeleccion.setOnClickListener {
            pickImageGalery()
        }

        mBinding.btnCompletarRegistro.setOnClickListener {
            val userUid: String? = registerUserCredentials()
            if (!userUid.isNullOrEmpty()) {
                saveUserData(userUid)
                uploadImage(userUid)
                showAuthActivity()
            }
        }
    }

    private fun getIntentData() {
        email = intent.getStringExtra("email")!!
        contraseña = intent.getStringExtra("contraseña")!!
        fechaNacimiento = intent.getStringExtra("fechaNacimiento")!!
        nickname = intent.getStringExtra("nickname")!!


    }


    private fun registerUserCredentials(): String? {
        var userUid: String? = null
        if (!email.isNullOrBlank() && !contraseña.isNullOrBlank()) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                    email,
                    contraseña
                )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showSuccessfulRegisterToast()
                        userUid = it?.result?.user?.uid ?: ""
                    } else {
                        showRegisterAlert()
                    }
                }
        }
        return userUid;
    }

    private fun saveUserData(uid: String) {
        val db = Firebase.firestore
        val user = hashMapOf(
            "email" to email,
            "nickname" to nickname,
            "fechaNacimiento" to fechaNacimiento
        )
        val userDocument =  db.collection("users").document(uid)
        userDocument.set(user)
            .addOnSuccessListener {  }
    }

    private fun showSuccessfulRegisterToast() {
        Toast.makeText(
            this,
            "El registro se ha realizado correctamente. Introduce tus credenciales en el login",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showRegisterAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error ")
        builder.setMessage(
            "No se ha podido realizar el registro correctamente. " +
                    "Asegurate de que has rellenado todos los campos que son obligatorios"
        )
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAuthActivity() {
        val authActivityIntent = Intent(this, AuthActivity::class.java).apply {
        }
        startActivity(authActivityIntent)
    }

    private fun pickImageGalery() {
        val gallery = Intent()
        gallery.action = Intent.ACTION_GET_CONTENT
        gallery.type = "image/*"
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), pickImage);
    }

    // El nombre de la imagen guardada es el que hay en el parentesis de child, darle un identificador unico
    private fun uploadImage(userUid: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child(userUid)
        imageUri?.let { uri ->
            mBinding?.let {
                storageRef.putFile(uri)
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                            Log.i("URL", downloadUrl.toString())
                        }
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val photo = Photo(localUri = imageUri.toString())
            mBinding.ivCircle.setImageURI(imageUri)
        }
    }

    /**
     * Vamos a tener una coleccion de usuarios en firestore, y por cada uno tendremos un documento.
     * Por otro lado tendremos por cada uno de los usuarrios una entrada en firebase storage con el mismo id.
     * Primero habrá que guardar la foto del  usuario en firestore, y despues crear un documento
     * con el mismo nombre para el usuario. Una vez alli se guardará el resto de la info del usuario
     */
}



