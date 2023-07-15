package com.example.testingchat.data.repository

import com.example.testingchat.data.auth.AuthReponse
import com.example.testingchat.data.auth.AuthRequest
import com.example.testingchat.service.AuthService
import retrofit2.Response
import javax.inject.Inject

class RemoteUser @Inject constructor(private val service: AuthService): AuthService {


    override suspend fun sendAuthCode(request: AuthRequest): Response<AuthReponse> =
        service.sendAuthCode(request)
}