package com.example.tfg_friendpoint.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.tasks.await

class AuthRepository {
    val mAuth = FirebaseAuth.getInstance()
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

    fun sendEmailToRestorePassword(email: String){
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("mAuthRepo", "Email sent.")
                }
            }
    }

    fun logOut(){
        mAuth.signOut()
    }
}

