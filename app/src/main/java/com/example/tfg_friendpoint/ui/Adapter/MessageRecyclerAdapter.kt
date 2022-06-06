package com.example.tfg_friendpoint.ui.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.example.tfg_friendpoint.ui.model.MessageModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class MessageRecyclerAdapter(options: FirestoreRecyclerOptions<MessageModel>) :
    FirestoreRecyclerAdapter<MessageModel,MessageRecyclerAdapter.MyViewHolder>(
        options
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false)

        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: MessageModel) {
        holder.tvNickname.text = model.nickname
        holder.tvTime.text = model.sendTime
        holder.tvMessage.text = model.body
        Glide.with(holder.itemView).load(model.photoUri).into(holder.civPhoto)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val tvNickname = itemView.findViewById<TextView>(R.id.msg_tv_nickname)
        val tvTime = itemView.findViewById<TextView>(R.id.msg_tv_time)
        val civPhoto = itemView.findViewById<CircleImageView>(R.id.msg_civ_photo)
        val tvMessage: TextView = itemView.findViewById(R.id.mgs_tv_message)

    }
}