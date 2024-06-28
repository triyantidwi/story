package com.dicoding.storyappsubmission.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumptech.glide.Glide.init
import com.dicoding.storyappsubmission.data.pref.UserModel
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.data.repository.UserRepository
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userSession = MutableLiveData<UserModel>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var  story: LiveData<PagingData<ListStoryItem>>? = null



    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                _userSession.value = user
                if (user.token.isNotEmpty()) {
                    loadStories()

                }
            }
        }
    }


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun loadStories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                story = repository.getStoriesPaging().cachedIn(viewModelScope)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getStoryPagingData(): LiveData<PagingData<ListStoryItem>>? {
        return story
    }
}
