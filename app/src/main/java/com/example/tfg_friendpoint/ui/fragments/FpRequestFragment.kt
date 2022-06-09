package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.databinding.FragmentFpRequestBinding
import com.example.tfg_friendpoint.repository.FpRequestRepository
import com.example.tfg_friendpoint.ui.Adapter.ExploreFriendPointRecyclerAdapter
import com.example.tfg_friendpoint.ui.Adapter.RecibedFpRequestRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class FpRequestFragment : Fragment() {
    private lateinit var mBinding: FragmentFpRequestBinding
    private var fpRecibedRequestAdapter: RecibedFpRequestRecyclerAdapter? = null
    private val args: FpRequestFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFpRequestBinding.inflate(inflater, container, false)
        val view = mBinding.root
        setupRecyclerView()
        return view
    }

    //
    private fun setupRecyclerView() {
        fpRecibedRequestAdapter = getAdapter()
        mBinding.fpRequestRv.layoutManager = LinearLayoutManager (activity)
        mBinding.fpRequestRv.adapter = fpRecibedRequestAdapter
    }

    private fun getAdapter() : RecibedFpRequestRecyclerAdapter {
        val fpRequestRepo = FpRequestRepository(args.fpUid)
        val recibedRequestQuery = fpRequestRepo.getRecibedRequestQuery()
        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<RequestModel>()
            .setQuery(recibedRequestQuery, RequestModel::class.java)
            .build()
        return RecibedFpRequestRecyclerAdapter(firestoreRecyclerOptions)
    }


    override fun onStart() {
        super.onStart()
        fpRecibedRequestAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        fpRecibedRequestAdapter!!.startListening()
    }

    override fun onPause() {
        super.onPause()
        fpRecibedRequestAdapter!!.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        fpRecibedRequestAdapter!!.stopListening()
    }


}