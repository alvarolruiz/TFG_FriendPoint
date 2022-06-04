package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentDetailsBinding
import com.example.tfg_friendpoint.repository.FriendPointsRepository
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
    val collectionName: String = "FriendPoints"
    val dbReference = Firebase.firestore.collection(collectionName)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = mBinding.root

        currentFriendPointModel = FriendPointModel()
        repository = FriendPointsRepository()
        Log.e("deta", args?.uid.toString())


        GlobalScope.launch(Dispatchers.IO) {
            val data = dbReference
                .document(args?.uid.toString())
                .get()
                .await()
            currentFriendPointModel = data.toObject(FriendPointModel::class.java)
            withContext(Dispatchers.Main) {
                Glide.with(mBinding.root).load(currentFriendPointModel!!.photoUrl).into(mBinding.detailsIvPhoto)
                mBinding.detailsTvPlan.text = currentFriendPointModel!!.plan
                mBinding.detailsTvDescripcion.text = currentFriendPointModel!!.descripcion
                mBinding.detailsTvDescripcion.text = currentFriendPointModel!!.getEdadmedia().toString()
            }


        /*GlobalScope.launch(Dispatchers.IO){
            var snapshot = repository.getData(args?.uid.toString()).await()
            currentFriendPointModel = snapshot.toObject(FriendPointModel::class.java)
            withContext(Dispatchers.IO) {
                mBinding.detailsTvPlan.text = currentFriendPointModel?.plan
            }*/

        }


        /*
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("uid", null)
            ?.observe(viewLifecycleOwner) { result ->
                Log.e("TAG", result)
            }*/
        mBinding.detailsTvAficiones.text = "karaoke, etx"
        return view
    }


}