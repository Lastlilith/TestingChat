package com.example.testingchat

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.data.auth.initial.InitialAuthRequest
import com.example.testingchat.data.auth.sms.SmsAuthRequest
import com.example.testingchat.data.const.Constant.Companion.ACCESS_TOKEN
import com.example.testingchat.data.const.Constant.Companion.PHONE_NUMBER
import com.example.testingchat.data.const.Constant.Companion.REFRESH_TOKEN
import com.example.testingchat.data.repository.RemoteUserAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val remote: RemoteUserAuth
) : BaseViewModel() {

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

    fun sendAuthRequest(number: String) {
        _progress.value = true
        _phoneText.value = number
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authenticate(
                    initialAuthRequest = InitialAuthRequest(
                        phone = _phoneText.value.toString().trim()
                    )
                )
                launch(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        _progress.value = false
                        Log.e("POPO", "success ${response.message()} ${response.body()}")
                        _successCode.value = true
                        preferenceManager.setData(PHONE_NUMBER, _phoneText.value.toString().trim())
                        Toast.makeText(application.applicationContext, "Пароль: 133337", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("POPO", "failed from else: ${response.message()} ${response.body()} ")
                        _error.value = "Что-то пошло не так"
                        _progress.value = false
                        _successCode.value = false
                    }
                }
            } catch (e: IOException) {
                launch(Dispatchers.Main) {
                    Log.e("POPO", "failed from catch IO : ${e.localizedMessage} ")
                    _error.value = "Проверьте интернет соединение и повторите попытку позже"
                    _progress.value = false
                }
            }
            catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.e("POPO", "failed from catch: ${e.localizedMessage} ")
                    _error.value = "Повторите попытку позже"
                    _progress.value = false
                }
            }
        }
    }

    fun validateUser(sms: String) {
        _progress.value = true
        _smsText.value = sms
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = verifyCode(
                    smsAuthRequest = SmsAuthRequest(
                        phone = _phoneText.value.toString().trim(),
                        code = _smsText.value.toString().trim()
                    )
                )
                launch(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()!= null) {
                        Log.e("POPO", "success ${response.message()} ${response.body()}")
                        _progress.value = false
                        _isUserExist.value = response.body()!!.is_user_exists
                        if (response.body()!!.is_user_exists) {
                            preferenceManager.setData(REFRESH_TOKEN, response.body()!!.refreshToken)
                            preferenceManager.setData(ACCESS_TOKEN, response.body()!!.access_token)
                        }
                        Log.e("POPO", "from viewModel: ${_isUserExist.value}")
                    } else {
                        Log.e("POPO", "failed in response: ${response.message()} ${response.body()} ")
                        Log.e("POPO", "sms code: ${_smsText.value}")
                        _error.value = "Неверный код"
                        _progress.value = false
                    }
                }
            } catch (e: IOException) {
                launch(Dispatchers.Main) {
                    Log.e("POPO", "failed from catch IO : ${e.localizedMessage} ")
                    _error.value = "Проверьте интернет соединение и повторите попытку позже"
                    _progress.value = false
                }
            }
            catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.e("POPO", "failed in catch: ${e.localizedMessage} ")
                    _error.value = "Повторите попытку позже"
                    _progress.value = false
                }
            }
        }
    }

    private suspend fun authenticate(initialAuthRequest: InitialAuthRequest) =
        remote.sendAuthCode(initialAuthRequest)

    private suspend fun verifyCode(smsAuthRequest: SmsAuthRequest) =
        remote.validateUser(smsAuthRequest)

}