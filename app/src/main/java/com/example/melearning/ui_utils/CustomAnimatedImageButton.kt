package com.example.melearning.ui_utils

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnResume

class CustomAnimatedImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CustomImageButton(context, attrs, defStyleAttr)
{
    private var changeColorAnimation: ValueAnimator = createAnimation()

    override fun pressed(isPressed: Boolean) {
        super.pressed(isPressed)

        if(isPressed)
            changeColorAnimation.reverse()
        else
            changeColorAnimation.start()
    }

    private fun createAnimation(): ValueAnimator {
        val colorAnimation = ValueAnimator.ofArgb(defaultColor, pressedColor)
        colorAnimation.duration = 150
        colorAnimation.addUpdateListener {
            animator -> setColor(animator.animatedValue as Int)
        }
        colorAnimation.doOnEnd {}
        colorAnimation.doOnResume {}
        return colorAnimation
    }
}