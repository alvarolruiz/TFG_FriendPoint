package com.example.tfg_friendpoint.repository

import android.util.Log
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.example.tfg_friendpoint.ui.model.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.IOException

class UsersRepository() {
    var db = Firebase.firestore
    var usersReference = db.collection("users")
    var fpReference = db.collection("FriendPoints")
    var auth = Firebase.auth

    suspend fun getUser(uid: String): UserModel {

        var snapshot = usersReference.document(uid).get().await()
        var data = snapshot.toObject(UserModel::class.java)
        return data!!
    }

    @Throws(NullPointerException::class)
    suspend fun sendRequest(fpUid: String, userUid: String, message: String) {
        var request = RequestModel(userUid, fpUid, message)

        var userSendRequestReference = usersReference.document(userUid).collection("sentRequest")
        var fpRecibedRequestReference = fpReference.document(fpUid).collection("recibedRequest")

        var userRequestUid = userSendRequestReference.add(request)
            .addOnCompleteListener {
                if (it.result.id != null) {
                    Log.e("userRequestUid", it.result.id)
                }
            }.await().id
        fpRecibedRequestReference.document(userRequestUid!!).set(request)
            .addOnCompleteListener {
                Log.e("fpRepo", userRequestUid)
            }


    }
}
