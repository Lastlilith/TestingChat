package com.example.testingchat.data.repository

import com.example.testingchat.data.sign_up.SignUpRequest
import com.example.testingchat.data.sign_up.SignUpResponse
import com.example.testingchat.service.SignUpService
import retrofit2.Response
import javax.inject.Inject

class RemoteUserRegister @Inject constructor(private val service: SignUpService): SignUpService  {

    override suspend fun registerUser(request: SignUpRequest): Response<SignUpResponse> =
        service.registerUser(request)

}
