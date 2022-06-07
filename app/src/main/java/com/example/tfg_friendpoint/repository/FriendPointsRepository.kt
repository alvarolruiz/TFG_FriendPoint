package com.example.tfg_friendpoint.repository

import android.net.Uri
import android.util.Log
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

//casi completa
class FriendPointsRepository {

    private val db = Firebase.firestore
    private val rootElement =  "FriendPoints"
    private val fpCollection = db.collection("FriendPoints")
    private val storageRef = FirebaseStorage.getInstance().reference


    suspend fun getFpImage(fpUid : String) : String{
        val fpImagesReference = storageRef.child("${rootElement}/${fpUid!!}")
        return fpImagesReference.downloadUrl.await().toString()

    }

    fun uploadUserImage(fpUid: String, localPhotoUri: Uri) {
        val fpImageReference = storageRef.child("${rootElement}/${fpUid!!}")
        fpImageReference.putFile(localPhotoUri)
            .addOnSuccessListener {
                Log.e("Storage", "Foto subida correctamente")
            }
    }

    suspend fun getUserImage(userUid: String) : String {
        val userImagesReference = storageRef.child(userUid)
        return userImagesReference.downloadUrl.await().toString()
    }

    suspend fun addFp (childName: String, friendPointModel: FriendPointModel): String? {
        return try {
            val fpUid = fpCollection
                .add(friendPointModel).addOnCompleteListener{
                    Log.i("FpRepository", "New Friendpoint added with id ${it.result.id}")
                }.await().id
            return fpUid
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun updateFp(docUid: String, friendPointModel: FriendPointModel
    ): Boolean {
        return try {
            val data = fpCollection
                .document(docUid)
                .set(friendPointModel).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun getFpData (docUid: String): FriendPointModel? {
        return fpCollection.document(docUid).get().await().toObject<FriendPointModel>(FriendPointModel::class.java)
    }

    fun getFpOfUserQuery (uid : String) : Query {
        return fpCollection.whereArrayContains("miembros", uid)
    }

}





