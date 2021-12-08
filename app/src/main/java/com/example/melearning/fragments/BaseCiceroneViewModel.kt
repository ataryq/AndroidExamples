package com.example.melearning.fragments

import androidx.lifecycle.ViewModel
import com.example.melearning.examples.coroutines.CoroutineSingleController

class BaseCiceroneViewModel: ViewModel() {
    var controlledRunner = CoroutineSingleController<List<Int>>()

    suspend fun doSomething() {
        controlledRunner.runSingle {
            println("runSingle")
        }
    }
}