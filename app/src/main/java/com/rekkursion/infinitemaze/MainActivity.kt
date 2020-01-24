package com.rekkursion.infinitemaze

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater
import android.widget.Button
import android.widget.Toast
import com.rekkursion.infinitemaze.activity.MazeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mBtnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // set transition animations (returning back)
        window.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.slide_start)
        window.reenterTransition = TransitionInflater.from(this).inflateTransition(R.transition.slide_start)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        mBtnStart = findViewById(R.id.btn_start_at_main_activity)
        mBtnStart.setOnClickListener {
            startActivity(
                Intent(this, MazeActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }
    }
}
