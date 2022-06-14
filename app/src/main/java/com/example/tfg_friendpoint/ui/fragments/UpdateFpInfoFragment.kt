package com.example.tfg_friendpoint.ui.fragments

import android.app.Activity.RESULT_OK
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.databinding.FragmentUpdateFpInfoBinding
import com.example.tfg_friendpoint.repository.FriendPointsRepository
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
    private var imageUri: Uri? = null
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
            navigateToHome(mBinding.root)
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
            if (imageUri != null) {
                fpRepo.uploadFpImage(fpUid, imageUri!!)
                currentFp.photoUrl = fpRepo.getFpImage(fpUid)
            }
            fpRepo.updateFp(fpUid, currentFp)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    mBinding.root.context,
                    "Friend point actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun navigateToHome(it : View) {
        val action = UpdateFpInfoFragmentDirections.actionUpdateFpInfoToHomeFragment()
        Navigation.findNavController(it).navigate(action)
    }

    //Not implemented
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
        Glide.with(mBinding.root).load(currentFp!!.photoUrl).into(mBinding.fpUpdateIvPhoto)
    }

    private fun pickImageGalery() {
        val gallery = Intent()
        gallery.action = Intent.ACTION_OPEN_DOCUMENT
        gallery.type = "image/*"
        gallery.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        gallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), pickImage);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            Glide.with(mBinding.root).load(imageUri.toString()).into(mBinding.fpUpdateIvPhoto)
        }
    }


}