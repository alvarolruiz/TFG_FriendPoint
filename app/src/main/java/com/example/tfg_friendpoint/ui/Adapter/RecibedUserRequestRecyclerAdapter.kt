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


class RecibedUserRequestRecyclerAdapter(options: FirestoreRecyclerOptions<RequestModel>) :
    FirestoreRecyclerAdapter<RequestModel, RecibedUserRequestRecyclerAdapter.MyViewHolder>(
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
        var user: UserModel? = UserModel()
        GlobalScope.launch(Dispatchers.IO) {
            user = userRepo.getUser(model.fromUid)
            withContext(Dispatchers.Main) {
                holder.nickname.text = user?.nickName ?: ""
                holder.edad.text = user?.getEdad().toString() ?: ""
                Glide.with(holder.itemView).load(user?.photoUrl ?: "").into(holder.photoUrl)
                holder.fbAccept.setOnClickListener {
                    val requestUid = snapshots.getSnapshot(holder.layoutPosition).id
                    acceptUserRequest(model.fromUid, model.toUid, requestUid)
                    Log.i("fpRequestAdapter", "Request ${requestUid} accepted")
                }
                holder.fbDeny.setOnClickListener {
                    val requestUid = snapshots.getSnapshot(holder.layoutPosition).id
                    Log.i("fpRequestAdapter", "Request ${requestUid} denied")

                }
            }
        }


    }

    private fun acceptUserRequest(requestUid: String, userUid: String, fpUid: String) {
        var fpRequestRepository = FpRequestRepository(fpUid)
        var usrRequestRepository = UserRequestRepository(userUid)
        fpRequestRepository.accepRecibedRequest(requestUid)
        usrRequestRepository.sentRequestAccepted(requestUid)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val nickname: TextView = itemView.findViewById(R.id.usr_request_tv_nickname)
        val edad: TextView = itemView.findViewById(R.id.usr_request_tv_edad)
        val photoUrl: CircleImageView = itemView.findViewById(R.id.usr_request_civ_photo)
        val fbAccept: FloatingActionButton = itemView.findViewById(R.id.usr_request_fb_ok)
        val fbDeny: FloatingActionButton = itemView.findViewById(R.id.usr_request_fb_deny)

    }
}