package com.rekkursion.infinitemaze.view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.*
import android.widget.Button

class EffectButton(context: Context, attrs: AttributeSet? = null): Button(context, attrs) {
    // the passed context
    private val mContext = context

    /* ================================================================ */

    // build the animation when clicking this effect-button
    private fun createClickedAnimation(): AnimationSet {
        val ret = AnimationSet(true)
        ret.interpolator = AccelerateDecelerateInterpolator()
        ret.duration = 500L
        ret.fillAfter = true

        val scaleAnim = ScaleAnimation(1F, 300F, 1F, 300F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F)
        ret.addAnimation(scaleAnim)

        val fadeOutAnim = AlphaAnimation(1F, 0F)
        ret.addAnimation(fadeOutAnim)

        return ret
    }

    /* ================================================================ */

    // override the set-on-click-listener to play the animation
    override fun setOnClickListener(listener: OnClickListener?) {
        val l = OnClickListener {
            startAnimation(createClickedAnimation())
            listener?.onClick(it)
        }

        super.setOnClickListener(l)
    }
}