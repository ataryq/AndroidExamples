package com.example.melearning.ui_utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.example.melearning.R

open class CustomImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CustomImageClickable(context, attrs, defStyleAttr)
{
    interface OnPressButtonListener {
        fun pressed(pressed: Boolean)
    }

    protected var defaultColor: Int = Color.BLUE
    protected var pressedColor: Int = Color.CYAN

    var onPressedListener: OnPressButtonListener? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.CustomButtonUploadHistory) {
            pressedColor = getColor(R.styleable.CustomButtonUploadHistory_pressedColor, pressedColor)
            defaultColor = getColor(R.styleable.CustomButtonUploadHistory_defaultColor, defaultColor)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        pressed(false)
    }

    protected fun setColor(color: Int) {
        setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun pressed(isPressed: Boolean) {
        onPressedListener?.pressed(isPressed)
    }
}