package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.databinding.FragmentChatBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.ui.Adapter.ChatRecyclerAdapter
import com.example.tfg_friendpoint.ui.Adapter.ExploreFriendPointRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.ChatModel
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {
    private val authRepository = AuthRepository()
    private lateinit var mBinding: FragmentChatBinding
    private val db = Firebase.firestore
    private val fpRepository  = FriendPointsRepository()
    private lateinit var fpAdapter :  ChatRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentChatBinding.inflate(inflater, container, false)
        val view = mBinding?.root


        fpAdapter = getAdapter(fpRepository.getFpOfUserQuery(authRepository.currentUser!!.uid))
        setupRecyclerView(fpAdapter)
        return view
    }

    private fun setupRecyclerView(adapter: ChatRecyclerAdapter) {
        mBinding.chatsRv.layoutManager = LinearLayoutManager(activity)
        mBinding.chatsRv.adapter = adapter

    }

    private fun getAdapter(query : Query): ChatRecyclerAdapter {
        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<ChatModel>()
            .setQuery(query, ChatModel::class.java)
            .build()
        return ChatRecyclerAdapter(firestoreRecyclerOptions)
    }

    //Implementar que cuando se clicke en un item se vaya a la vista de chat y se muestren los mensajes

    private fun displayChatMesages() {
    }

    override fun onStart() {
        super.onStart()
        fpAdapter!!.startListening()
    }

    override fun onPause() {
        super.onPause()
        fpAdapter!!.stopListening()
    }

}