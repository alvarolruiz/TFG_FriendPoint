package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentProfileBinding
import com.example.tfg_friendpoint.databinding.FragmentUpdateFpInfoBinding
import com.example.tfg_friendpoint.repository.FriendPointsRepository


class UpdateFpInfoFragment : Fragment() {

    private lateinit var mBinding : FragmentUpdateFpInfoBinding
    private val args: UpdateFpInfoFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentUpdateFpInfoBinding.inflate(inflater,container,false)
        var view  = mBinding.root
        var fpRepo  = FriendPointsRepository()
        var fp = fpRepo.getFpData(args.uid)
        Log.e("updatefp", fp!!.plan)


        return view
    }

}