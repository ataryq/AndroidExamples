package com.example.custom_ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes

class CustomHintButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr), CustomImageButton.OnPressButtonListener {
    private lateinit var hintTextView: TextView
    private lateinit var hintImageButton: CustomImageButton
    private var hintText: String? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.CustomHintButton) {
            hintText = getString(R.styleable.CustomHintButton_hintText)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        inflate(context, R.layout.hint_button, this)
        hintTextView = findViewById(R.id.hint_text)
        if(hintText != null) hintTextView.text = hintText
        hintImageButton = findViewById(R.id.hint_image_button)
        hintImageButton.onPressedListener = this
        pressed(false)
    }

    override fun pressed(pressed: Boolean) {
        hintTextView.visibility = if(pressed) VISIBLE else INVISIBLE
    }
}