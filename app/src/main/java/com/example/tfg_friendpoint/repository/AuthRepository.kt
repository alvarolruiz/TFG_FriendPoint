package com.example.tfg_friendpoint.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User

class AuthRepository {

        val mAuth  = FirebaseAuth.getInstance()
        var currentUserUid  = mAuth.currentUser!!.uid

}