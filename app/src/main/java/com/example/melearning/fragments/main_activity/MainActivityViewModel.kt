package com.example.melearning.fragments.main_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    val backIconVisible = MutableLiveData(false)
    var mapSharedData = mapOf<String, Any>()
}