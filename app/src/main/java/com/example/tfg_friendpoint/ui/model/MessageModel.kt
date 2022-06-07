package com.example.tfg_friendpoint.ui.model

import android.net.Uri
import android.provider.ContactsContract
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalTime

class MessageModel (
    var message : String? = null,
    var nickname: String? = null,
    var photoUri : String? = null,
    val sendTime :String?= LocalTime.now().toString()
    ){

    /*constructor{
        this.nickname = nickname
        this.photoUri = photoUri
        this.message = body
    }
    constructor(){}*/
    /*@ServerTimestamp
    val sendTime = null
    var nickname :String = "adda"
    var photoUri :String = "addsa"
    var message :String = "asdsda"*/
}