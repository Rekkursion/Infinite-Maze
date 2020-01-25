package com.rekkursion.infinitemaze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rekkursion.infinitemaze.R
import com.rekkursion.infinitemaze.utils.Maze
import com.rekkursion.infinitemaze.utils.Point
import com.rekkursion.infinitemaze.view.MazeView

class MazeActivity: AppCompatActivity() {
    private lateinit var mMazeView: MazeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        initViews()
    }

    private fun initViews() {
        mMazeView = findViewById(R.id.maze_view)
        mMazeView.setMaze(25, 25)
    }
}
