package com.example.testingchat.app.activities.register

import com.example.testingchat.base.BaseViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel  @Inject constructor(

): BaseViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


}