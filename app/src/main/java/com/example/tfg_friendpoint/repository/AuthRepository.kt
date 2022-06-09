package com.example.tfg_friendpoint.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser

    suspend fun signInWithEmailAndPassword(email: String, contraseña: String): String? {
        return try {
            mAuth.signInWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("AuthRepo", "User with uid ${it.result.user?.uid}")
                    } else {
                        Log.i("AuthRepo", "Login Failed")
                    }
                }.await().user!!.uid
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun registerWithEmailAndPassword(email: String, contraseña: String): String? {
        return try {
            mAuth.createUserWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener {
                    Log.i("AuthRepo", "User with uid ${it.result.user?.uid} succesfully registered")

                }
                .addOnFailureListener {
                    Log.i("AuthRepo", "User register failed")
                }.await().user!!.uid
        } catch (e: Exception) {
            return null
        }
    }
}
/*
    fun registerUserCredentials(email: String?, contraseña: String?): String? {
        //TODO: Crear repo para auth e implementar corrutinas
        var userUid = ""
        if (!email.isNullOrBlank() && !contraseña.isNullOrBlank()) {
            mAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener {
                userUid = it.result.user?.uid.toString()
                showSuccessfulRegisterToast()
                saveUserData(it.result.user!!.uid)
                //TODO Cambiar la forma de subir la imagen. Sustituir repo
                //uploadImage(it.result.user!!.uid, imageUri)
                showAuthActivity()
                Log.d("Main", "uid: ${it.result.user?.uid}")
            }
        } else {
            showMissingCredentialsAlert()
        }
        return userUid;
    }*/
