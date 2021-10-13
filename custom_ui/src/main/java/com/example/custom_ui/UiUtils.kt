package com.example.custom_ui

import android.content.Context
import android.os.Handler
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.res.getColorOrThrow

@Suppress("MemberVisibilityCanBePrivate", "unused")
class UiUtils(private val context: Context) {
    companion object {
        fun getPrimaryColor(context: Context): Int {
            return getAttributeColor(R.attr.colorPrimary, context)
        }

        fun getAccentColor(context: Context): Int {
            return getAttributeColor(R.attr.colorAccent, context)
        }

        fun getAttributeColor(@AttrRes colorId: Int, context: Context): Int {
            val a = context.obtainStyledAttributes(null, intArrayOf(colorId))
            val primaryColor = a.getColorOrThrow(0)
            a.recycle()
            return primaryColor
        }

        fun postMotionEvent(view: View, typeEvent: Int, x: Float = 0f, y: Float = 0f) {
            view.dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    typeEvent,
                    x,
                    y,
                    0
                )
            )
        }

        fun imitateTouch(view: View) {
            Handler().post {
                postMotionEvent(view, MotionEvent.ACTION_DOWN)
                postMotionEvent(view, MotionEvent.ACTION_UP)
            }
        }

    }

    fun getPrimaryColor() = getPrimaryColor(context)
    fun getAccentColor() = getAccentColor(context)
    fun getAttributeColor(@AttrRes colorId: Int) = getAttributeColor(colorId, context)
}