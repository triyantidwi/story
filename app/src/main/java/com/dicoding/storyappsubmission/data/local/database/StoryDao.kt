package com.dicoding.storyappsubmission.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @androidx.room.Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @androidx.room.Query("DELETE  FROM story")
    suspend fun deleteAll()
}