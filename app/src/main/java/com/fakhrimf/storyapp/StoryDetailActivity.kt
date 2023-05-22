package com.fakhrimf.storyapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.fakhrimf.storyapp.model.PostModel
import com.fakhrimf.storyapp.ui.story.StoryDetailFragment
import com.fakhrimf.storyapp.utils.STORY_INTENT

class StoryDetailActivity : AppCompatActivity() {

    fun getStory(): PostModel{
        return intent.getParcelableExtra<PostModel>(STORY_INTENT)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, StoryDetailFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        supportFinishAfterTransition()
    }
}