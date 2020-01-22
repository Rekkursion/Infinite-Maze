package com.rekkursion.infinitemaze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var mBtnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        mBtnStart = findViewById(R.id.btn_start_at_main_activity)
        mBtnStart.setOnClickListener {

        }
    }
}
