package com.example.tfg_friendpoint.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class fpMembersRepository {
    var db = Firebase.firestore
    var usersReference = db.collection("users")
    var fpReference = db.collection("FriendPoints")
    var auth = Firebase.auth
}