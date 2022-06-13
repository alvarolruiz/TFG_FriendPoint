package com.example.tfg_friendpoint.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.example.tfg_friendpoint.ui.model.UserModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//casi completa
class FriendPointsRepository {

    private val db = Firebase.firestore
    private val rootElement = "FriendPoints"
    private val fpCollection = db.collection(rootElement)
    private val storageRef = FirebaseStorage.getInstance().reference

    /*suspend fun getMembersList(fpUid: String): ArrayList<String> {
        var docSnapshot = fpCollection.document(fpUid).get().await()
        val list = docSnapshot.get("ip_range") as ArrayList<String>
        Log.d(TAG, list.toString())
        return list
    }

    suspend fun getMembersSize(fpUid: String): Int {
        var membersList = getMembersList(fpUid)
        return membersList.size.or(0)
    }

    suspend fun getAverageAge(fpUid: String): Int {
        var result = 0
        var usersRepo = UsersRepository()
        GlobalScope.launch(Dispatchers.IO) {
            var miembros = getMembersList(fpUid)
            if (miembros.isNotEmpty()) {
                var sumaEdad = 0
                var user: UserModel?
                miembros.forEach { userUid ->
                    user = usersRepo.getUser(userUid.toString())
                    sumaEdad += user?.getEdad() ?: 0
                    result = sumaEdad / miembros.size
                }
            }
        }
        return result.or(0)
    }*/

        suspend fun getFpImage(fpUid: String): String {
            val fpImageReference = storageRef.child("${rootElement}/${fpUid!!}")
            return fpImageReference.downloadUrl.addOnCompleteListener {
                Log.i("FpStorage", it.result.toString())
            }.await().toString()
        }


        suspend fun uploadFpImage(fpUid: String, localPhotoUri: Uri) {
            val fpImageReference = storageRef.child("${rootElement}/${fpUid!!}")
            try {
                fpImageReference.putFile(localPhotoUri).addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                        Log.i("PhotoUrl", downloadUrl.toString())
                    }
                }.await()
            } catch (e: Exception) {
                null
            }
        }

        suspend fun addFp(friendPointModel: FriendPointModel): String? {
            return try {
                val fpUid = fpCollection.add(friendPointModel)
                    .addOnCompleteListener {
                        Log.i("FpRepository", "New Friendpoint added with id ${it.result.id}")
                    }.await().id
                return fpUid
            } catch (e: Exception) {
                return null
            }
        }

        fun addAdmin(fpUid: String, userUid: String) {
            addMember(fpUid, userUid)
            fpCollection.document(fpUid).update("Admins", FieldValue.arrayUnion(userUid))
        }

        suspend fun updateFp(fpUid: String, friendPointModel: FriendPointModel): Boolean {
            return try {
                val data = fpCollection
                    .document(fpUid)
                    .set(friendPointModel).await()
                return true
            } catch (e: Exception) {
                return false
            }
        }

        suspend fun getFpData(fpUid: String): FriendPointModel? {
            var currentFp: FriendPointModel? = null
            currentFp = fpCollection.document(fpUid).get()
                .await().toObject(FriendPointModel::class.java)
            return currentFp
        }

        fun addMember(fpUid: String, userToAddUid: String) {
            fpCollection.document(fpUid).update("Members", FieldValue.arrayUnion(userToAddUid))
        }

        fun getFpOfUserQuery(uid: String): Query {
            return fpCollection.whereArrayContains("Members", uid)
        }

        fun getManagedFpOfUser(userUid: String): Query {
            return fpCollection.whereArrayContains("Admins", userUid)
        }

        fun updateFpImage(fpUid: String, externalUri: String) {
            fpCollection.document(fpUid).update("photoUrl", externalUri)
        }
    }







