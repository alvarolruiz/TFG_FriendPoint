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
import com.example.tfg_friendpoint.repository.FpRequestRepository
import com.example.tfg_friendpoint.repository.FriendPointsRepository
import com.example.tfg_friendpoint.repository.UserRequestRepository
import com.example.tfg_friendpoint.repository.UsersRepository
import com.example.tfg_friendpoint.ui.fragments.DetailsFragmentArgs
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendshipRequestDialog : BottomSheetDialogFragment() {
    //Repositories
    lateinit var userRepository: UsersRepository
    lateinit var userRequestRepository: UserRequestRepository
    lateinit var fpRequestRepository: FpRequestRepository
    private val args: FriendshipRequestDialogArgs by navArgs()

    //View elements
    lateinit var etMessage: EditText
    lateinit var okButton: FloatingActionButton
    lateinit var cancelButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootview: View = inflater.inflate(R.layout.dialog_friendship_request, container, false)
        initializeRepositories()
        findViewElements(rootview)
        setupButtons()
        return rootview

    }

    private fun initializeRepositories() {
        userRequestRepository = UserRequestRepository(args.userUid)
        fpRequestRepository = FpRequestRepository(args.friendPointUid)
    }

    private fun setupButtons() {
        setupOkButton()
        setupCancelButton()
    }

    private fun setupOkButton() {
        okButton.setOnClickListener {
            var request = RequestModel(args.userUid, args.friendPointUid, etMessage.text.toString())
            sendRequest(request)
            dismiss()
        }
    }

    private fun sendRequest(request: RequestModel) {
        GlobalScope.launch (Dispatchers.IO){
            var requestId = userRequestRepository.sendRequestToFp(request)
            Log.e ("reqId", requestId)
            fpRequestRepository.recibeRequestFromUser(requestId, request)
        }

    }

    private fun setupCancelButton() {
        cancelButton.setOnClickListener { dismiss() }
    }

    private fun findViewElements(rootview: View) {
        etMessage = rootview.findViewById(R.id.request_et_message)
        okButton = rootview.findViewById(R.id.request_fb_ok)
        cancelButton = rootview.findViewById(R.id.request_fb_cancel)
    }


}