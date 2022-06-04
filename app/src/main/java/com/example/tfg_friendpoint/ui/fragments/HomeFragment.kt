package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.databinding.FragmentHomeBinding
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.ui.Adapter.ExploreFriendPointRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    val db = Firebase.firestore
    val fpCollectionReference: CollectionReference = db.collection("FriendPoints")
    var fpAdapter: ExploreFriendPointRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = mBinding.root
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {

        val query: Query = fpCollectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<FriendPointModel> =
            FirestoreRecyclerOptions.Builder<FriendPointModel>()
                .setQuery(query, FriendPointModel::class.java)
                .build()
        fpAdapter = ExploreFriendPointRecyclerAdapter(firestoreRecyclerOptions)
        mBinding.rvFriendPoints.layoutManager = LinearLayoutManager(activity)
        mBinding.rvFriendPoints.adapter = fpAdapter

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