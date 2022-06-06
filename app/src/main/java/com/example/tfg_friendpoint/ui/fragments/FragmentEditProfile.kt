package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentEditProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentEditProfile : Fragment() {

    private lateinit var loggedUser: FirebaseUser
    lateinit var mAuth: FirebaseAuth

    private lateinit var mBinding: FragmentEditProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = mBinding.root
        return view
    }


}