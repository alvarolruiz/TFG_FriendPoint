package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.databinding.FragmentExploreBinding
import com.example.tfg_friendpoint.ui.Adapter.ExploreFriendPointRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExploreFragment : Fragment() {

    private lateinit var mBinding: FragmentExploreBinding
    private val db = Firebase.firestore
    private val fpCollectionReference: CollectionReference = db.collection("FriendPoints")
    private var fpAdapter: ExploreFriendPointRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentExploreBinding.inflate(inflater, container, false)
        val view = mBinding.root
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        fpAdapter  = getAdapter()
        mBinding.rvExploreFriendPoints.layoutManager = LinearLayoutManager(activity)
        mBinding.rvExploreFriendPoints.adapter = fpAdapter

    }

    private fun getAdapter() :  ExploreFriendPointRecyclerAdapter{
        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<FriendPointModel>()
                .setQuery(fpCollectionReference, FriendPointModel::class.java)
                .build()
        return ExploreFriendPointRecyclerAdapter(firestoreRecyclerOptions)
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