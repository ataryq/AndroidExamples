package com.example.melearning

private class A {
    private var mNamePrint = "A"

    fun printA() {
        println("A: $mNamePrint")
    }

    class Nested {
        fun print() {
            //has no access
            //print(NamePrint)
            println("Nested")
        }
    }
    inner class Inner {
        fun print() {
            println("Inner: $mNamePrint")
            printA()
        }
    }
}

fun main2() {
    val array: ArrayList<Double> = arrayListOf(1.0, 2.0, 3.0, 4.0, 5.0)

    var average = 0.0
    for (i in array)
        average += i
    average /= array.size
    println("Average of ${array.size} elements is $average")

    var lambdaAdd = {a:Int, b:Int -> a + b }

    println("Sum of 1 and 2 is ${lambdaAdd(1, 2)}")

    A().Inner().print()
    A.Nested().print()
}