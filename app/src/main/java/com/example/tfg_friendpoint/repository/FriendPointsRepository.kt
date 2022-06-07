package com.example.tfg_friendpoint.repository

import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

//casi completa
class FriendPointsRepository {

    private val db = Firebase.firestore
    private val fpCollection = db.collection("FriendPoints")

    fun addFp(childName: String, friendPointModel: FriendPointModel): Boolean {
        return try {
            val data = fpCollection
                .add(friendPointModel)
            return true
        } catch (e: Exception) {
            return false
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





