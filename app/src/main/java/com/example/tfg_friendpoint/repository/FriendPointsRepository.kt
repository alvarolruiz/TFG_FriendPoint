package com.example.tfg_friendpoint.repository

import android.net.Uri
import android.util.Log
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//casi completa
class FriendPointsRepository {

    private val db = Firebase.firestore
    private val rootElement = "FriendPoints"
    private val fpCollection = db.collection(rootElement)
    private val storageRef = FirebaseStorage.getInstance().reference


    suspend fun getFpImage(fpUid: String): String {
        val fpImageReference = storageRef.child("${rootElement}/${fpUid!!}")
        return fpImageReference.downloadUrl.addOnCompleteListener {
            Log.i("FpStorage", it.result.toString())
        }.await().toString()
    }

    suspend fun uploadFpImage(fpUid: String, localPhotoUri: Uri) {
        val fpImageReference = storageRef.child("${rootElement}/${fpUid!!}")
        try{
            fpImageReference.putFile(localPhotoUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                    Log.i("PhotoUrl", downloadUrl.toString())
                }
            }.await()
        }catch (e :Exception){
            null
        }
    }

    suspend fun addFp(friendPointModel: FriendPointModel): String? {
        return try {
            val fpUid = fpCollection.add(friendPointModel)
                .addOnCompleteListener {
                    Log.i("FpRepository", "New Friendpoint added with id ${it.result.id}")
                }.await().id
            return fpUid
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun updateFp(fpUid: String, friendPointModel: FriendPointModel): Boolean {
        return try {
            val data = fpCollection
                .document(fpUid)
                .set(friendPointModel).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun getFpData(fpUid: String): FriendPointModel? {
        var currentFp: FriendPointModel? = null
        currentFp = fpCollection.document(fpUid).get()
            .await().toObject(FriendPointModel::class.java)
        return currentFp
    }

    fun getFpOfUserQuery(uid: String): Query {
        return fpCollection.whereArrayContains("miembros", uid)
    }

    fun updateFpImage(fpUid: String, externalUri: String) {
        fpCollection.document(fpUid).update("photoUrl", externalUri)
    }
}







