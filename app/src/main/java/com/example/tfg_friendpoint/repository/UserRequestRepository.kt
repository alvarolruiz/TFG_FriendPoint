package com.example.tfg_friendpoint.repository

import android.util.Log
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

//Casi completa
class UserRequestRepository(val loggedUserUid: String) {
    private val db = Firebase.firestore
    private val rootCollection = "users"
    private val sentCollection = db.collection(rootCollection).document(loggedUserUid).collection("sentRequest")
    private val recibedCollection = db.collection(rootCollection).document(loggedUserUid).collection("recibedRequest")

    //Todo
    fun isValidRecibedRequest(){}

    fun getSentRequestQuery() : Query { return sentCollection }
    fun getRecibedRequestQuery() : Query {return recibedCollection}

    // Cundo se llame a este metodo tb hay que llamar al que guarda un recibido en el fp
    suspend fun sendRequestToFp(request : RequestModel) : String{
        var userRequestUid = sentCollection.add(request)
            .addOnCompleteListener {
                if (it.result.id != null) {
                    Log.e("userRequestUid", it.result.id)
                }
            }.await().id
        return userRequestUid
    }

    fun recibeRequestFromFp(requestUid : String, request: RequestModel ) {
        recibedCollection.document(requestUid).set(request)
    }
    //Actualiza, actualiza y despues hace un get de la request en cuestión para copiarla exactamente igual en el Fp
    fun acceptRequest(requestUid: String) {
        recibedCollection.document(requestUid).update("resolved", true)
        recibedCollection.document(requestUid).update("accepted", true)
    }
    fun denyRequest(requestUid: String) {
        recibedCollection.document(requestUid).update("resolved", true)
        recibedCollection.document(requestUid).update("accepted", false)
    }

}