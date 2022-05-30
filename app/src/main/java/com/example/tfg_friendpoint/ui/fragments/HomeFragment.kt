package com.example.tfg_friendpoint.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentDetailsBinding
import com.example.tfg_friendpoint.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater,container,false)
        val view =  mBinding.root
        mBinding.btnDetails.setOnClickListener {
            Log.e(TAG, "btnclicked")
            Navigation.findNavController(view).navigate(R.id.action_home_fragment_to_detailsFragment)
        }
        return view
    }


}