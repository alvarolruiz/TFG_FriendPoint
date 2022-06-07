package com.example.tfg_friendpoint.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.ui.model.MessageModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class MessageRecyclerAdapter(context: Context, messageList: ArrayList<MessageModel>) :
    RecyclerView.Adapter<MessageRecyclerAdapter.MyViewHolder>() {
    val context = context
    val list : ArrayList<MessageModel> = messageList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false)

        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var model = list.get(position)
        holder.tvNickname.text = model.nickname
        holder.tvTime.text = model.sendTime.toString()
        holder.tvMessage.text = model.message
        Glide.with(holder.itemView).load(model.photoUri).into(holder.civPhoto)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val tvNickname = itemView.findViewById<TextView>(R.id.msg_tv_nickname)
        val tvTime = itemView.findViewById<TextView>(R.id.msg_tv_time)
        val civPhoto = itemView.findViewById<CircleImageView>(R.id.msg_civ_photo)
        val tvMessage: TextView = itemView.findViewById(R.id.mgs_tv_message)

    }
}