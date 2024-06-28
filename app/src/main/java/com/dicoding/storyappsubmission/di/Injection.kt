package com.dicoding.storyappsubmission.di

import android.content.Context
import com.dicoding.storyappsubmission.data.local.database.StoryDatabase
import com.dicoding.storyappsubmission.data.pref.UserPreference
import com.dicoding.storyappsubmission.data.pref.dataStore
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.storyappsubmission.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getAuthApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return UserRepository(apiService, pref, database)
    }
}