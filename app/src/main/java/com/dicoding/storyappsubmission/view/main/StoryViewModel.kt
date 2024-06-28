package com.dicoding.storyappsubmission.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyappsubmission.data.pref.UserModel
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.data.repository.UserRepository
import kotlinx.coroutines.launch
class StoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userSession = MutableLiveData<UserModel>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                _userSession.value = user
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

     fun getStories() : LiveData<PagingData<ListStoryItem>> {
         return  repository.getStories().cachedIn(viewModelScope)
     }



    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
