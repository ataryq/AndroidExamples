package com.example.melearning

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnClickListener {
    private val mAdapter = RecycleViewAdapter(this)

    override fun click(info: CalculationInfo?) {
        if(info != null)
            restoreCalculationFrom(info)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter.data = arrayListOf(
            CalculationInfo(20, 20, 20, 20 ),
            CalculationInfo(20, 20, 20, 20 ),
            CalculationInfo(30, 20, 20, 20 )
        )
        historyCalculations.adapter = mAdapter
    }



    private fun parseStringToNumber(text: EditText): Int {
        var percentText = text.text.toString();
        return Integer.parseInt(percentText)
    }

    fun getCalculationInfo(): CalculationInfo {
        var percent: Int = parseStringToNumber(percentET)
        var periods: Int = parseStringToNumber(numPeriodsET)
        var intital: Int = parseStringToNumber(startSumET)
        var income:Int = parseStringToNumber(incomeET)

        println("percent: $percent, periods: $periods, startAmount: $intital, income: $income")
        return CalculationInfo(percent, periods, intital, income)
    }

    fun calculate(v: View) {
        var result: Double = calculate(getCalculationInfo())
        resultTV.text = cutTheNumber(2, result).toString()
        saveCalculationToHistory()
    }

    fun saveCalculationToHistory() {
        mAdapter.data.add(getCalculationInfo())
        mAdapter.notifyDataSetChanged()
    }

    fun restoreCalculationFrom(info: CalculationInfo) {
        percentET.setText(info.mPercent.toString())
        numPeriodsET.setText(info.mPeriods.toString())
        startSumET.setText(info.mInitial.toString())
        incomeET.setText(info.mIncome.toString())
    }
}