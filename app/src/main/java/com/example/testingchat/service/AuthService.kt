package com.example.testingchat.service

import com.example.testingchat.data.auth.initial.InitialAuthReponse
import com.example.testingchat.data.auth.initial.InitialAuthRequest
import com.example.testingchat.data.auth.sms.SmsAuthRequest
import com.example.testingchat.data.auth.sms.SmsAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(
        @Body request: InitialAuthRequest
    ): Response<InitialAuthReponse>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/check-auth-code/")
    suspend fun validateUser(
        @Body request: SmsAuthRequest
    ): Response<SmsAuthResponse>
}