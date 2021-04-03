package com.example.melearning

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.melearning.CalculationHistoryDb.CalculationInfo
import kotlinx.android.synthetic.main.clear_table_dialog.*
import kotlinx.android.synthetic.main.percent_calculation_fragment.*
import javax.inject.Inject

class CalculationFragment:
    Fragment(R.layout.percent_calculation_fragment),
    OnClickListener,
    Utils.Callback {

    @Inject lateinit var mDb: DataProvider
    @Inject lateinit var mActivity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            ActivityDaggerComponent.instance.inject(this)
        }
    }

    override fun onStart() {
        super.onStart()

        restoreLastResults()
        initTextEditListeners()
        calculate()
        initBottomActionBar()
    }

    private fun initBottomActionBar() {
        saveResultButton.setOnClickListener {
            saveCalculationToHistory()
        }

        bottom_app_bar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.app_bar_search -> {
                    var dialog = AlertDialog.Builder(requireContext())
                        .setView(R.layout.clear_table_dialog).show()

                    dialog.yes_btn.setOnClickListener{ mDb.clear(); dialog.dismiss() }
                    dialog.no_btn.setOnClickListener{ dialog.dismiss() }

                    true
                }
                else -> false
            }
        }

        bottom_app_bar.setNavigationOnClickListener {
            println("onOptionsItemSelected")
            val bottomNavDrawerFragment = BottomNavigationFragment(this)
            val fragManager = mActivity.supportFragmentManager
            if(fragManager != null)
                bottomNavDrawerFragment.show(fragManager, bottomNavDrawerFragment.tag)
        }
    }

    override fun click(info: CalculationInfo?) {
        if(info != null)
            setCalculationInfo(info)
    }

    override fun called() {
        calculate()
    }

    private fun restoreLastResults() {
        mDb.getAll(object : LoadDataListener {
            override fun getData(data: List<CalculationInfo>) {
                if(data.isNotEmpty())
                    setCalculationInfo(data.last())
            }
        })
    }

    private fun initTextEditListeners() {
        Utils.addEditTextChangedListener(percentET, this)
        Utils.addEditTextChangedListener(numPeriodsET, this)
        Utils.addEditTextChangedListener(startSumET, this)
        Utils.addEditTextChangedListener(incomeET, this)
    }

    private fun ifEmptySet0(text: EditText) {
        if(text.text.isEmpty())
            text.setText("0")
    }

    private fun calculate() {
        var result: Double = calculate(getCalculationInfo())
        resultTV.text = String.format("%.2f", result)
    }

    private fun saveCalculationToHistory() {
        mDb.insertAll(getCalculationInfo())
    }

    private fun setCalculationInfo(info: CalculationInfo) {
        percentET.setText(info.percent.toString())
        numPeriodsET.setText(info.periods.toString())
        startSumET.setText(info.initial.toString())
        incomeET.setText(info.income.toString())
    }

    private fun getCalculationInfo(): CalculationInfo {
        var percent: Double = Utils.parseStringToNumber(percentET)
        var periods: Double = Utils.parseStringToNumber(numPeriodsET)
        var initial: Double = Utils.parseStringToNumber(startSumET)
        var income:Double = Utils.parseStringToNumber(incomeET)

        ifEmptySet0(percentET)
        ifEmptySet0(numPeriodsET)
        ifEmptySet0(startSumET)
        ifEmptySet0(incomeET)

        println("percent: $percent, periods: $periods, startAmount: $initial, income: $income")
        return CalculationInfo(percent, periods, initial, income)
    }
}