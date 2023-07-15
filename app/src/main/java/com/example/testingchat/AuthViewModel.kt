package com.example.testingchat

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.data.auth.AuthRequest
import com.example.testingchat.data.repository.RemoteUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val remote: RemoteUser
): BaseViewModel() {

    private val _phoneText = NonNullMutableLiveData("")
    val phoneText: NonNullLiveData<String>
        get() = _phoneText

    private val _smsText = NonNullMutableLiveData("")
    val smsText: NonNullLiveData<String>
        get() = _smsText

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _successCode = MutableLiveData<Boolean>()
    val successCode: LiveData<Boolean>
        get() = _successCode

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun sendAuthRequest() {
        _progress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authenticate(
                    authRequest = AuthRequest(
                        phone = _phoneText.value.toString().trim()
                    )
                )
                launch(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        _progress.value = false
                        Log.e("POPO", "success ${response.message()} ${response.body()}")
                        Toast.makeText(application.applicationContext, "Пароль: 133337", Toast.LENGTH_LONG)
                            .show()
                        _successCode.value = true
                    } else {
                        Log.e("POPO", "failed: ${response.message()} ${response.body()} ")
                        Toast.makeText(application.applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        _error.value = "Проверьте интернет соединение и повторите попытку позже"
                        _progress.value = false
                        _successCode.value = false
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.e("POPO", "failed: ${e.localizedMessage} ")
                    _error.value = "Проверьте интернет соединение и повторите попытку позже"
                    _progress.value = false
                    _successCode.value = false
                }
            }
        }
    }

    private suspend fun authenticate(authRequest: AuthRequest) = remote.sendAuthCode(authRequest)
}