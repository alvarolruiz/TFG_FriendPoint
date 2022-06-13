package com.example.tfg_friendpoint.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.repository.*
import com.example.tfg_friendpoint.ui.model.RequestModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendshipRequestDialog : BottomSheetDialogFragment() {
    //Repositories
    private lateinit var userRepo: UsersRepository
    private lateinit var userRequestRepo: UserRequestRepository
    private lateinit var fpRequestRepo: FpRequestRepository
    private lateinit var authRepo: AuthRepository
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
        authRepo = AuthRepository()
        userRepo = UsersRepository()
        userRequestRepo = UserRequestRepository(authRepo.currentUser!!.uid)
        fpRequestRepo = FpRequestRepository(args.friendPointUid)
    }

    private fun setupButtons() {
        setupOkButton()
        setupCancelButton()
    }

    private fun setupOkButton() {
        okButton.setOnClickListener {
            var request = RequestModel(authRepo.currentUser!!.uid, args.friendPointUid, etMessage.text.toString())
            sendRequest(request)
            dismiss()
        }
    }

    fun sendRequest(request: RequestModel) {
        GlobalScope.launch(Dispatchers.IO) {
            if (userRequestRepo.isValidSentRequest(authRepo.currentUser!!.uid)){
                var result = userRequestRepo.sendRequestToFp(request)
                fpRequestRepo.recibeRequestFromUser(result, request)
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Ya has enviado una petición de amistad a este friendpoint." +
                            "Espera a que sea respondida", Toast.LENGTH_LONG).show()
                }
            }
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