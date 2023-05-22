package com.fakhrimf.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.storyapp.ui.register.RegisterFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RegisterFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        supportFinishAfterTransition()
    }
}