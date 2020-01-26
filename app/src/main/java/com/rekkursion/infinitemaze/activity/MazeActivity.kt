package com.rekkursion.infinitemaze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckedTextView
import android.widget.Toast
import com.rekkursion.infinitemaze.R
import com.rekkursion.infinitemaze.utils.Maze
import com.rekkursion.infinitemaze.utils.Point
import com.rekkursion.infinitemaze.view.ArrowType
import com.rekkursion.infinitemaze.view.ControlBar
import com.rekkursion.infinitemaze.view.MazeView

class MazeActivity: AppCompatActivity() {
    // the maze-view
    private lateinit var mMazeView: MazeView

    // the control-bar for arrow keys
    private lateinit var mControlBar: ControlBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        initViews()
        initEvents()
    }

    private fun initViews() {
        mMazeView = findViewById(R.id.maze_view)
        mControlBar = findViewById(R.id.arrow_key_control_bar)
    }

    private fun initEvents() {
        // create the maze
        mMazeView.setMaze(25, 25)

        // events of clicking arrow-keys
        mControlBar.setOnArrowKeyClickListener(object: ControlBar.OnArrowKeyClickListener {
            override fun onArrowKeyClick(arrowType: ArrowType) {
                mMazeView.makeMove(arrowType.dir.first, arrowType.dir.second)
            }
        })
    }
}
