package com.rekkursion.infinitemaze.manager

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.Toast

object AnimationManager {
    // to store the views which have started any animation-set
    private val mAddedSubjects = HashSet<View>()

    // build an animation-set by animation ids
    fun buildAnimationSet(context: Context, vararg ids: Int): AnimationSet {
        val animSet = AnimationSet(true)
        ids.forEach { id -> animSet.addAnimation(AnimationUtils.loadAnimation(context, id)) }
        return animSet
    }

    // build and start an animation-set immediately
    fun startAnimationSet(subject: View, vararg ids: Int) {
        val animSet = buildAnimationSet(subject.context, *ids)
        subject.startAnimation(animSet)
    }

    // remove all the animation-sets of a view
    fun endAnimationSet(subject: View) {
        val found = mAddedSubjects.find { it == subject }
        found?.clearAnimation()
        mAddedSubjects.remove(found)
    }
}