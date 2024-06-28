package com.dicoding.storyappsubmission.view.upStory

import androidx.lifecycle.ViewModel
import com.dicoding.storyappsubmission.data.repository.UserRepository
import java.io.File

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    fun addStory(file: File, description: String) = repository.addStory(file, description)
}