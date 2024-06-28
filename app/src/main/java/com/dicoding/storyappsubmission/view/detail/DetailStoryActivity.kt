package com.dicoding.storyappsubmission.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyappsubmission.R
import com.dicoding.storyappsubmission.data.remote.response.DetailStoryResponse
import com.dicoding.storyappsubmission.databinding.ActivityDetailStoryBinding
import com.dicoding.storyappsubmission.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.detail_story)

        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            viewModel.getStoryDetail(id)
        }

        viewModel.storyDetail.observe(this) { storyDetail ->
            if (storyDetail != null) {
                showStoryDetail(storyDetail)
            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showStoryDetail(story: DetailStoryResponse) {
        Glide.with(applicationContext)
            .load(story.story.photoUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = story.story.name
        binding.tvDetailDescription.text = story.story.description

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}