package com.example.testingchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testingchat.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel  @Inject constructor(

): BaseViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _progress = NonNullMutableLiveData(false)
    val progress: NonNullLiveData<Boolean>
        get() = _progress

    private val _nameText = NonNullMutableLiveData("")
    val nameText: NonNullLiveData<String>
        get() = _nameText

    private val _userNameText = NonNullMutableLiveData("")
    val userNameText: NonNullLiveData<String>
        get() = _userNameText

    private val _phoneText = NonNullMutableLiveData("")
    val phoneText: NonNullLiveData<String>
        get() = _phoneText

    private val _success = NonNullMutableLiveData(false)
    val success: NonNullLiveData<Boolean>
        get() = _success

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _dataSent = MutableLiveData<Boolean>()
    val dataSent: LiveData<Boolean> = _dataSent

}