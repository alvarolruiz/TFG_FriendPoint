package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentChatBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.UserFpRepository
import com.example.tfg_friendpoint.ui.Adapter.ExploreFriendPointRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {
    private val authRepo = AuthRepository()
    private lateinit var mBinding: FragmentChatBinding
    private val db = Firebase.firestore
    private val fpCollectionReference  = UserFpRepository().getFpOfUser()

    private var fpAdapter: ExploreFriendPointRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentChatBinding.inflate(inflater, container, false)
        val view = mBinding?.root
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        fpAdapter = getAdapter()
        mBinding.chatsRv.layoutManager = LinearLayoutManager(activity)
        mBinding.chatsRv.adapter = fpAdapter

    }

    private fun getAdapter(): ExploreFriendPointRecyclerAdapter {

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<FriendPointModel>()
            .setQuery(fpCollectionReference, FriendPointModel::class.java)
            .build()
        return ExploreFriendPointRecyclerAdapter(firestoreRecyclerOptions)
    }

    //Implementar que cuando se clicke en un item se vaya a la vista de chat y se muestren los mensajes

    private fun displayChatMesages() {
    }

    override fun onStart() {
        super.onStart()
        fpAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        fpAdapter!!.stopListening()
    }

}