package com.example.testingchat

import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.data.repository.RemoteUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthUsernameViewModel @Inject constructor(
    private val remote: RemoteUser
): BaseViewModel() {

}