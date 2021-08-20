@file:Suppress("unused")

package com.example.melearning.ui_utils

import android.animation.Animator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AnimationUtils

fun View.startAnimationFromId(animId: Int) {
    startAnimation(AnimationUtils.loadAnimation(context, animId))
}

fun ViewPropertyAnimator.doOnEnd(function: () -> Unit): ViewPropertyAnimator {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            function()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    })
    return this
}

fun ViewPropertyAnimator.doOnStart(function: () -> Unit): ViewPropertyAnimator {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            function()
        }

        override fun onAnimationEnd(animation: Animator?) {

        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    })
    return this
}

fun ViewPropertyAnimator.doOnRepeat(function: () -> Unit): ViewPropertyAnimator {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {

        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
            function()
        }
    })
    return this
}