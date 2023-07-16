package com.example.testingchat.service

import com.example.testingchat.data.sign_up.SignUpRequest
import com.example.testingchat.data.sign_up.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpService {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/users/register/")
    suspend fun registerUser(
        @Body request: SignUpRequest
    ): Response<SignUpResponse>
}