package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentHomeBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.ui.Adapter.ExploreFriendPointRecyclerAdapter
import com.example.tfg_friendpoint.ui.Adapter.HomeFriendPointRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    private var fpAdapter: HomeFriendPointRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = mBinding.root
        setupRecyclerView()
        mBinding.homeFbAdd.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_home_fragment_to_fpCreatorFragment)
        }
        return view
    }

    private fun setupRecyclerView() {
        fpAdapter  = getAdapter()
        mBinding.rvHomeFriendPoints.layoutManager = LinearLayoutManager(activity)
        mBinding.rvHomeFriendPoints.adapter = fpAdapter

    }

    private fun getAdapter() :  HomeFriendPointRecyclerAdapter{
        var fpRepo = FriendPointsRepository()
        var authRepo = AuthRepository()
        var query = fpRepo.getManagedFpOfUser(authRepo.currentUser!!.uid)
        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<FriendPointModel>()
            .setQuery(query, FriendPointModel::class.java)
            .build()
        return HomeFriendPointRecyclerAdapter(firestoreRecyclerOptions)
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