package com.example.tfg_friendpoint.ui.dialog

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.FragmentHomeBinding
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.fragments.DetailsFragmentArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendshipRequestDialog : BottomSheetDialogFragment() {

    lateinit var userRepository: UsersRepository
    private val args: FriendshipRequestDialogArgs by navArgs()
    private lateinit var message: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootview: View = inflater.inflate(R.layout.dialog_friendship_request, container, false)
        val etMessage = rootview.findViewById<EditText>(R.id.request_et_message)
        var okButon = rootview.findViewById<FloatingActionButton>(R.id.request_fb_ok)
        var cancelButton = rootview.findViewById<FloatingActionButton>(R.id.request_fb_cancel)
        message = etMessage.text.toString()
        okButon.setOnClickListener {
            Toast.makeText(rootview.context, etMessage.text, Toast.LENGTH_LONG).show()
            //Write send request on user and recibed on friendpoint
            dismiss()
            sendRequest()

        }
        cancelButton.setOnClickListener { dismiss() }
        userRepository = UsersRepository()

        return rootview

    }

    private fun sendRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                userRepository.sendRequest(args.userUid, args.friendPointUid, message)
            } catch (e: Exception) {
                Log.e("EXCEPCION", e.message.toString())
            }
        }
    }
}