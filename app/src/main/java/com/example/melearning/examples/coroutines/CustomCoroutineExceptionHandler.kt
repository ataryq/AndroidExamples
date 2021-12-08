package com.example.melearning.examples.coroutines

import invoice.media.data.remote.exceptions.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class CustomCoroutineExceptionHandler(
    private val invalidFieldsException: () -> Unit = {},
    private val notFoundException: () -> Unit = {},
    private val serverConflictException: () -> Unit = {},
    private val serverException: () -> Unit = {},
    private val tooManyRequests: () -> Unit = {},
    private val otherException: () -> Unit = {}
) {
    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(processException)
    }

    private val processException: (CoroutineContext, Throwable) -> Unit = { _, throwable ->
        throwable.printStackTrace()

        when(throwable) {
            is InvalidFieldsException -> invalidFieldsException()
            is NotFoundException -> notFoundException()
            is ServerConflictException -> serverConflictException()
            is ServerException -> serverException()
            is TooManyRequestsException -> tooManyRequests()
            else -> otherException()
        }
    }

    fun getHandler() = exceptionHandler

}