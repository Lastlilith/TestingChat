package com.example.testingchat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.data.const.Constant.Companion.USER_ID
import com.example.testingchat.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
): BaseViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList

    var myId: Int? = null

    fun loadAllUsers() {
        myId = preferenceManager.getInt(USER_ID.toString())
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(true)

            try {
                val querySnapshot = firestore.collection("users")
                    .get()
                    .await()
                val userList = querySnapshot.toObjects(UserModel::class.java)
                _userList.postValue(userList)
                _loadingState.postValue(false)
            } catch (exception: Exception) {
                _loadingState.postValue(false)
                // Handle the error
            }
        }
    }
}