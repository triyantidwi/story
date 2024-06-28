package com.dicoding.storyappsubmission.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.local.database.StoryDatabase
import com.dicoding.storyappsubmission.data.local.database.StoryRemoteMediator
import com.dicoding.storyappsubmission.data.pref.UserModel
import com.dicoding.storyappsubmission.data.pref.UserPreference
import com.dicoding.storyappsubmission.data.remote.response.AddStoryResponse
import com.dicoding.storyappsubmission.data.remote.response.DetailStoryResponse
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.data.remote.response.LoginResponse
import com.dicoding.storyappsubmission.data.remote.response.RegisterResponse
import com.dicoding.storyappsubmission.data.remote.response.StoryResponse
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.storyappsubmission.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository(
    private val apiService: ApiService,
    private val pref: UserPreference,
    private val database: StoryDatabase
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return ApiConfig.getApiService().register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return ApiConfig.getApiService().login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return pref.getSession()
    }

    suspend fun getStoryById(id: String): DetailStoryResponse {
        return apiService.getStoryById(id)
    }

    fun addStory(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.addStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: UserPreference,
            database: StoryDatabase
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref, database)
            }.also { instance = it }
    }
}