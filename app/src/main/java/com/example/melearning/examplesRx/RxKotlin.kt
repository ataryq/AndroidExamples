package com.example.melearning.examplesRx

fun main() {
    //RxJavaSimple().main(RxKotlin())
    RxJavaRetrofit().main(RxKotlin())
}

class RxKotlin {
    public fun log(msg: String) {
        println(msg)
    }
}