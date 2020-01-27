package com.rekkursion.infinitemaze.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.core.content.res.ResourcesCompat
import com.rekkursion.infinitemaze.R
import com.rekkursion.infinitemaze.activity.MazeActivity
import com.rekkursion.infinitemaze.utils.BlockType
import com.rekkursion.infinitemaze.utils.Maze
import com.rekkursion.infinitemaze.utils.Point
import java.util.logging.Handler
import kotlin.math.max
import kotlin.math.min

open class MazeView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    companion object {
        // the margin of the blocks
        private const val MARGIN_OF_BLOCKS = 23.3F

        // the number of columns, i.e., blocks, on the screen
        private const val NUMBER_OF_COLS_ON_SCREEN = 15

        // the text size of the showing current location
        private const val TEXT_SIZE = 75.0F

        // the ratio between block-size and gap
        private const val RATIO_BETWEEN_BLOCK_SIZE_AND_GAP = 30.0F
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

    // region set the maze
    fun setMaze(
        level: Int,
        width: Int,
        height: Int,
        isClosed: Boolean = true,
        startLoc: Point<Int> = if (isClosed) Point(1, 1) else Point(0, 0),
        endLoc: Point<Int> = if (isClosed) Point(height - 2, width - 2) else Point(height - 1, width - 1),
        randomStartAndEndLocs: Boolean = true
    ) {
        // region determine the start location & end location
        // for randomly choose the start location & end location
        val rInt = kotlin.random.Random(System.currentTimeMillis())
        // determine the start location if shall be random
        val sLoc =
            if (randomStartAndEndLocs)
                Point(rInt.nextInt(if (isClosed) 1 else 0, if (isClosed) height - 1 else height), rInt.nextInt(if (isClosed) 1 else 0, if (isClosed) width - 1 else width))
            else
                startLoc
        // determine the end location if shall be random
        var eLoc =
            if (randomStartAndEndLocs)
                Point(rInt.nextInt(if (isClosed) 1 else 0, if (isClosed) height - 1 else height), rInt.nextInt(if (isClosed) 1 else 0, if (isClosed) width - 1 else width))
            else
                endLoc
        // the end location mustn't be the same as start location
        while (eLoc == sLoc)
            eLoc =
                if (randomStartAndEndLocs || endLoc == sLoc)
                    Point(rInt.nextInt(if (isClosed) 1 else 0, if (isClosed) height - 1 else height), rInt.nextInt(if (isClosed) 1 else 0, if (isClosed) width - 1 else width))
                else
                    endLoc
        // endregion

        // create the maze-model
        mMazeModel = Maze.Builder()
            .setLevel(level)
            .setSize(width, height)
            .setIsClosed(isClosed)
            .setStartLocation(sLoc.y, sLoc.x)
            .setEndLocation(eLoc.y, eLoc.x)
            .create()
        makeMove(0, 0)
        Log.e("MAZE", mMazeModel.toString())

        // re-render
        invalidate()
    }
    // endregion

    // move
    fun makeMove(dy: Int, dx: Int): Boolean {
        mMazeModel?.let { maze ->
            // move the current location
            maze.moveCurrentLocation(dy, dx)

            // move the camera
            if (maze.width > NUMBER_OF_COLS_ON_SCREEN) {
                // for moving the camera, first compute the center of width, e.g., 15 / 2 = 7
                val centerOfWidth = NUMBER_OF_COLS_ON_SCREEN / 2

                // calculate the new location of the camera
                val newCameraX = when {
                    maze.curX <= centerOfWidth -> 0
                    maze.height - maze.curX - 1 <= centerOfWidth -> maze.height - NUMBER_OF_COLS_ON_SCREEN
                    else -> maze.curX - centerOfWidth
                }
                val newCameraY = when {
                    maze.curY <= centerOfWidth -> 0
                    maze.width - maze.curY - 1 <= centerOfWidth -> maze.width - NUMBER_OF_COLS_ON_SCREEN
                    else -> maze.curY - centerOfWidth
                }
                // set the location of the camera
                mCamera.set(newCameraY, newCameraX)
            }

            // re-render
            invalidate()

            // check if reaches the end location
            if (maze.curLocCopied == maze.endLocCopied)
                return true
        }

        return false
    }

    private fun renderMaze(canvas: Canvas) {
        // if the maze-model is not a null
        mMazeModel?.let { maze ->
            // calculate the block's size
            val blockSize = (width - 2 * MARGIN_OF_BLOCKS) / min(maze.width, NUMBER_OF_COLS_ON_SCREEN)
            // calculate the gap among blocks (block-padding)
            val gap = blockSize / RATIO_BETWEEN_BLOCK_SIZE_AND_GAP
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
                            j * blockSize + gap + MARGIN_OF_BLOCKS,
                            k * blockSize + gap + MARGIN_OF_BLOCKS,
                            (j + 1) * blockSize - gap + MARGIN_OF_BLOCKS,
                            (k + 1) * blockSize - gap + MARGIN_OF_BLOCKS
                        )

                        // set the color
                        mPaint.color =
                            if (rowIdx == maze.curY && colIdx == maze.curX)
                                BlockType.CURRENT.colorValueOnMazeView
                            else
                                blocks[rowIdx][colIdx].colorValueOnMazeView

                        // render
                        canvas.drawRect(rect, mPaint)
                    }
                }
            }
        } // end of maze-model?.let
    }

    private fun renderCoordinates(canvas: Canvas) {
        // if the maze-model is not a null
        mMazeModel?.let { maze ->
            // calculate the text's bounds to get the length of the to-be-drawn text
            val text = maze.curLocCopied.toString()
            val textBound = Rect()
            mPaint.getTextBounds(text, 0, text.length, textBound)

            // render the text of the current location: (y, x)
            mPaint.color = Color.BLACK
            canvas.drawText(text, width - textBound.width() - MARGIN_OF_BLOCKS, width.toFloat() + TEXT_SIZE / 2.0F + MARGIN_OF_BLOCKS, mPaint)
        } // end of maze-model?.let
    }

    private fun renderLevel(canvas: Canvas) {
        mMazeModel?.let { maze ->
            maze.level?.let { level ->
                mPaint.color = Color.BLACK
                canvas.drawText("Level $level", MARGIN_OF_BLOCKS, width.toFloat() + TEXT_SIZE / 2.0F + MARGIN_OF_BLOCKS, mPaint)
            }
        } // end of maze-model?.let
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
            renderMaze(canvas)
            renderCoordinates(canvas)
            renderLevel(canvas)
        } // end of canvas?.let
    }
}