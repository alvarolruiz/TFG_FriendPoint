package com.example.tfg_friendpoint.repository

import android.util.Log
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class FpRequestRepository(private val fpUid: String) {
    private val db = Firebase.firestore
    private val rootCollection = "FriendPoints"
    private val sentCollection =
        db.collection(rootCollection).document(fpUid).collection("sentRequest")
    private val recibedCollection =
        db.collection(rootCollection).document(fpUid).collection("recibedRequest")

    suspend fun isValidSentRequest(requestUid: String, userUid: String): Boolean {
           return sentCollection.whereEqualTo("fromUid", userUid).get().await().isEmpty
    }

    fun getSentRequestQuery(): Query {
        return sentCollection
    }

    fun getRecibedRequestQuery(): Query {
        return recibedCollection.whereEqualTo("resolved", false)
    }

    fun getUserPendingRecibedRequest(): Query {
        return recibedCollection.whereEqualTo("resolved", false)
    }

    fun getUserPendingSentRequest(): Query {
        return sentCollection.whereEqualTo("resolved", false)
    }

    fun sendRequestToUser(request: RequestModel): String {
        var userRequestUid: String = ""

        GlobalScope.launch(Dispatchers.IO) {
            userRequestUid = sentCollection.add(request)
                .addOnCompleteListener {
                    if (it.result.id != null) {
                        Log.e("fpRequestSent", it.result.id)
                    }
                }.await().id
        }
        return userRequestUid
    }

    fun recibeRequestFromUser(requestUid: String, request: RequestModel): Unit {
        recibedCollection.document(requestUid).set(request)
            .addOnCompleteListener { Log.e("fpRequestRecibed", requestUid) }
    }

    fun accepRecibedRequest(requestUid: String) {
        Log.e("requestRepo", requestUid)
        recibedCollection.document(requestUid).update("resolved", true)
        recibedCollection.document(requestUid).update("accepted", true)
    }

    fun denyRecibedRequest(requestUid: String) {
        recibedCollection.document(requestUid).update("resolved", true)
        recibedCollection.document(requestUid).update("accepted", false)
    }

    fun sentRequestAccepted(requestUid: String) {
        sentCollection.document(requestUid).update("resolved", true)
        sentCollection.document(requestUid).update("accepted", true)
    }

    fun sentRequestDenied(requestUid: String) {
        sentCollection.document(requestUid).update("resolved", true)
        sentCollection.document(requestUid).update("accepted", true)
    }


}