package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentDetailsBinding
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.ui.dialog.FriendshipRequestDialog
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class DetailsFragment : Fragment() {
    private lateinit var mBinding: FragmentDetailsBinding
    private lateinit var repository: FriendPointsRepository
    private val args: DetailsFragmentArgs by navArgs()
    private var currentFriendPointModel: FriendPointModel? = FriendPointModel()
    private val collectionName: String = "FriendPoints"
    private val dbReference = Firebase.firestore.collection(collectionName)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = mBinding.root
        updateFpData()
        setupButtons()
        return view
    }

    private fun setupButtons() {
        setupSendRequestButton()
        setupSeeMembersButton()
    }

    private fun updateFpData() {
        GlobalScope.launch(Dispatchers.IO) {
            var fpRepo = FriendPointsRepository()
            currentFriendPointModel = fpRepo.getFpData(args?.uid.toString())
            withContext(Dispatchers.Main) {
                if (currentFriendPointModel != null) updateUI()
            }
        }

    }

    private fun setupSeeMembersButton() {
        mBinding.detailsLlMembers.setOnClickListener {
            navigateToMembersFragment()
        }
    }

    private fun setupSendRequestButton() {
        mBinding.detailsLlBtnRequest.setOnClickListener {
            navigateToRequestDialog()

        }

    }

    private fun navigateToMembersFragment() {
        val action = DetailsFragmentDirections.actionDetailsFragmentToFpMembersFrament(args.uid!!)
        findNavController().navigate(action)
    }

    private fun navigateToRequestDialog() {
        val action = DetailsFragmentDirections.actionDetailsFragmentToFriendshipRequestDialog(args.uid!!)
        findNavController().navigate(action)
    }

    private fun updateUI() {
        Glide.with(mBinding.root).load(currentFriendPointModel!!.photoUrl)
            .into(mBinding.detailsIvPhoto)
        mBinding.detailsTvPlan.text = currentFriendPointModel!!.plan
        mBinding.detailsTvDescripcion.text = currentFriendPointModel!!.descripcion
        //mBinding.detailsTvDescripcion.text = currentFriendPointModel!!.getEdadmedia().toString()
    }
}