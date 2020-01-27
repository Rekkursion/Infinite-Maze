package com.rekkursion.infinitemaze.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.rekkursion.infinitemaze.utils.BlockType
import com.rekkursion.infinitemaze.utils.Maze
import kotlin.math.max
import kotlin.math.min

// the small-map as a zoomed out perspective view of the corresponding maze-view
class SmallMap(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    // the context of this view
    private val mContext = context

    // the model of the maze
    private var mMazeModel: Maze? = null

    // the paint for rendering
    private val mPaint = Paint()

    /* ================================================================ */

    // update this small-map by an altered maze-model
    fun update(newMazeModel: Maze?) {
        newMazeModel?.let {
            mMazeModel = newMazeModel
            invalidate()
        }
    }

    /* ================================================================ */

    // for setting the value of height as the same value of width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = max(
            suggestedMinimumWidth + paddingLeft + paddingRight,
            MeasureSpec.getSize(widthMeasureSpec)
        )
        val desiredHeight = max(
            suggestedMinimumHeight + paddingTop + paddingBottom,
            MeasureSpec.getSize(heightMeasureSpec)
        )
        val smallerOne = min(desiredWidth, desiredHeight)

        setMeasuredDimension(smallerOne, smallerOne)
    }

    // render
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        // if the canvas is not a null
        canvas?.let {
            // if the maze-model is not a null
            mMazeModel?.let { maze ->
                // calculate the block's size
                val blockSize = width / maze.width
                // get the blocks from the maze-model
                val blocks = maze.blocksCopied

                // iterate by row
                for (k in 0 until maze.height) {
                    // iterate by column
                    for (j in 0 until maze.width) {
                        // create a rect-f for rendering
                        val rect = RectF(
                            j * blockSize.toFloat(),
                            k * blockSize.toFloat(),
                            (j + 1) * blockSize.toFloat(),
                            (k + 1) * blockSize.toFloat()
                        )

                        // set the color
                        mPaint.color =
                            if (k == maze.curY && j == maze.curX)
                                BlockType.CURRENT.colorValueOnMazeView
                            else
                                blocks[k][j].colorValueOnMazeView

                        // render
                        canvas.drawRect(rect, mPaint)
                    }
                }
            } // end of maze-model?.let
        } // end of canvas?.let
    }
}