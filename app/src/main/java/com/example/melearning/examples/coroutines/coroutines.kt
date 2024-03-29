package com.example.melearning.examples.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

fun main() { // this: CoroutineScope
//    Coroutines2().asynchronousRunning()
    runCoroutinesSequentially()
}

fun runCoroutinesSequentially() {
    CoroutineScope(Dispatchers.IO).launch {
        delay(100)
        println("1")
        withContext(Dispatchers.Default) {
            delay(100)
            println("2")
        }
        println("3")
    }
    Thread.sleep(500)
}

class Coroutines2 {

    fun run2() {
        runBlocking {
            println("Calculation result: ${structuredConcurrency()}")
        }
    }

    suspend fun nonCancelable() {
        coroutineScope {
            launch(NonCancellable) {

            }
        }
    }

    private val exceptionHandler = CustomCoroutineExceptionHandler()

    suspend fun launchWithHandler() {
        coroutineScope {
            launch(exceptionHandler.getHandler()) {
                //some code
            }
        }
    }

    /**
     * coroutineScope will wait until all child processes are not finished
     */
    private suspend fun structuredConcurrency(): Int {
        var data1 = 0
        var data2 = 0

        coroutineScope {
            launch(IO) {
                data1 = downloadUserData1()
            }
            launch(IO) {
                data2 = downloadUserData2()
            }
        }

        return data1 + data2
    }

    /**
     * await - force to wait util the result is available
     * async - run code as non blocking
     */
    suspend fun unstructuredConcurrency() {
        CoroutineScope(Dispatchers.IO).launch {
            val startTime = System.currentTimeMillis()

            val res1 = async { downloadUserData1() }
            val res2 = async { downloadUserData2() }

            println("sum: ${res1.await() + res2.await()}")
            println("Time: ${System.currentTimeMillis() - startTime}")
        }
    }

    fun asynchronousRunning() {
        CoroutineScope(Dispatchers.Default).launch {
            withContext(IO) {
                println(downloadUserData1())
            }
            downloadUserData2()
        }
    }

    private suspend fun downloadUserData1(): Int {
        delay(3000)
        return 1
    }

    private suspend fun downloadUserData2(): Int {
        delay(3000)
        return 2
    }
}
