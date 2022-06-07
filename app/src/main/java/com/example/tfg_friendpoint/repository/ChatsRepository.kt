package com.example.tfg_friendpoint.repository

import com.example.tfg_friendpoint.ui.model.MessageModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatsRepository(userUid :String, fpUid: String) {
    var userUid = userUid
    var fpUid = fpUid
    val database = Firebase.database
    val myRef = database.getReference("message")

    private val db = Firebase.database("https://friendpoint-2be27.europe-west1.firebasedatabase.app")
    private val rootReference = "Chats"
    private val fpChatRoomReference = db.getReference(rootReference).child(fpUid)

    fun getChatRoomReference (): DatabaseReference{
        return fpChatRoomReference
    }

    fun sendMessage(messageModel: MessageModel) {
        fpChatRoomReference.child(userUid).setValue(messageModel)
    }
}