package com.example.tfg_friendpoint.ui.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.repository.FpRequestRepository
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.repository.UserRequestRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.example.tfg_friendpoint.ui.model.UserModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecibedFpRequestRecyclerAdapter  (options: FirestoreRecyclerOptions<RequestModel>) :
    FirestoreRecyclerAdapter<RequestModel, RecibedFpRequestRecyclerAdapter.MyViewHolder>(
        options
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_request_item, parent, false)

        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: RequestModel) {
        var userRepo = UsersRepository()
        var user : UserModel? = UserModel()
        GlobalScope.launch (Dispatchers.IO){
            user = userRepo.getUser(model.fromUid)
            withContext(Dispatchers.Main){
                holder.nickname.text = user?.nickName ?:""
                holder.age.text = user?.getEdad().toString() ?:""
                Glide.with(holder.itemView).load(user?.photoUrl ?:"").into(holder.photoUrl)
                holder.message.text = model.message
                holder.fbAccept.setOnClickListener {
                    val requestUid = snapshots.getSnapshot(holder.layoutPosition).id
                    Log.i("fpRequestAdapter", "Request $requestUid accepted")
                    acceptUserRequest(requestUid, model.fromUid, model.toUid)

                }
                holder.fbDeny.setOnClickListener {
                    val requestUid = snapshots.getSnapshot(holder.layoutPosition).id
                    dennyUserRequest(requestUid, model.fromUid, model.toUid)
                    Log.i("fpRequestAdapter", "Request $requestUid denied")
                }
            }
        }



    }

    private fun dennyUserRequest(requestUid: String, userUid: String, fpUid: String) {
        var fpRequestRepository = FpRequestRepository(fpUid)
        var usrRequestRepository = UserRequestRepository(userUid)
        fpRequestRepository.denyRecibedRequest(requestUid)
        usrRequestRepository.sentRequestDenied(requestUid)
    }

    private fun acceptUserRequest(requestUid : String, userUid: String, fpUid: String) {
        var fpRequestRepository = FpRequestRepository(fpUid)
        var usrRequestRepository = UserRequestRepository(userUid)
        var fpRepository = FriendPointsRepository()
        var userRepository = UsersRepository()
        fpRequestRepository.accepRecibedRequest(requestUid)
        usrRequestRepository.sentRequestAccepted(requestUid)
        fpRepository.addMember(fpUid,userUid)
        userRepository.userAddedToFp(userUid,fpUid)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val nickname: TextView = itemView.findViewById(R.id.usr_request_tv_nickname)
        val age: TextView = itemView.findViewById(R.id.usr_request_tv_edad)
        val message: TextView = itemView.findViewById(R.id.user_request_message)
        val photoUrl: CircleImageView = itemView.findViewById(R.id.usr_request_civ_photo)
        val fbAccept: FloatingActionButton = itemView.findViewById(R.id.usr_request_fb_ok)
        val fbDeny: FloatingActionButton = itemView.findViewById(R.id.usr_request_fb_deny)

    }
}