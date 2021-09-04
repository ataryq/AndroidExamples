package com.example.melearning.fragments.calculation.history_adapter

import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.CalculationHistoryDb
import com.example.melearning.databinding.CalculatorHistoryItemBinding
import com.example.custom_ui.CustomEditTextWrapper

class HistoryAdapterItem(private var mBinding: CalculatorHistoryItemBinding,
                         private val mListener: HistoryItemListener)
    : RecyclerView.ViewHolder(mBinding.root) {

    private lateinit var mInfo: CalculationHistoryDb.CalculationInfo
    private val editTextFields = ArrayList<EditTextHistoryItemWrapper>()
    private val lifecycleOwner = mListener.getLifecycleOwner()

    init {
        mBinding.historyItemApplyButton.setOnClickListener {
            mListener.click(mInfo)
        }
        mBinding.historyItemDeleteButton.setOnClickListener{
            mListener.deleteHistoryItem(mInfo)
        }
        editTextFields.add(EditTextHistoryItemWrapper(mBinding.percentTV))
        editTextFields.add(EditTextHistoryItemWrapper(mBinding.incomeTV))
        editTextFields.add(EditTextHistoryItemWrapper(mBinding.startSumTV))
        editTextFields.add(EditTextHistoryItemWrapper(mBinding.periodsTV))
    }

    fun bind(info: CalculationHistoryDb.CalculationInfo) {
        mInfo = info
        mBinding.calculationInfo = info
    }

    private fun saveHistoryItem() {
        mInfo.percent = mBinding.percentTV.text.toString().toDouble()
        mInfo.income = mBinding.incomeTV.text.toString().toDouble()
        mInfo.initial = mBinding.startSumTV.text.toString().toDouble()
        mInfo.periods = mBinding.periodsTV.text.toString().toDouble()
        mListener.updateHistoryItem(mInfo)
    }

    fun onDetach() {
        for(item in editTextFields)
            item.clear()
    }

    inner class EditTextHistoryItemWrapper(val editText: EditText):
        CustomEditTextWrapper(editText, lifecycleOwner)
    {
        private var inFocus = false

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            super.onFocusChange(v, hasFocus)

            if(!hasFocus && inFocus) {
                saveHistoryItem()
            }

            inFocus = hasFocus
        }
    }
}