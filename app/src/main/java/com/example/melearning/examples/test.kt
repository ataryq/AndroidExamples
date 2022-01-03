package com.example.melearning.examples

import java.util.*

val number: Int? = null

fun main1() {
    println("Hello, world!")

    val constValue = 0;
    var notConstValue = "1";

    val onlyInt: Short = 30000
    var firCharInStr: Char = notConstValue[0]
    val resultDouble: Double = 5.0 / 3
    println(resultDouble)

    when("spring1") {
        "spring" -> println("Season number: " + 1)
        "summer" -> println("Season number: " + 1)
        "winter" -> println("Season number: " + 1)
        else -> println("Invalid season")
    }

    val month = 999;
    when(month) {
        !in 0..1000 -> println("The number is invalid")
        12, in 1..2 -> println("winter")
        in 3..5 -> println("spring")
        else -> println("Invalid season")
    }

    val x: Any = 's'

    when(x) {
        is Int -> println("It's int")
        !is Double -> println("It's not double")
        else -> println("It's double")
    }

    for (num in 1..10) print("$num ")
    println("")
    for (num in 10 downTo 1 step 2) print("$num ")
    println("")
    for (num in 10.downTo(1).step(2)) print("$num ")
    println("")

    println("5 + 4 = ${sum(5, 4)}")

    var name: String? = null
    println(name?.toUpperCase(Locale.ROOT))
    // execute code only if name not null
    name?.let { println("Name length ${name?.length}") }
    //name!!.toLowerCase(); // runtime error

    val constName = name ?: "Gohan2"
    println("constName: $constName")

    name = "Gohan"
    println(name.toUpperCase(Locale.ROOT))
    name.let { println("Name length: ${name.length}") }

    val constName2 = name ?: "Gohan2"
    println("constName2: $constName2")

    val innerClass = ClassA("Goshan").ClassInnerA().sayHello()
    ClassA.ClassNestedA()
}

fun sum(a: Int, b: Int): Int {
    return a + b;
}

class ClassA(private val name: String) {

    inner class ClassInnerA {
        fun sayHello() = println("Hello $name")
    }

    class ClassNestedA {
        fun sayHello() = println("Hello")
    }
}