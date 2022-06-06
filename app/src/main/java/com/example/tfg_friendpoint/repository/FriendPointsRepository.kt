package com.example.tfg_friendpoint.repository

import android.util.Log
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FriendPointsRepository {
    val collectionName: String = "FriendPoints"
    val dbReference = Firebase.firestore.collection(collectionName)


    suspend fun saveData(childName: String, friendPointModel: FriendPointModel): Boolean {
        return try {
            val data = dbReference
                .add(friendPointModel)
                .await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun updateData(
        docUid: String,
        friendPointModel: FriendPointModel
    ): Boolean {
        return try {
            val data = dbReference
                .document(docUid)
                .set(friendPointModel)
                .await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun getData(docUid: String): DocumentSnapshot {

        return dbReference.document(docUid).get().await()
    }

}





