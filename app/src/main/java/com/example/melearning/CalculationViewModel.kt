package com.example.melearning

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

typealias CalculationInfoEvent = MutableLiveData<Event<CalculationHistoryDb.CalculationInfo>>

class CalculationFragmentViewModelFactory(private val context: CalculationFragment,
                                          private val mDb: DataProvider): ViewModelProvider.Factory
{
    companion object {
        fun createModel(context: CalculationFragment,
                        db: DataProvider): CalculationViewModel {
            val factory = CalculationFragmentViewModelFactory(context, db)
            return ViewModelProvider(context, factory).get(CalculationViewModel::class.java)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CalculationViewModel::class.java)) {
            return CalculationViewModel(context, mDb) as T
        }
        throw IllegalArgumentException("Class for model view is unrecognized")
    }
}

class CalculationViewModel(private val mFragment: CalculationFragment,
                           private val mDb: DataProvider)
    : ViewModel(), Observer<String>
{
    val mPercent = MutableLiveData("12")
    val mStartSum = MutableLiveData("1000")
    val mPeriods = MutableLiveData("60")
    val mIncome = MutableLiveData("100")
    val mResult = MutableLiveData("0")
    private val mUpdateCalcInfoHandler: CalculationInfoEvent

    init {
        mUpdateCalcInfoHandler = MutableLiveData(Event(getCalculationInfo()))
        initUpdateCalcInfoHandler()
        setObserver()
    }

    private fun initUpdateCalcInfoHandler() {
        mUpdateCalcInfoHandler.observe(mFragment, {
            setCalculationInfo(it.peekContent())
        })
    }

    private fun setObserver() {
        mPercent.observe(mFragment, this)
        mStartSum.observe(mFragment, this)
        mPeriods.observe(mFragment, this)
        mIncome.observe(mFragment, this)
    }

    override fun onChanged(t: String?) {
        calculate()
    }

    fun saveCalculationToHistory() {
        mDb.insertAll(getCalculationInfo())
    }

    fun setCalculationInfo(info: CalculationHistoryDb.CalculationInfo) {
        mPercent.value = info.percent.toString()
        mPeriods.value = info.periods.toString()
        mStartSum.value = info.initial.toString()
        mIncome.value = info.income.toString()
    }

    private fun calculate() {
        val resultNum: Double = calculate(getCalculationInfo())
        mResult.value = String.format("%.2f", resultNum)
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