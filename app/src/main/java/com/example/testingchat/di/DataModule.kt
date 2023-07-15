package com.example.testingchat.di

import android.content.Context
import android.content.SharedPreferences
import com.example.testingchat.data.local.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getPreferences(context)

    @Singleton
    @Provides
    fun providePreferencesManager(preferences: SharedPreferences): PreferenceManager =
        PreferenceManager(preferences)
}