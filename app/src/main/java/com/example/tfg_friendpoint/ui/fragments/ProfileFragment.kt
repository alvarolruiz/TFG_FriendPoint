package com.example.tfg_friendpoint.ui.fragments

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentProfileBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.activity.AuthActivity
import com.example.tfg_friendpoint.ui.activity.MainActivity
import com.example.tfg_friendpoint.ui.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {
    private lateinit var mBinding: FragmentProfileBinding

    lateinit var mAuth: FirebaseAuth
    lateinit var loggedUser: FirebaseUser
    var userLoggedIn: UserModel? = UserModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        var view = mBinding.root
        getUserData()
        setupButtons()

        return view
    }

    private fun getUserData() {
        var userRepo = UsersRepository()
        var authRepo = AuthRepository()
        try{
            GlobalScope.launch(Dispatchers.IO) {
                userLoggedIn = userRepo.getUser(authRepo.currentUser!!.uid)
                withContext(Dispatchers.Main) {
                    updateUi()
                }
            }
        }catch (e : Exception){
            Log.e(TAG, e.localizedMessage)
        }

    }

    private fun updateUi() {
        mBinding.profileTvNickname.text = userLoggedIn!!.nickName ?: "Default"
        Glide.with(mBinding.root).load(userLoggedIn!!.photoUrl).into(mBinding.profileCivPhoto)
        mBinding.profileTvEdad.text = userLoggedIn!!.getEdad().toString() + " Años"
    }

    private fun setupButtons() {
        setupSignOutButton()
        setupEditProfileButton()
        setupInstagramButton()
    }

    private fun setupSignOutButton() {
        mBinding.profileIbLogOut.setOnClickListener{
            signOutUser()
        }

    }

    private fun signOutUser() {
        var authRepo = AuthRepository()
        authRepo.logOut()
        showAuthActivityIntent()
    }

    private fun setupInstagramButton() {
        mBinding.profileIbInstagram.setOnClickListener {
            instagramIntent()
        }

    }

    private fun instagramIntent() {
        val uri: Uri = Uri.parse("http://www.instagram.com/alvarolruiz")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        likeIng.setPackage("com.instagram.android")
        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(uri.toString())
                )
            )
        }
    }

    private fun setupEditProfileButton() {
        mBinding.profileFbEdit.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profile_fragment_to_fragmentEditProfile)
        }
    }

    private fun showAuthActivityIntent(){
        val mainActivityIntent = Intent(mBinding.root.context, AuthActivity::class.java).apply {
        }
        startActivity(mainActivityIntent)
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