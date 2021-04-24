package com.example.melearning

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides

typealias CalculationInfoEvent = MutableLiveData<Event<CalculationHistoryDb.CalculationInfo>>

@Module
class CalculationFragmentViewModelFactory(private val mActivity: AppCompatActivity,
                                          private val mDb: DataProvider): ViewModelProvider.Factory  {
    companion object {
        @ActivityScope
        @Provides
        fun createInstance(activity: AppCompatActivity, db: DataProvider): CalculationFragmentViewModel {
            val factory = CalculationFragmentViewModelFactory(activity, db)
            return ViewModelProvider(activity, factory).get(CalculationFragmentViewModel::class.java)
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CalculationFragmentViewModel::class.java)) {
            return CalculationFragmentViewModel(mActivity, mDb) as T
        }
        throw IllegalArgumentException("Class for model view is unrecognized")
    }
}

class CalculationFragmentViewModel(private val mActivity: AppCompatActivity,
                                   private val mDb: DataProvider)
    : ViewModel(), Observer<String>
{
    val mPercent = MutableLiveData<String>("12")
    val mStartSum = MutableLiveData<String>("1000")
    val mPeriods = MutableLiveData<String>("60")
    val mIncome = MutableLiveData<String>("100")
    val mResult = MutableLiveData<String>("0")
    val mUpdateCalcInfoHandler: CalculationInfoEvent

    init {
        println("CalculationFragmentViewModel created")
        mUpdateCalcInfoHandler = MutableLiveData(Event(getCalculationInfo()))
        initUpdateCalcInfoHandler()
        setObserver()
    }

    private fun initUpdateCalcInfoHandler() {
        mUpdateCalcInfoHandler.observe(mActivity, Observer{
            println("mUpdateCalcInfoHandler event")
            setCalculationInfo(it.peekContent())
        })
    }

    private fun setObserver() {
        mPercent.observe(mActivity, this)
        mStartSum.observe(mActivity, this)
        mPeriods.observe(mActivity, this)
        mIncome.observe(mActivity, this)
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
        var resultNum: Double = calculate(getCalculationInfo())
        mResult.value = String.format("%.2f", resultNum)
    }

    private fun getCalculationInfo(): CalculationHistoryDb.CalculationInfo {
        var percentNum: Double = Utils.parseStringToNumber(mPercent.value as String)
        var periodsNum: Double = Utils.parseStringToNumber(mPeriods.value as String)
        var initialNum: Double = Utils.parseStringToNumber(mStartSum.value as String)
        var incomeNum:Double = Utils.parseStringToNumber(mIncome.value as String)

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