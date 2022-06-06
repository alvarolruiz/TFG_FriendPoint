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
import com.example.tfg_friendpoint.databinding.FragmentEditProfileBinding
import com.example.tfg_friendpoint.databinding.FragmentProfileBinding
import com.example.tfg_friendpoint.ui.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.ref.PhantomReference


class ProfileFragment : Fragment() {
    private lateinit var mBinding : FragmentProfileBinding

    lateinit var mAuth: FirebaseAuth
    lateinit var loggedUser : FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentProfileBinding.inflate(inflater,container,false)
        var view  = mBinding.root
        mBinding.profileFbEdit.setOnClickListener{
            Navigation.findNavController(it)
                .navigate(R.id.action_profile_fragment_to_fragmentEditProfile)
        }
        return view
    }

    /*private fun getUserData() {
        var user : UserModel?
        dbReference.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    user = document.toObject<UserModel>() ?: UserModel()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }*/
}