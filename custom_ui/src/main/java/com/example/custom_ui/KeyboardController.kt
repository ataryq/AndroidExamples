package com.example.custom_ui

import android.annotation.SuppressLint

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

class KeyboardController {
    companion object {
        @SuppressLint("StaticFieldLeak")
        val instance = KeyboardController()
    }

    private lateinit var activity: Activity
    private var isKeyboardOpened = MutableLiveData(false)

    fun initialize(activity: AppCompatActivity) {
        this.activity = activity
        setupKeyboardListener()
    }

    fun closeKeyboard() {
        activity.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    fun closeKeyboardFrom(view: View) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideHardKeyboard() {
        if(isKeyboardOpened.value == false)
            return

        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun showHardKeyboard() {
        if(isKeyboardOpened.value == true)
            return

        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun isKeyboardOpened() = isKeyboardOpened

    private fun Activity.getActivityRoot(): View {
        return (findViewById<ViewGroup>(R.id.content)).getChildAt(0)
    }

    private fun setupKeyboardListener() {
        val activityRootView = activity.getActivityRoot()

        activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            activityRootView.getWindowVisibleDisplayFrame(r)
            val heightDiff = activityRootView.rootView.height - r.height()
            val newIsKeyboardOpened = heightDiff > 0.25 * activityRootView.rootView.height
            if(isKeyboardOpened.value != newIsKeyboardOpened) {
                isKeyboardOpened.postValue(newIsKeyboardOpened)
            }
        }
    }
}