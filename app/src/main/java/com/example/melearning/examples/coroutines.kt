package com.example.melearning.examples

import kotlinx.coroutines.*

fun diffTime(prevTime:Long): Long {
    return System.currentTimeMillis() - prevTime
}

suspend fun main() { // this: CoroutineScope

    var startTime = System.currentTimeMillis()

    var thread = runBlocking {

        launch {
            delay(200L)
            println("Task from runBlocking - ${diffTime(startTime)}")
        }

        coroutineScope { // Creates a coroutine scope
            launch {
                delay(500L)
                println("Task from nested launch - ${diffTime(startTime)}")
            }

            delay(100L)
            println("Task from coroutine scope - ${diffTime(startTime)}") // This line will be printed before the nested launch
        }

    }


    println("Coroutine scope is over - ${diffTime(startTime)}") // This line is not printed until the nested launch completes

    //thread.join()
    delay(2000L)
}