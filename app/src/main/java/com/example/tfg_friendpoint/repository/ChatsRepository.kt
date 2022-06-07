package com.example.tfg_friendpoint.repository

import com.example.tfg_friendpoint.ui.model.MessageModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class ChatsRepository(userUid: String, fpUid: String) {
    var userUid = userUid
    var fpUid = fpUid
    val database = Firebase.database
    val myRef = database.getReference("message")

    private val db = Firebase.database
    private val rootReference = "Chats"
    private val fpChatRoomReference = db.getReference(rootReference).child(fpUid)

    fun getChatMessagesList(): ArrayList<MessageModel> {
        var messageList = ArrayList<MessageModel>()
        fpChatRoomReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    for (message in snapshot.children) {
                        messageList.add(message.getValue(MessageModel::class.java)!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return messageList
    }




    suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
        runBlocking {
            delay(time, unit)
        }
    }

    fun getChatRoomReference(): DatabaseReference {
        return fpChatRoomReference
    }

    fun sendMessage(messageModel: MessageModel) {
        fpChatRoomReference.push().setValue(messageModel)
    }
}