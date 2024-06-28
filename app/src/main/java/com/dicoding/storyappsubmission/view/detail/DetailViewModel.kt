package com.dicoding.storyappsubmission.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappsubmission.data.remote.response.DetailStoryResponse
import com.dicoding.storyappsubmission.data.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _storyDetail = MutableLiveData<DetailStoryResponse>()
    val storyDetail: LiveData<DetailStoryResponse> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryDetail(storyId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getStoryById(storyId)
                _storyDetail.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}