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
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tfg_friendpoint.databinding.FragmentUpdateFpInfoBinding
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.ui.activity.mAuth
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.example.tfg_friendpoint.ui.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UpdateFpInfoFragment : Fragment() {

    private lateinit var mBinding: FragmentUpdateFpInfoBinding
    private val args: UpdateFpInfoFragmentArgs by navArgs()
    private lateinit var currentFp: FriendPointModel
    private var fpRepo = FriendPointsRepository()
    private val pickImage = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentUpdateFpInfoBinding.inflate(inflater, container, false)
        var view = mBinding.root
        getFpData(args.uid)
        setupViewButtons()

        return view
    }

    private fun setupViewButtons() {
        setupRequestButton()
        setupInviteMembersButton()
        setupChangePhotoButton()
        setupSaveButton()
    }

    private fun setupSaveButton() {
        mBinding.fpUpdateFbUpdate.setOnClickListener {
            setCurrentFpWithUpdateFields()
            updateFp(args.uid)
            navigateToHome()
        }
    }

    private fun setCurrentFpWithUpdateFields() {
        currentFp.plan = mBinding.fpUpdateEtPlan.text.toString()
        currentFp.descripcion = mBinding.fpUpdateEtDescripcion.text.toString()
        val aficionesString = mBinding.fpUpdateEtAficiones.text.toString()
        currentFp.aficiones = aficionesString.split(",").toList()
    }

    private fun updateFp(fpUid: String) {
        val fpRepo = FriendPointsRepository()
        GlobalScope.launch(Dispatchers.IO) {
            fpRepo.updateFp(fpUid, currentFp)
            withContext(Dispatchers.Main){
            }
        }
       Toast.makeText(mBinding.root.context,"sdds",Toast.LENGTH_SHORT)

    }

    private fun navigateToHome() {
    }

    private fun setupInviteMembersButton() {
    }

    private fun setupChangePhotoButton() {
        mBinding.fpUpdateIbFoto.setOnClickListener {
            pickImageGalery()
        }
    }

    private fun setupRequestButton() {
        mBinding.fpUpdateIbRequest.setOnClickListener {
            navigateToRequest(args.uid)
        }
    }

    private fun navigateToRequest(uid: String) {
        val action = UpdateFpInfoFragmentDirections.actionUpdateFpInfoToFpRequestFragment(args.uid)
        findNavController().navigate(action)
    }

    /**
    Gets the data of the friendPoint with a given fp Uid.
     */
    private fun getFpData(fpUid: String) {
        GlobalScope.launch(Dispatchers.IO) {
            Log.e("updatefp", fpUid)
            currentFp = fpRepo.getFpData(fpUid)!!
            withContext(Dispatchers.Main) {
                updateView()
            }
        }
    }

    private fun updateView() {
        mBinding.fpUpdateEtPlan.setText(currentFp.plan)
        mBinding.fpUpdateEtDescripcion.setText(currentFp.descripcion)
        mBinding.fpUpdateEtDescripcion.setText(currentFp.descripcion)
        var aficionesString = currentFp.aficiones.joinToString(",") { it }
        mBinding.fpUpdateEtAficiones.setText(aficionesString)
        mBinding.fpUpdateIvPhoto.setImageURI(currentFp.photoUrl.toUri())
    }

    private fun pickImageGalery() {
        val gallery = Intent()
        gallery.action = Intent.ACTION_GET_CONTENT
        gallery.type = "image/*"
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), pickImage);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageUri: Uri?
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            mBinding.fpUpdateIvPhoto.setImageURI(imageUri)
        }
    }


}