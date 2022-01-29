package com.example.melearning.examples.flow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlin.system.measureTimeMillis

fun threadLog(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun startKotlinFlowTests() = CoroutineScope(Dispatchers.Default).launch {
    val time = measureTimeMillis {
        simpleFlow()
    }

    println("time: $time")
}

fun channelExample() = runBlocking {
    val channel = Channel<Int>(4) // create buffered channel

    launch {
        for (i in 0 .. 6) channel.send(i)
        channel.close()
    }

    delay(100)

    for(i in channel) println(i)
}

fun stateFlowExample() = runBlocking {
    val stateFlow = MutableStateFlow(0)

    stateFlow.emit(1)

    val job = launch {
        stateFlow.collect {
            println("stateFlow: $it")
        }
    }

    delay(50)

    stateFlow.emit(2)

    delay(50)

    job.cancel()
}

fun sharedFlowsExample() {
    println("flowsExample")

    val sharedFlow = MutableSharedFlow<Int>()

    runBlocking {
        val job = launch {
            sharedFlow.collectLatest {
                println(it)
            }
        }

        sharedFlow.emit(0)

        delay(100)

        sharedFlow.emit(1)

        job.cancel()
    }
}


suspend fun simpleFlow() {
    val numberStrings = flowOf("first", "second", "third", "fourth", "fifth")
    simple()
//        .flowOn(Dispatchers.Default) //run the code in the thread
//        .buffer() //start load next element immediately
        .conflate() //skip current values if more recent ones came
//        .filter { it % 2 == 0 || it == -1 }
//        .onEach { if(it == 5) throw Exception() } //throw an error
        .transform { //can emit elements
            emit(
                if(it == -1) "Loading"
                else performRequest(it)
            )
        }
        .map { input -> input }
        .flatMapConcat{ flow { emit(it) } }
//        .catch { e -> emit("Caught $e") } // catch an error
//       .take(4) //after 4 taken values stops
        .zip(numberStrings) {a, b -> "$a -> $b"} //combine two arrays
//      .collectLatest {  } // drop old values and immediately process a new one
        .onCompletion {
            if(it != null) println("Complete with error")
            else println("Complete successful")
        }
        .collect { //
            threadLog("flow: $it")
        }
}

suspend fun performRequest(request: Int): String {
    delay(500) // imitate long-running asynchronous work
    return "response $request"
}

fun simple(): Flow<Int> = flow {
    for (i in 1..5) {
        threadLog("start process value: $i")
//        emit(-1)
        delay(200)
        emit(i)
    }
}

class KotlinFlow(textDispatcher: MutableLiveData<String>) {
    companion object {
        @ExperimentalCoroutinesApi
        fun startStateFlowTests() {
            val textDispatcher = MutableLiveData<String>()
            val formatText = KotlinFlow(textDispatcher)

            //flow not working I don't know why
            //need investigate "flow" in android
            formatText.resultFlow.onSubscription { println("resultFlow.onSubscription") }
            formatText.resultFlow.onStart { println("resultFlow.onStart") }
            formatText.resultFlow.onEach {
                println(it)
            }
        }
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val textObserver = textDispatcher.map { text -> "send text: $text" }.asFlow()

    @ExperimentalCoroutinesApi
    val resultFlow = textObserver.mapLatest { newUserId ->
        flow { emit("flow: $newUserId".apply {
            println("resultFlow: $this")
        })
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = "0"
    )
}