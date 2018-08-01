package com.edu.memory.ui

import android.animation.AnimatorSet
import android.animation.AnimatorInflater
import android.content.Context
import android.view.View
import com.edu.memory.R

/**
 * Created by edu
 */
object FlipAnimator {

    fun flipView(context: Context, back: View?, front: View?, showFront: Boolean) {
        if (back == null || front == null) return

        val mSetRightOut = AnimatorInflater.loadAnimator(context, R.animator.out_animation) as AnimatorSet
        val mSetLeftIn = AnimatorInflater.loadAnimator(context, R.animator.in_animation) as AnimatorSet

        if (showFront) {
            mSetRightOut.setTarget(front)
            mSetLeftIn.setTarget(back)
            mSetRightOut.start()
            mSetLeftIn.start()
        } else {
            mSetRightOut.setTarget(back)
            mSetLeftIn.setTarget(front)
            mSetRightOut.start()
            mSetLeftIn.start()
        }
    }
}