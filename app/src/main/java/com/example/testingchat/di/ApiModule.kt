package com.example.testingchat.di

import com.example.testingchat.data.const.Constant.Companion.BASE_URL
import com.example.testingchat.service.AuthService
import com.example.testingchat.service.SignUpService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit.Builder): AuthService =
        retrofit.baseUrl(BASE_URL).build().create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideUserSignUp(retrofit: Retrofit.Builder): SignUpService =
        retrofit.baseUrl(BASE_URL).build().create(SignUpService::class.java)
}