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
import com.example.tfg_friendpoint.ui.fragments.HomeFragmentDirections
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView


class ExploreFriendPointRecyclerAdapter(options: FirestoreRecyclerOptions<FriendPointModel>) :
    FirestoreRecyclerAdapter<FriendPointModel, ExploreFriendPointRecyclerAdapter.MyViewHolder>(
        options
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.explore_item_friend_point, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: FriendPointModel) {
        holder.plan.text = model.plan
        holder.nMiembros.text = model.getNumeroMiembros().toString()
        holder.ubicacion.text = model.ubicacion
        Glide.with(holder.itemView).load(model.photoUrl).into(holder.photoUrl)
        holder.view.setOnClickListener {
            //TODO Crear un viewModel para el friend point seleccionado y setear el uid al del friendpoint clicado


            Log.e("TAG", snapshots.getSnapshot(holder.layoutPosition).id)

            /*Navigation.findNavController(it).previousBackStackEntry
                ?.savedStateHandle
                ?.set("uid")*/
            val uid = snapshots.getSnapshot(holder.layoutPosition).id
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(uid)
            Navigation.findNavController(it).navigate(action)


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



