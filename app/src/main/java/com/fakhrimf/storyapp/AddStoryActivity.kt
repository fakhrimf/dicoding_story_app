package com.fakhrimf.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.storyapp.ui.addstory.AddStoryFragment

class AddStoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddStoryFragment.newInstance())
                .commitNow()
        }
    }
}