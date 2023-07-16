package com.example.testingchat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.data.const.Constant.Companion.ACCESS_TOKEN
import com.example.testingchat.data.const.Constant.Companion.NAME
import com.example.testingchat.data.const.Constant.Companion.PHONE_NUMBER
import com.example.testingchat.data.const.Constant.Companion.REFRESH_TOKEN
import com.example.testingchat.data.const.Constant.Companion.USERNAME
import com.example.testingchat.data.const.Constant.Companion.USER_ID
import com.example.testingchat.data.repository.RemoteUserRegister
import com.example.testingchat.data.sign_up.SignUpRequest
import com.example.testingchat.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val remote: RemoteUserRegister
) : BaseViewModel() {

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

    fun registerUser(phoneNumber: String, name: String, userName: String) {
        _progress.value = true
        _nameText.value = name
        _userNameText.value = userName
        _phoneText.value = phoneNumber
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = register(
                    SignUpRequest(
                        phone = _phoneText.value,
                        name = _nameText.value,
                        username = _userNameText.value
                    )
                )
                launch(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        _progress.value = false
                        Log.e("POPO", "success ${response.message()} ${response.body()}")
                        _success.value = true
                        preferenceManager.setData(PHONE_NUMBER, _phoneText.value)
                        preferenceManager.setData(NAME, _nameText.value)
                        preferenceManager.setData(USERNAME, _userNameText.value)
                        preferenceManager.setData(ACCESS_TOKEN, response.body()!!.access_token)
                        preferenceManager.setData(REFRESH_TOKEN, response.body()!!.refresh_token)
                        preferenceManager.setData(USER_ID, response.body()!!.user_id)
                        Log.e(
                            "POPO",
                            "Prefname: ${preferenceManager.getString(NAME)}, prefUsername: ${
                                preferenceManager.getString(USERNAME)
                            }, prefPhone: ${preferenceManager.getString(PHONE_NUMBER)}, refresh: ${
                                preferenceManager.getString(
                                    REFRESH_TOKEN
                                )
                            }, access: ${preferenceManager.getString(ACCESS_TOKEN)}"
                        )
                        launch(Dispatchers.IO) {
                            sendDataToFirestore(
                                UserModel(
                                    phone = _phoneText.value,
                                    name = _nameText.value,
                                    username = _userNameText.value,
                                    id = response.body()!!.user_id,
                                )
                            )
                        }
                    } else {
                        Log.e("POPO", "failed from else: ${response.message()} ${response.body()} ")
                        _error.value = "Что-то пошло не так: ${response.message()}"
                        _progress.value = false
                        _success.value = false
                    }
                }
            } catch (e: IOException) {
                launch(Dispatchers.Main) {
                    Log.e("POPO", "failed from catch IO : ${e.localizedMessage} ")
                    _error.value = "Проверьте интернет соединение и повторите попытку позже"
                    _progress.value = false
                }

            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.e("POPO", "failed in catch: ${e.localizedMessage} ")
                    _error.value = "Ошибка. Повторите попытку позже"
                    _progress.value = false
                }
            }
        }
    }
    private fun sendDataToFirestore(user: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val collectionRef = firestore.collection("users")
                val documentRef = collectionRef.document(user.id.toString())
                documentRef.set(user)
                    .addOnSuccessListener {
                        _dataSent.postValue(true)
                    }
                    .addOnFailureListener { exception ->
                        _dataSent.postValue(false)
                        Log.e(
                            "RegisterUserViewModel",
                            "Failed to send data to Firestore: $exception"
                        )
                    }
            } catch (e: Exception) {
                _dataSent.postValue(false)
                Log.e("RegisterUserViewModel", "Error sending data to Firestore: ${e.message}")
            }
        }
    }

    private suspend fun register(request: SignUpRequest) =
        remote.registerUser(request)
}