package com.example.tfg_friendpoint.ui.model

import android.provider.ContactsContract
import com.google.firebase.firestore.ServerTimestamp

class MessageModel (body : String, nickname: String, photoUri : String){
    @ServerTimestamp
    val sendTime = null
    var nickname = nickname
    var photoUri = photoUri
    var body = body
}