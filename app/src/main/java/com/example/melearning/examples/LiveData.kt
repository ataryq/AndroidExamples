package com.example.melearning.examples

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

//start from Activity
@ExperimentalCoroutinesApi
fun startLiveDataTest() {
    val textDispatcher = MutableLiveData<String>()
    val formatText = MyFormatText(textDispatcher)
    formatText.result.observeForever {
        println(it)
    }
    textDispatcher.postValue("hello world")
}

class MyFormatText(textDispatcher: MutableLiveData<String>) {
    private val textObserver = textDispatcher.map { text -> "send text: $text" }.asFlow()
    private val resultText = textObserver.asLiveData().switchMap {
        text -> liveData {
            emit("$text!")
        }
    }
    //can observe multiple live data
    val result = MediatorLiveData<String>().apply {
        addSource(resultText) {
            postValue("Mediator: $it")
        }
    }
}

