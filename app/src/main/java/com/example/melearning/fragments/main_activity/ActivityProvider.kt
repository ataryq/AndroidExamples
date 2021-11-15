package com.example.melearning.fragments.main_activity

import androidx.appcompat.widget.Toolbar

interface ActivityProvider {
    fun getToolbar(): Toolbar?
    fun setBackInterceptor(callback: () -> Unit)
    fun removeBackInterceptor()
}