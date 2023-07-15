package com.example.testingchat.data.repository

import com.example.testingchat.data.auth.initial.InitialAuthReponse
import com.example.testingchat.data.auth.initial.InitialAuthRequest
import com.example.testingchat.data.auth.sms.SmsAuthRequest
import com.example.testingchat.data.auth.sms.SmsAuthResponse
import com.example.testingchat.service.AuthService
import retrofit2.Response
import javax.inject.Inject

class RemoteUser @Inject constructor(private val service: AuthService): AuthService {


    override suspend fun sendAuthCode(request: InitialAuthRequest): Response<InitialAuthReponse> =
        service.sendAuthCode(request)

    override suspend fun validateUser(request: SmsAuthRequest): Response<SmsAuthResponse> =
        service.validateUser(request)
}