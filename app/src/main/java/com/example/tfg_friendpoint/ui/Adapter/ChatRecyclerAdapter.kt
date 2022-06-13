package com.example.tfg_friendpoint.ui.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.ui.fragments.ChatFragmentDirections
import com.example.tfg_friendpoint.ui.fragments.ExploreFragmentDirections
import com.example.tfg_friendpoint.ui.model.ChatModel
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class ChatRecyclerAdapter(options: FirestoreRecyclerOptions<ChatModel>) :
    FirestoreRecyclerAdapter<ChatModel, ChatRecyclerAdapter.MyViewHolder>(
        options
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item, parent, false)

        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: ChatModel) {
        holder.nombre.text =  model.plan
        Glide.with(holder.view).load(model.photoUrl).into(holder.photo)

        holder.view.setOnClickListener {
            Log.e("clickedChatItem", snapshots.getSnapshot(holder.layoutPosition).id)
            val uid = snapshots.getSnapshot(holder.layoutPosition).id
            nagivateToRoom(it, uid)
        }
    }

    private fun nagivateToRoom(it: View, fpUid: String) {
        val action = ChatFragmentDirections.actionChatFragmentToChatRoomFragment(fpUid)
        Navigation.findNavController(it).navigate(action)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val nombre : TextView = itemView.findViewById(R.id.chat_item_tv_nombre)
        val photo : CircleImageView = itemView.findViewById(R.id.chat_item_civ_photo)

    }
}