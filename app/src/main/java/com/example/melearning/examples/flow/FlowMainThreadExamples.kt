package com.example.melearning2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

fun startFlow() {
//    liveDataBlockTest()

}

fun liveDataBlockTest() {
    runBlocking {
        serverResponse.observeForever {
            println(it)
        }
    }
}

private val userId: LiveData<Int> = MutableLiveData(0)

private val serverResponse = userId.switchMap { id ->
    liveData {
        emit(requestServerResponse(id))
    }
}

suspend fun requestServerResponse(id: Int): String {
    delay(1000)
    return "Server response for $id: successful"
}
