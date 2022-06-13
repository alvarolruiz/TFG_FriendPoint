package com.example.tfg_friendpoint.repository

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.example.tfg_friendpoint.ui.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException

class UsersRepository() {
    var db = Firebase.firestore
    var rootCollection = "users"
    var usersReference = db.collection(rootCollection)
    val storageRef = FirebaseStorage.getInstance().reference.child("UserImages")

    suspend fun saveUserData(userUid: String, userModel: UserModel) {
        usersReference.document(userUid).set(userModel)
            .addOnSuccessListener {
                Log.i("UsersRepository", "User data succesfuly saved")
            }
            .addOnFailureListener {
                Log.i("UsersRepository", "User data couldn't be saved")
            }.await()
    }

    suspend fun uploadUserImage(userUid: String, localPhotoUri: Uri) {
        val userImagesReference = storageRef.child(userUid!!)
        try{
            userImagesReference.putFile(localPhotoUri).addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                        Log.i("PhotoUrl", downloadUrl.toString())
                    }
            }.await()
        }catch (e :Exception){
            null
        }
    }

    suspend fun getUserImage(userUid: String): String {
        val userImagesReference = storageRef.child(userUid)
        return userImagesReference.downloadUrl.addOnCompleteListener {
            Log.e("UsersStorage", it.result.path.toString())
        }.await().toString()
    }

    suspend fun getUser(uid: String): UserModel? {
        var currentUser: UserModel? = null
            currentUser = usersReference.document(uid).get().await()
                .toObject(UserModel::class.java)
        return currentUser
    }

    suspend fun getUserNickName(userUid: String): String {
        var nickname = usersReference.document(userUid).get().addOnCompleteListener {
            Log.e("UsersRepository", it.result.id)
        }.await().get("nickname").toString()
        return nickname
    }

    fun getAllUsersQuery(): Query {
        return usersReference
    }

    fun userAdminAddedToFp(userUid: String, fpUid:String){
        usersReference.document(userUid).update("AdminOf", FieldValue.arrayUnion(fpUid))
        userAddedToFp(userUid,fpUid)
    }

    fun userAddedToFp(userUid: String, fpUid:String){
        usersReference.document(userUid).update("MemberOf", FieldValue.arrayUnion(fpUid))
    }

    fun getUserMembersOfFp (fpUid:String) : Query{
        return usersReference.whereArrayContains("MemberOf", fpUid)
    }

    fun getUserAdminsOfFp (fpUid:String) : Query{
        return usersReference.whereArrayContains("AdminOf", fpUid)
    }

    fun updateUserImage(userUid: String, externalUri :String){
            usersReference.document(userUid).update("photoUrl", externalUri)
    }


    /* fun getMembersOfFp(fpUid: String): Query {
 // O obtener todos los usuarios miembros en un array y conseguir hacer una consulta con ellos o
         // O obtener todos los usuarios que contengan al fp en concreto
     }*/

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
