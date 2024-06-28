package com.dicoding.storyappsubmission.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyappsubmission.R
import com.dicoding.storyappsubmission.data.local.paging.LoadingStateAdapter
import com.dicoding.storyappsubmission.databinding.ActivityStoryBinding
import com.dicoding.storyappsubmission.view.ViewModelFactory
import com.dicoding.storyappsubmission.view.detail.DetailStoryActivity
import com.dicoding.storyappsubmission.view.login.LoginActivity
import com.dicoding.storyappsubmission.view.maps.MapsActivity
import com.dicoding.storyappsubmission.view.upStory.UploadActivity

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val viewModel: StoryViewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { session ->
            if (!session.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                viewModel.loadStories()
            }
        }



        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        setData()
        setupView()
        setupAction()
    }

    private fun setData() {
        val storyAdapter = AdapterStory { story ->
            val intent = Intent(this, DetailStoryActivity::class.java).apply {
                putExtra(DetailStoryActivity.EXTRA_ID, story.id)
            }
            startActivity(intent)
        }

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }

        )
        viewModel.story.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                viewModel.logout()
                navigateToLogin()
                true
            }

            R.id.menu_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> false
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupAction() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
