package com.example.custom_ui

import android.view.View
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

open class CustomEditTextWrapper(private var editText: EditText?,
                            lifecycleOwner: LifecycleOwner): Observer<Boolean>,
    View.OnFocusChangeListener {
    private var mOnFocusChangeListener: View.OnFocusChangeListener? = null

    init {
        editText?.onFocusChangeListener = this
        KeyboardController.instance.isKeyboardOpened().observe(lifecycleOwner, this)
    }

    fun clear() {
        editText?.onFocusChangeListener = null
        KeyboardController.instance.isKeyboardOpened().removeObserver(this)
        editText = null
    }

    override fun onChanged(it: Boolean) {
        if(!it && editText?.hasFocus() == true) {
            editText?.clearFocus()
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(!hasFocus && v != null) {
            KeyboardController.instance.closeKeyboardFrom(v)
            mOnFocusChangeListener?.onFocusChange(v, hasFocus)
        }
    }
}