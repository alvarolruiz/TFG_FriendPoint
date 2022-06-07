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
import com.example.tfg_friendpoint.ui.fragments.ExploreFragmentDirections
import com.example.tfg_friendpoint.ui.fragments.HomeFragmentDirections
import com.example.tfg_friendpoint.ui.model.FriendPointModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView

class HomeFriendPointRecyclerAdapter (options: FirestoreRecyclerOptions<FriendPointModel>) :
    FirestoreRecyclerAdapter<FriendPointModel, HomeFriendPointRecyclerAdapter.MyViewHolder>(
        options
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.home_item_friend_point, parent, false)

        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: FriendPointModel) {
        holder.plan.text = model.plan
        holder.ubicacion.text = model.ubicacion
        Glide.with(holder.itemView).load(model.photoUrl).into(holder.photoUrl)
        holder.view.setOnClickListener {
            val uid = snapshots.getSnapshot(holder.layoutPosition).id
            Log.e("clickedItem", uid)
            nagivateToDetails(it, uid)
        }
        holder.fbEdit.setOnClickListener {
            val uid = snapshots.getSnapshot(holder.layoutPosition).id
            navigateToUpdate(it, uid)
        }
    }

    private fun navigateToUpdate(it: View, uid: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToUpdateFpInfo(uid)
        Navigation.findNavController(it).navigate(action)
    }

    private fun nagivateToDetails(it: View, fpUid: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(fpUid)
        Navigation.findNavController(it).navigate(action)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
        val photoUrl: CircleImageView = itemView.findViewById(R.id.home_item_civ_photo)
        val plan: TextView = itemView.findViewById(R.id.home_item_tv_plan)
        val ubicacion: TextView = itemView.findViewById(R.id.home_item_tv_ubi)
        val fbEdit: FloatingActionButton = itemView.findViewById(R.id.home_item_fb_edit)

    }
}