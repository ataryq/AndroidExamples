package com.example.melearning.fragments.calculation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.melearning.CalculationHistoryDb
import com.example.melearning.DataProvider
import com.example.melearning.Utils

class CalculationViewModel(application: Application):
    AndroidViewModel(application), Observer<String>
{
    var mDb: DataProvider? = null
    val mPercent = MutableLiveData("12")
    val mStartSum = MutableLiveData("1000")
    val mPeriods = MutableLiveData("60")
    val mIncome = MutableLiveData("100")
    val mResult = MutableLiveData("0")

    init {
        setObserver()
    }

    companion object {
        private fun calculate(info: CalculationHistoryDb.CalculationInfo): Double {
            return calculate(info.percent, info.periods, info.initial, info.income)
        }

        private fun calculate(percent:Double, periods:Double, initial:Double, income:Double): Double {
            var result: Double = initial
            val normalizedPercent = 1 + percent / 100.0

            for(i in 1 .. periods.toInt()) {
                result *= normalizedPercent
                result += income
            }

            return result
        }
    }

    //calls at the end of the life activity
    // override fun onCleared() {super.onCleared()}

    private fun setObserver() {
        mPercent.observeForever(this)
        mStartSum.observeForever(this)
        mPeriods.observeForever(this)
        mIncome.observeForever(this)
    }

    override fun onChanged(t: String?) {
        println("onChanged")
        calculate()
    }

    fun saveCalculationToHistory() {
        mDb?.insertAll(getCalculationInfo())
    }

    fun setCalculationInfo(info: CalculationHistoryDb.CalculationInfo) {
        mPercent.value = info.percent.toString()
        mPeriods.value = info.periods.toString()
        mStartSum.value = info.initial.toString()
        mIncome.value = info.income.toString()
    }

    private fun calculate() {
        val resultNum: Double = calculate(getCalculationInfo())
        mResult.postValue(String.format("%.2f", resultNum))
        println("resultNum: $resultNum")
    }

    private fun getCalculationInfo(): CalculationHistoryDb.CalculationInfo {
        val percentNum: Double = Utils.parseStringToNumber(mPercent.value as String)
        val periodsNum: Double = Utils.parseStringToNumber(mPeriods.value as String)
        val initialNum: Double = Utils.parseStringToNumber(mStartSum.value as String)
        val incomeNum:Double = Utils.parseStringToNumber(mIncome.value as String)

        ifEmptySetZero(mPercent)
        ifEmptySetZero(mPeriods)
        ifEmptySetZero(mStartSum)
        ifEmptySetZero(mIncome)

        println("percent: $percentNum, periods: $periodsNum, startAmount: $initialNum, income: $incomeNum")
        return CalculationHistoryDb.CalculationInfo(percentNum, periodsNum, initialNum, incomeNum)
    }

    private fun ifEmptySetZero(text: MutableLiveData<String>) {
        if(text.value.toString().isEmpty())
            text.value = "0"
    }
}