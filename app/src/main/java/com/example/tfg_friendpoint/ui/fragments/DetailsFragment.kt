package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {
private lateinit var mBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDetailsBinding.inflate(inflater,container,false)
        val view = mBinding.root

        mBinding.detailsTvAficiones.text = "karaoke, etx"
        return view
    }


}