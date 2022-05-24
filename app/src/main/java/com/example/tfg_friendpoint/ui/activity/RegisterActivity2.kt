package com.example.tfg_friendpoint.ui.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.ActivityRegister2Binding
import com.example.tfg_friendpoint.ui.model.Photo
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class RegisterActivity2 : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegister2Binding

    private var imageUri: Uri? = null
    private val pickImage = 100


    /*private val responseLauncher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val bitmap = it?.data?.extras?.get("data") as Bitmap
            mBinding.ivCircle.setImageBitmap(bitmap)
        }

    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegister2Binding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        /*TODO: recibir la info del intent. Implementar la lógica para poder seleccionar una foto
        Y que se pueda guardar en firestore toda la información del usuario, a la vez que en firebase auth
        se registra un nuevo usuario. Una vez hecho esto se redigirá al usuario a la pagina principal
        */
        mBinding.btnSeleccion.setOnClickListener {
            pickImageGalery()
        }

        mBinding.btnCompletarRegistro.setOnClickListener {
            uploadImage()
        }
    }

    private fun pickImageGalery() {
        val gallery = Intent()
        gallery.action=Intent.ACTION_GET_CONTENT
        gallery.type="image/*"

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), pickImage);

    }

    // El nombre de la imagen guardada es el que hay en el parentesis de child, darle un identificador unico
    private fun uploadImage(){
        val storageRef = FirebaseStorage.getInstance().reference.child("images")
        imageUri?.let{ uri ->
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



