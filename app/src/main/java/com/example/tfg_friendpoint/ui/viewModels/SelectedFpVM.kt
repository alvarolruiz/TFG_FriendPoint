package com.example.tfg_friendpoint.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfg_friendpoint.ui.model.FriendPointModel

class SelectedFpVM : ViewModel() {
    val selectedFp = MutableLiveData<FriendPointModel>()

    fun setSelectedFp(friendPointModel: FriendPointModel) {
        selectedFp.postValue(friendPointModel)
    }

}