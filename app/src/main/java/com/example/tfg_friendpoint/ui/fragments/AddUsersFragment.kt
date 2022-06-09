package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.databinding.FragmentAddUsersBinding
import com.example.tfg_friendpoint.databinding.FragmentDetailsBinding

class AddUsersFragment : Fragment() {
    private lateinit var mBinding: FragmentAddUsersBinding
    //private val fpAdapter : AddUser =null
    //private val args:  by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentAddUsersBinding.inflate(inflater, container, false)
        val view = mBinding.root
            //setupRecyclerView()
        return view
    }

   /* private fun setupRecyclerView() {
        fpAdapter  = getAdapter()
        mBinding.layoutManager = LinearLayoutManager(activity)
        mBinding.rvExploreFriendPoints.adapter = fpAdapter
    }*/



    // por cada item vamos a poder pulsar un boton. lo que hará que le llegue
    // una peticion de amistad al usuario (fp send user recibe)



}