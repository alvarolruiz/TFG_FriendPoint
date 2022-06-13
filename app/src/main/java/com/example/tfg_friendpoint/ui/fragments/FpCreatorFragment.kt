package com.example.tfg_friendpoint.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentFpCreatorBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.example.tfg_friendpoint.ui.model.Photo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class FpCreatorFragment : Fragment() {
    private lateinit var mBinding: FragmentFpCreatorBinding
    private val pickImage = 100
    private var imageUri: Uri? = null
    private val dbReference = Firebase.firestore.collection("FriendPoints")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentFpCreatorBinding.inflate(inflater, container, false)
        val view = mBinding.root
        setupButtons()


        return view
    }

    private fun setupButtons() {
        setupPhotoImageButton()
        setupRegisterButton()
    }

    private fun setupPhotoImageButton() {
        mBinding.createIbPhoto.setOnClickListener {
            pickImageGalery()
        }
    }

    private fun setupRegisterButton() {
        mBinding.btnRegister.setOnClickListener {
            if (validateFields()) {
                saveFriendPoint(getFriendPointData())
                Toast.makeText(
                    mBinding.root.context,
                    "Friend point guardado correctamente!",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToHome(mBinding.root)
            }

        }
    }

    private fun navigateToHome(it: View) {
            val action = FpCreatorFragmentDirections.actionFpCreatorFragmentToHomeFragment()
            Navigation.findNavController(it).navigate(action)
    }


    private fun validateFields(): Boolean {
        var correct = true
        if (mBinding.createEtPlan.text.isNullOrEmpty()
            || mBinding.createEtAficiones.text.isNullOrEmpty()
            || mBinding.createEtDescripcion.text.isNullOrEmpty()
            || mBinding.createEtMaxIntegrantes.text.isNullOrEmpty())
        {
            Toast.makeText(
                mBinding.root.context,
                "Faltan campos por rellenar",
                Toast.LENGTH_SHORT
            ).show()
            correct = false
        }
        return correct
    }
    

    private fun saveFriendPoint(friendPointModel: FriendPointModel): String {
        var fpRepo = FriendPointsRepository()
        var userRepository = UsersRepository()
        var authRepo = AuthRepository()
        var fpUid = ""
        GlobalScope.launch(Dispatchers.IO) {
            fpUid = fpRepo.addFp(friendPointModel) ?: ""
            uploadImageViewPhoto(fpUid)
            fpRepo.addAdmin(fpUid, authRepo.currentUser!!.uid)
            userRepository.userAdminAddedToFp(authRepo.currentUser!!.uid, fpUid)
        }
        return fpUid
    }


    fun getFriendPointData(): FriendPointModel {
        var friendPoint = FriendPointModel()
        friendPoint.plan = mBinding.createEtPlan.text.toString()
        friendPoint.descripcion = mBinding.createEtDescripcion.text.toString()
        friendPoint.aficiones = setAficiones()
        friendPoint.maxNumeroMiembros =
            Integer.parseInt(mBinding.createEtMaxIntegrantes.text.toString()).or(0)
        friendPoint.photoUrl = imageUri.toString()
        return friendPoint
    }

    private fun uploadImageViewPhoto(fpUid: String) {
        var fpRepo = FriendPointsRepository()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                fpRepo.uploadFpImage(fpUid, imageUri!!)
                var externalPhotoUri = fpRepo.getFpImage(fpUid)
                updateFpPhoto(fpUid, externalPhotoUri!!)
            } catch (e: Exception) {
                Log.e("FpCreatorFragment", "error imagen")
            }
        }
    }

    private fun updateFpPhoto(fpUid: String, externalUri: String) {
        var fpRepo = FriendPointsRepository()
        fpRepo.updateFpImage(fpUid, externalUri)
    }

    /**
     * Precondiciones: Que el usuario haya introducido las aficiones separadas por comas
     * Obtiene las aficiones introducidas por el usuario y las transforma en un arraylist
     * returns: arraylist con todas las aficiones introducidas
     */
    fun setAficiones(): ArrayList<String> {
        val list: List<String> = mBinding.createEtAficiones.text.split(",").toList()
        return ArrayList(list)
    }


    private fun pickImageGalery() {
        val gallery = Intent()
        gallery.action = Intent.ACTION_GET_CONTENT
        gallery.type = "image/*"
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), pickImage);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data ?: null
            mBinding.createIbPhoto.setImageURI(imageUri)
        }
    }

}