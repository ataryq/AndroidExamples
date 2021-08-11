package com.example.melearning

import com.example.melearning.CalculationHistoryDb.CalculationInfo

fun calculate(info: CalculationInfo): Double {
    return calculate(info.percent, info.periods, info.initial, info.income)
}

fun calculate(percent:Double, periods:Double, initial:Double, income:Double): Double {
    var result: Double = initial

    for(i in 1 .. periods.toInt()) {
        result *= 1.0 + percent / 100.0
        result += income
    }

    return result
}