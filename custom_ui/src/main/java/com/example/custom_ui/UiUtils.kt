package com.example.custom_ui

import android.content.Context
import androidx.annotation.AttrRes
import androidx.core.content.res.getColorOrThrow

@Suppress("MemberVisibilityCanBePrivate")
class UiUtils {
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
    }
}