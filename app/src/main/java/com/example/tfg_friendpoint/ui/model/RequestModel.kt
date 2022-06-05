package com.example.tfg_friendpoint.ui.model

import android.os.Message
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class RequestModel( from: String = "",
                    to: String = "",
                    msg : String = ""
) {
    @ServerTimestamp
    val createdAt: Date? = null
    val message: String = msg
    var fromUid: String = from
    var toUid: String = to
    var resolved: Boolean = false
}