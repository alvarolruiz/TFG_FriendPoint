package com.example.tfg_friendpoint.repository

import android.util.Log
import com.example.tfg_friendpoint.ui.activity.mAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserFpRepository {

    val mAuth  = FirebaseAuth.getInstance()
    var currentUserUid  = mAuth.currentUser!!.uid
    var db = Firebase.firestore
    var usersReference = db.collection("FriendPoints")
    var auth = AuthRepository()

    fun getFpOfUser () : Query {
        var s = usersReference.whereArrayContains("miembros", currentUserUid)
        //var arr =  s.data!!.toList().map { "(${it.first}" }
        return s

    }

    fun getFp (){

    }
}