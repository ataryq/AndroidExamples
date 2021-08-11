package com.example.melearning.ui_utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

@SuppressLint("ClickableViewAccessibility")
abstract class CustomImageClickable @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr)
{
    abstract fun pressed(isPressed: Boolean)

    init {
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnTouchListener { _, moveEvent -> processMotionEvent(moveEvent) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setOnTouchListener(null)
    }

    private fun processMotionEvent(moveEvent: MotionEvent): Boolean {
        println("button: $moveEvent")

        when(moveEvent.action) {
            MotionEvent.ACTION_DOWN -> pressed(true)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                pressed(false)
                performClick()
            }
            else -> return false
        }

        return true
    }

}