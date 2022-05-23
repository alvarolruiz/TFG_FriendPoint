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
import java.io.File


class RegisterActivity2 : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegister2Binding
    private lateinit var nickName: String
    private lateinit var email: String
    private lateinit var contrasena: String
    private lateinit var fechaNacimiento: String

    private lateinit var imageView: ImageView
    private lateinit var file: File
    private lateinit var uri : Uri
    private lateinit var camIntent:Intent
    private lateinit var galIntent:Intent
    private lateinit var cropIntent:Intent
    private lateinit var btnImg: Button


    private val responseLauncher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val bitmap = it?.data?.extras?.get("data") as Bitmap
            mBinding.ivCircle.setImageBitmap(bitmap)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegister2Binding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        /*TODO: recibir la info del intent. Implementar la lógica para poder seleccionar una foto
        Y que se pueda guardar en firestore toda la información del usuario, a la vez que en firebase auth
        se registra un nuevo usuario. Una vez hecho esto se redigirá al usuario a la pagina principal
        */
        enableRuntimePermission()
        mBinding.btnSeleccion.setOnClickListener {
            pickImageGalery()
        }
    }



private fun openDialog() {
    val openDialog = AlertDialog.Builder(this@RegisterActivity2)
    openDialog.setIcon(R.drawable.logo_fp)
    openDialog.setTitle("Choose your Image in...!!")
    openDialog.setPositiveButton("Camera") { dialog, _ ->
        openCamera()
        dialog.dismiss()

    }
    openDialog.setNegativeButton("Gallery") { dialog, _ ->
        openGallery()
        dialog.dismiss()
    }
    openDialog.setNeutralButton("Cancel") { dialog, _ ->
        dialog.dismiss()
    }
    openDialog.create()
    openDialog.show()

}

private fun openGallery() {
    galIntent = Intent(
        Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    )
    startActivityForResult(
        Intent.createChooser(
            galIntent,
            "Select Image From Gallery "
        ), 2
    )
}

private fun openCamera() {
    camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    file = File(
        Environment.getExternalStorageDirectory(),
        "file" + System.currentTimeMillis().toString() + ".jpg"
    )
    uri = Uri.fromFile(file)
    camIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    camIntent.putExtra("return-data", true)
    startActivityForResult(camIntent, 0)
}

private fun enableRuntimePermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            this@RegisterActivity2, Manifest.permission.CAMERA
        )
    ) {
        Toast.makeText(
            this@RegisterActivity2,
            "Camera Permission allows us to Camera App",
            Toast.LENGTH_SHORT
        ).show()
    } else {
        ActivityCompat.requestPermissions(
            this@RegisterActivity2,
            arrayOf(Manifest.permission.CAMERA), RequestPermissionCode
        )
    }
}

private fun cropImages() {
    /**set crop image*/
    try {
        cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.setDataAndType(uri, "image/*")
        cropIntent.putExtra("crop", true)
        cropIntent.putExtra("outputX", 180)
        cropIntent.putExtra("outputY", 180)
        cropIntent.putExtra("aspectX", 3)
        cropIntent.putExtra("aspectY", 4)
        cropIntent.putExtra("scaleUpIfNeeded", true)
        cropIntent.putExtra("return-data", true)
        startActivityForResult(cropIntent, 1)

    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 0 && resultCode == RESULT_OK) {
        cropImages()
    } else if (requestCode == 2) {
        if (data != null) {
            uri = data.data!!
            cropImages()
        }
    } else if (requestCode == 1) {
        if (data != null) {
            val bundle = data.extras
            val bitmap = bundle!!.getParcelable<Bitmap>("data")
            mBinding.ivCircle.setImageBitmap(bitmap)
        }
    }
}

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
        RequestPermissionCode -> if (grantResults.size > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this@RegisterActivity2,
                "Permission Granted , Now your application can access Camera",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@RegisterActivity2,
                "Permission Granted , Now your application can not  access Camera",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


companion object {
    const val RequestPermissionCode = 111
}

private fun pickImageGalery() {
    val intent = Intent()
    intent.type = "image/*"

    intent.action = Intent.ACTION_GET_CONTENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    startActivityForResult(intent, 1)
    responseLauncher.launch(intent)
}


private fun getIntentData() {

}
}