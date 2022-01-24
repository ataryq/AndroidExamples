package com.example.melearning.fragments.paginating_sample

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    override fun onDraw(c: Canvas?) {
        println("CustomRecyclerView: onDraw")
        super.onDraw(c)
    }
}