package com.example.melearning

import kotlin.math.pow

data class CalculationInfo(var mPercent:Int, var mPeriods:Int, var mInitial:Int, var mIncome:Int)

fun cutTheNumber(saveNumberAfterDot:Int, number:Double): Double {
    var mult: Double = 10.0.pow(saveNumberAfterDot)
    return (number * mult).toInt().toDouble() / mult
}

fun calculate(info: CalculationInfo): Double {
    return calculate(info.mPercent, info.mPeriods, info.mInitial, info.mIncome)
}

fun calculate(percent:Int, periods:Int, initial:Int, income:Int): Double {
    var result: Double = initial.toDouble()

    for(i in 1 .. periods) {
        result *= 1.0 + percent.toDouble() / 100.0
        result += income
    }

    return result
}