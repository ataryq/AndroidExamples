package com.example.melearning.examples.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun log(msg: String) {
    println("${System.currentTimeMillis() % 100000}: $msg")
}

fun main() {
    runBlocking {
//        testColdAndHotFlows()
        testChannelFlow()
    }
}

suspend fun testChannelFlow() {
    coroutineScope {
        val stateFlow = MutableStateFlow<Int>(1)
        val sharedFlow = MutableSharedFlow<Int>()
        sharedFlow.emit(1)
    }
}

suspend fun testColdAndHotFlows() {
    log("testColdAndHotFlows start")

    coroutineScope {
        val myFlow = flow<Int> {
            for(i in 0 .. 4) {
                log("emit start $i")
                delay(1000)
                log("emit $i")
                emit(i)
            }
        }

        val myStateFlow = myFlow.stateIn(this)

        delay(1000)

        myStateFlow.collect {
            log("$it")
        }

    }
}