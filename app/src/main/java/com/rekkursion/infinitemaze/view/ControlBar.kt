package com.rekkursion.infinitemaze.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageButton
import com.rekkursion.infinitemaze.R
import java.util.*

enum class ArrowType(val dir: Pair<Int, Int>) {
    UP(Pair(-1, 0)), RIGHT(Pair(0, 1)), DOWN(Pair(1, 0)), LEFT(Pair(0, -1))
}

class ControlBar(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    // the context of this view
    private val mContext = context

    // the 4 arrow-keys (up, right, down, left)
    private lateinit var mImgbtnArrows: Array<AppCompatImageButton>

    // the listener for clicking the arrow keys
    private var mOnArrowKeyClickListener: OnArrowKeyClickListener? = null

    // the timer for the event of clicking the arrow-key
    private var mArrowKeyClickTimer: Timer? = Timer()

    // for the primary constructor
    init {
        LayoutInflater.from(mContext).inflate(R.layout.view_control_bar, this)
        initViews()
        initEvents()
    }

    /* ================================================================ */

    // initialize views
    private fun initViews() {
        // initialize the array of the arrows' img-btns
        mImgbtnArrows = arrayOf(
            findViewById(R.id.imgbtn_up_arrow),
            findViewById(R.id.imgbtn_right_arrow),
            findViewById(R.id.imgbtn_down_arrow),
            findViewById(R.id.imgbtn_left_arrow)
        )
    }

    // initialize events of views
    private fun initEvents() {
        // set the listener to all arrow keys
        mImgbtnArrows.forEachIndexed { index, imgbtnArrow ->
            imgbtnArrow.setOnClickListener {
//                mOnArrowKeyClickListener?.onArrowKeyClick(ArrowType.values()[index])
            }
            imgbtnArrow.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    // pressed (key down)
                    MotionEvent.ACTION_DOWN -> {
                        mArrowKeyClickTimer = Timer()
                        mArrowKeyClickTimer?.scheduleAtFixedRate(object: TimerTask() {
                            override fun run() {
                                mOnArrowKeyClickListener?.onArrowKeyClick(ArrowType.values()[index])
                            }
                        }, 0L, 100L)
                    }

                    // released (key up)
                    MotionEvent.ACTION_UP -> {
                        mArrowKeyClickTimer?.cancel()
                        mArrowKeyClickTimer?.purge()
                        mArrowKeyClickTimer = null
                    }
                }

                true
            }
        }
    }

    // set the on-arrow-key-click-listener
    fun setOnArrowKeyClickListener(onArrowKeyClickListener: OnArrowKeyClickListener) {
        mOnArrowKeyClickListener = onArrowKeyClickListener
    }

    /* ================================================================ */

    // the interface for clicking the arrow keys
    interface OnArrowKeyClickListener {
        fun onArrowKeyClick(arrowType: ArrowType)
    }
}