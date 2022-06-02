package com.example.tfg_friendpoint.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView


class FriendPointRecyclerAdapter(options: FirestoreRecyclerOptions<FriendPointModel>) :
    FirestoreRecyclerAdapter<FriendPointModel, FriendPointRecyclerAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_friend_point, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: FriendPointModel) {
        holder.plan.text = model.plan
        holder.nMiembros.text = model.numeroMiembros.toString()
        holder.ubicacion.text = model.ubicacion
        Glide.with(holder.itemView).load(model.photoUrl).into(holder.photoUrl)
        holder.view.setOnClickListener {
            //TODO Crear un viewModel para el friend point seleccionado y setear el uid al del friendpoint clicado

            Navigation.findNavController(it)
                .navigate(R.id.action_home_fragment_to_detailsFragment)
//snapshots.getSnapshot(holder.layoutPosition).id
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val photoUrl: CircleImageView = itemView.findViewById(R.id.list_iv_photo)
        val plan: TextView = itemView.findViewById(R.id.list_tv_plan)
        val nMiembros: TextView = itemView.findViewById(R.id.list_tv_numero_miembros)
        val ubicacion: TextView = itemView.findViewById(R.id.list_tv_ubi)

    }
}



