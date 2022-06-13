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
import kotlinx.coroutines.withContext

//Casi completa
class UserRequestRepository(val loggedUserUid: String) {
    private val db = Firebase.firestore
    private val rootCollection = "users"
    private val sentCollection =
        db.collection(rootCollection).document(loggedUserUid).collection("sentRequest")
    private val recibedCollection =
        db.collection(rootCollection).document(loggedUserUid).collection("recibedRequest")

    suspend fun isValidSentRequest(userUid: String): Boolean {
        return sentCollection.whereEqualTo("fromUid", userUid)
            .whereEqualTo("resolved", false)
            .get().await().isEmpty
    }

    fun getSentRequestQuery(): Query {
        return sentCollection
    }

    fun getRecibedRequestQuery(): Query {
        return recibedCollection
    }

    suspend fun sendRequestToFp(request: RequestModel): String {
        Log.e("UsrReq", sentCollection.toString())
        return sentCollection.add(request).await().id
    }


    fun recibeRequestFromFp(requestUid: String, request: RequestModel) {
        recibedCollection.document(requestUid).set(request)
    }

    //Actualiza, actualiza y despues hace un get de la request en cuestión para copiarla exactamente igual en el Fp
    fun accepRecibedRequest(requestUid: String) {
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