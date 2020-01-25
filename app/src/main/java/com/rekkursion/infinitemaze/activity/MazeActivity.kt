package com.rekkursion.infinitemaze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rekkursion.infinitemaze.R
import com.rekkursion.infinitemaze.utils.Maze

class MazeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        val m = Maze.Builder()
            .setSize(10, 10)
            .setIsClosed(true)
            .setStartLocation()
            .setEndLocation()
            .create()

        Log.e("MAZE", "\n\n$m\n")
    }
}
