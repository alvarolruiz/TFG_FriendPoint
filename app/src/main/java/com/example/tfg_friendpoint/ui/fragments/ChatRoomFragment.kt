package com.example.tfg_friendpoint.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_friendpoint.databinding.FragmentChatRoomBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.example.tfg_friendpoint.repository.ChatsRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.Adapter.MessageRecyclerAdapter
import com.example.tfg_friendpoint.ui.model.MessageModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NullPointerException


class ChatRoomFragment : Fragment() {
    private lateinit var mBinding: FragmentChatRoomBinding
    private lateinit var rvMessages: RecyclerView
    private val args: ChatRoomFragmentArgs by navArgs()
    private lateinit var chatRepository: ChatsRepository
    private var authRepository = AuthRepository()
    private lateinit var usersRepository: UsersRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentChatRoomBinding.inflate(inflater, container, false)
        val view = mBinding.root
        rvMessages = mBinding.roomRvMessages
        chatRepository = ChatsRepository(authRepository.currentUser!!.uid, args.fpUid)
        usersRepository = UsersRepository()
        rvMessages.layoutManager = LinearLayoutManager(activity)
        var list = ArrayList<MessageModel>()
        list = chatRepository.getChatMessagesList()
        var adapter = MessageRecyclerAdapter(view.context, list)
        rvMessages.adapter = adapter
        mBinding.roomFbSend.setOnClickListener {
            var message = mBinding.roomEtMsg.text.toString()
            sendMessage(message)
            mBinding.roomEtMsg.setText("")
        }

        /*chatRepository.getChatRoomReference().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var s = dataSnapshot.getValue(MessageModel::class.java)
                if (s != null) {
                    list.add(s)
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }

        })*/

        chatRepository.getChatRoomReference().addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var s = snapshot.getValue(MessageModel::class.java)
                if (s != null) {
                    list.add(s)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })


        chatRepository

        return view
    }

    private fun sendMessage(message: String) {
        var nickname = ""

        lateinit var messageModel: MessageModel
        GlobalScope.launch(Dispatchers.Main) {
            var photo = usersRepository.getUserImage(authRepository.currentUser!!.uid)
            nickname = usersRepository.getUserNickName(authRepository.currentUser!!.uid)
            messageModel = MessageModel(message, nickname, photo)
            chatRepository.sendMessage(messageModel)

        }
    }


}