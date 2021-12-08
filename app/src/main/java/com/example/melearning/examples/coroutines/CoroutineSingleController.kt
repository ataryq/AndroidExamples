package com.example.melearning.examples.coroutines

import kotlinx.coroutines.*

class CoroutineSingleController<T> {
    private var job: Job? = null
    private var promise: Deferred<T>? = null

    private suspend fun cancel() {
        job?.cancel()
        promise?.cancel()
    }

    suspend fun runSingle(callback: () -> Unit) {
        cancel()
        coroutineScope {
            job = launch {
                callback()
                job = null
            }
        }
    }

    suspend fun runSingleAsync(callback: () -> T): T {
        cancel()

        coroutineScope {
            promise = async {
                callback()
            }
        }

        val result = promise?.await()
        promise = null

        return result!!
    }
}