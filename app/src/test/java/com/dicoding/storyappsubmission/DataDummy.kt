package com.dicoding.storyappsubmission

import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "id $i",
                "www.photo.com/$i",
                "1-2-$i",
                "name $i",
                "description $i",
                i + 1.toDouble(),
                i + 2.toDouble()
            )
            items.add(story)
        }
        return items
    }
}