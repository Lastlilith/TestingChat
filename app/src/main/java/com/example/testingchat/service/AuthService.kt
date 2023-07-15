package com.example.testingchat.service

import com.example.testingchat.data.auth.AuthReponse
import com.example.testingchat.data.auth.AuthRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(
        @Body request: AuthRequest
    ): Response<AuthReponse>
}