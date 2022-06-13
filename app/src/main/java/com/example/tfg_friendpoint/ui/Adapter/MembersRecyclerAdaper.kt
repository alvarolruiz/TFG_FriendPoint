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
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.example.tfg_friendpoint.ui.model.UserModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView

class MembersRecyclerAdapter (options: FirestoreRecyclerOptions<UserModel>) :
    FirestoreRecyclerAdapter<UserModel, MembersRecyclerAdapter.MyViewHolder>(
        options
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_member_item, parent, false)

        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: UserModel) {
        Glide.with(holder.itemView).load(model.photoUrl).into(holder.photoUrl)
        holder.edad.text = model.getEdad().toString() + " Años"
        holder.nickname.text = model.nickName
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val nickname: TextView = itemView.findViewById(R.id.usr_member_tv_nickname)
        val edad: TextView = itemView.findViewById(R.id.usr_member_tv_edad)
        val photoUrl: CircleImageView = itemView.findViewById(R.id.usr_member_civ_photo)
    }
}