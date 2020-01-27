package com.rekkursion.infinitemaze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rekkursion.infinitemaze.R
import com.rekkursion.infinitemaze.view.ArrowType
import com.rekkursion.infinitemaze.view.ControlBar
import com.rekkursion.infinitemaze.view.MazeView

class MazeActivity: AppCompatActivity() {
    // the maze-view
    private lateinit var mMazeView: MazeView

    // the control-bar for arrow keys
    private lateinit var mControlBar: ControlBar

    // the counter of the mazes' levels
    private var levelCounter = 1

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
        mMazeView.setMaze(levelCounter, 15, 15)

        // events of clicking arrow-keys
        mControlBar.setOnArrowKeyClickListener(object: ControlBar.OnArrowKeyClickListener {
            override fun onArrowKeyClick(arrowType: ArrowType) {
                moveInMaze(arrowType)
            }
        })

        mMazeView.setOnClickListener {
            mMazeView.setMaze(levelCounter, 15, 15)
        }
    }

    // move in the maze and update the maze-view and small-map
    private fun moveInMaze(arrowType: ArrowType) {
        // make a move
        val reachEndLoc = mMazeView.makeMove(arrowType.dir.first, arrowType.dir.second)

        // if it has already reached the end location of this maze
        if (reachEndLoc) {
            // go to the next level
            ++levelCounter
            // create the maze
            mMazeView.setMaze(levelCounter, 15, 15)
        }
    }
}
