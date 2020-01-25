package com.rekkursion.infinitemaze.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.rekkursion.infinitemaze.R
import com.rekkursion.infinitemaze.utils.Maze
import com.rekkursion.infinitemaze.utils.Point
import kotlin.math.max
import kotlin.math.min

class MazeView(context: Context, attrs: AttributeSet?): View(context, attrs) {
    companion object {
        // the margin of the blocks
        private const val MARGIN_OF_BLOCKS = 23.3F

        // the number of columns, i.e., blocks, on the screen
        private const val NUMBER_OF_COLS_ON_SCREEN = 15

        // the text size of the showing current location
        private const val TEXT_SIZE = 75.0F
    }

    /* ================================================================ */

    // the context of this view
    private val mContext = context

    // the model of the maze
    private var mMazeModel: Maze? = null

    // the location of the camera
    private val mCamera = Point(0, 0)

    // the paint for rendering
    private val mPaint = Paint()

    init {
        mPaint.textSize = TEXT_SIZE
        mPaint.typeface = ResourcesCompat.getFont(mContext, R.font.consola)
    }

    /* ================================================================ */

    // set the maze
    fun setMaze(
        width: Int,
        height: Int,
        isClosed: Boolean = true,
        startLoc: Point<Int> = if (isClosed) Point(1, 1) else Point(0, 0),
        endLoc: Point<Int> = if (isClosed) Point(height - 2, width - 2) else Point(height - 1, width - 1)) {
        mMazeModel = Maze.Builder()
            .setSize(width, height)
            .setIsClosed(isClosed)
            .setStartLocation(startLoc.y, startLoc.x)
            .setEndLocation(endLoc.y, endLoc.x)
            .create()
        Log.e("MAZE", mMazeModel.toString())
        invalidate()
    }

    /* ================================================================ */

    // for setting the value of height as the same value of width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = max(
            suggestedMinimumWidth + paddingLeft + paddingRight,
            MeasureSpec.getSize(widthMeasureSpec)
        )
        val desiredHeight = desiredWidth + TEXT_SIZE.toInt()
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    // render
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        // if the canvas is not a null
        canvas?.let {
            // if the maze-model is not a null
            mMazeModel?.let { maze ->
                // calculate the block's size
                val blockSize = (width - 2 * MARGIN_OF_BLOCKS) / min(maze.width, NUMBER_OF_COLS_ON_SCREEN)
                // calculate the gap among blocks (block-padding)
                val gap = blockSize / 20.0F
                // get the blocks from the maze-model
                val blocks = maze.blocksCopied

                // iterate by row in the range of camera-y + number-of-cols-on-screen
                for (k in 0 until NUMBER_OF_COLS_ON_SCREEN) {
                    // iterate by column in the range of camera-x + number-of-cols-on-screen
                    for (j in 0 until NUMBER_OF_COLS_ON_SCREEN) {
                        val rowIdx = k + mCamera.y
                        val colIdx = j + mCamera.x

                        // if the axis is in the range of the maze-model's size
                        if (rowIdx < maze.height && colIdx < maze.width) {
                            // create a rect-f for rendering
                            val rect = RectF(
                                colIdx * blockSize + gap + MARGIN_OF_BLOCKS,
                                rowIdx * blockSize + gap + MARGIN_OF_BLOCKS,
                                (colIdx + 1) * blockSize - gap + MARGIN_OF_BLOCKS,
                                (rowIdx + 1) * blockSize - gap + MARGIN_OF_BLOCKS
                            )

                            // set the color determined by the type of this block
                            mPaint.color = blocks[rowIdx][colIdx].colorValueOnMazeView
                            // render
                            canvas.drawRect(rect, mPaint)
                        }
                    }
                }

                // render the text of the current location: (y, x)
                mPaint.color = Color.BLACK
                canvas.drawText(maze.curLocCopied.toString(), MARGIN_OF_BLOCKS, width.toFloat() + TEXT_SIZE / 2.0F + MARGIN_OF_BLOCKS, mPaint)
            } // end of maze-model?.let
        } // end of canvas?.let
    }
}