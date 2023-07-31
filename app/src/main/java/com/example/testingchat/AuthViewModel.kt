package com.example.testingchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testingchat.base.BaseViewModel


class AuthViewModel : BaseViewModel() {


    private val _phoneText = NonNullMutableLiveData("")
    val phoneText: NonNullLiveData<String>
        get() = _phoneText

    private val _smsText = MutableLiveData<String>()
    val smsText: LiveData<String>
        get() = _smsText

    private val _progress = NonNullMutableLiveData(false)
    val progress: NonNullLiveData<Boolean>
        get() = _progress

    private val _successCode = NonNullMutableLiveData(false)
    val successCode: NonNullLiveData<Boolean>
        get() = _successCode


    private val _isUserExist = NonNullMutableLiveData(false)
    val isUserExist: NonNullLiveData<Boolean>
        get() = _isUserExist


    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error


}