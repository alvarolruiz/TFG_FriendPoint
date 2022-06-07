package com.example.tfg_friendpoint.ui.model

import android.os.Message
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class RequestModel(
    from: String = "",
    to: String = "",
    msg: String = ""
) {
    @ServerTimestamp
    val createdAt: Date? = null
    val message: String = msg
    var fromUid: String = from
    var toUid: String = to
    var resolved: Boolean = false
    var accepted: Boolean = false

    fun getFromUser(): UserModel {
        lateinit var user: UserModel
        GlobalScope.launch(Dispatchers.IO) {
            user = UsersRepository().getUser(fromUid)
        }
        return user
    }
}
