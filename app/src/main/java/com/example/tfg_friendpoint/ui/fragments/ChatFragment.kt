package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
private lateinit var mBinding : FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentChatBinding.inflate(inflater,container,false)
        val view = mBinding.root
        return view
    }

}