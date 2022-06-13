package com.example.tfg_friendpoint.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_friendpoint.databinding.FragmentFpMembersBinding
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.Adapter.MembersRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.UserModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class FpMembersFragment : Fragment() {
    private lateinit var mBinding: FragmentFpMembersBinding
    private var membersAdapter: MembersRecyclerAdapter? = null

    private lateinit var repository: FriendPointsRepository
    private val args: FpMembersFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFpMembersBinding.inflate(inflater, container, false)
        val view = mBinding.root
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        membersAdapter = getAdapter()
        mBinding.membersRv.layoutManager = LinearLayoutManager(activity)
        mBinding.membersRv.adapter = membersAdapter
    }

    private fun getAdapter(): MembersRecyclerAdapter {
        var usersRepo = UsersRepository()
        var users = usersRepo.getUserMembersOfFp(args.fpUid)
        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(users, UserModel::class.java)
            .build()
        return MembersRecyclerAdapter(firestoreRecyclerOptions)
    }

    override fun onStart() {
        super.onStart()
        membersAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        membersAdapter!!.startListening()
    }

    override fun onPause() {
        super.onPause()
        membersAdapter!!.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        membersAdapter!!.stopListening()
    }


}
