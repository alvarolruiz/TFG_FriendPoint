package com.example.tfg_friendpoint.repository

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.example.tfg_friendpoint.ui.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.IOException

class UsersRepository() {
    var db = Firebase.firestore
    var usersReference = db.collection("users")
    val storageRef = FirebaseStorage.getInstance().reference
    var fpReference = db.collection("FriendPoints")


    val mAuth = FirebaseAuth.getInstance()
    var currentUserUid = mAuth.currentUser!!.uid

    fun uploadUserImage(userUid: String, localPhotoUri: Uri) {
        val userImagesReference = storageRef.child("userImages/${userUid!!}")
        userImagesReference.putFile(localPhotoUri)
            .addOnSuccessListener {
                Log.e("Storage", "Foto subida correctamente")
            }
    }

    suspend fun getUserImage(userUid: String) : Uri {
        val userImagesReference = storageRef.child("userImages/${userUid!!}")
        return userImagesReference.downloadUrl.await()
    }

    suspend fun getUser(uid: String): UserModel {
        var snapshot = usersReference.document(uid).get().await()
        var data = snapshot.toObject(UserModel::class.java)
        return data!!
    }

    suspend fun getUserNickName(userUid: String):String{
        var nickname = usersReference.document(userUid).get().addOnCompleteListener {
            Log.e("UsersRepository", it.result.id)
        }.await().get("nickname").toString()
        return nickname
    }

    /*@Throws(NullPointerException::class)
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


    }*/
}
