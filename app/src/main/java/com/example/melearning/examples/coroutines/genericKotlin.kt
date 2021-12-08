package com.example.melearning.examples.coroutines

interface Base {
    val number: Int
    fun doSomething()
}

open class Parent(override val number: Int): Base {
    override fun doSomething() {
        println("number: $number")
    }
}

class Child(base: Base): Base by base {

}

fun main() {
    val p = Parent(5)
    Child(p).doSomething()
}