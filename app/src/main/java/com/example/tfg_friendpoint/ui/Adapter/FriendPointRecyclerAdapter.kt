package com.example.tfg_friendpoint.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class FriendPointRecyclerAdapter (options: FirestoreRecyclerOptions<FriendPointModel>) :
    FirestoreRecyclerAdapter<FriendPointModel,FriendPointRecyclerAdapter.MyViewHolder> (options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_friend_point, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: FriendPointModel) {
        holder.plan.text = model.plan
        holder.nMiembros.text = model.numeroMiembros.toString()
        holder.photo.drawable
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val photo : ImageView = itemView.findViewById(R.id.list_iv_photo)
        val plan : TextView = itemView.findViewById(R.id.list_tv_plan)
        val nMiembros : TextView = itemView.findViewById(R.id.list_tv_numero_miembros)
        val ubicavion : TextView = itemView.findViewById(R.id.list_tv_ubi)
    }
}



