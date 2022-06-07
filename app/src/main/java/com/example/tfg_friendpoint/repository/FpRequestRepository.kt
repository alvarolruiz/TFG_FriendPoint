package com.example.tfg_friendpoint.repository

import android.util.Log
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FpRequestRepository (private val fpUid : String){
    private val db = Firebase.firestore
    private val rootCollection = "FriendPoints"
    private val sentCollection = db.collection(rootCollection).document(fpUid).collection("sentRequest")
    private val recibedCollection = db.collection(rootCollection).document(fpUid).collection("recibedRequest")

    //Todo
    fun isValidRecibedRequest(){}

    fun getSentRequestQuery() : Query { return sentCollection }
    fun getRecibedRequestQuery() : Query {return recibedCollection}

    suspend fun sendRequestToUser(request : RequestModel) : String{
        var userRequestUid = sentCollection.add(request)
            .addOnCompleteListener {
                if (it.result.id != null) {
                    Log.e("fpRequestSent", it.result.id)
                }
            }.await().id
        return userRequestUid
    }

    fun recibeRequestFromFp(requestUid : String, request: RequestModel) :Unit {
        recibedCollection.document(requestUid).set(request)
            .addOnCompleteListener {
                Log.e("fpRequestRecibed", requestUid)
            }
    }

    fun acceptRequest(requestUid: String) {
        recibedCollection.document(requestUid).update("resolved", true)
        recibedCollection.document(requestUid).update("accepted", true)
    }
    fun denyRequest(requestUid: String) {
        recibedCollection.document(requestUid).update("resolved", true)
        recibedCollection.document(requestUid).update("accepted", false)
    }


}