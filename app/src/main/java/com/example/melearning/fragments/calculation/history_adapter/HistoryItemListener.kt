package com.example.melearning.fragments.calculation.history_adapter

import androidx.lifecycle.LifecycleOwner
import com.example.melearning.CalculationHistoryDb

interface HistoryItemListener {
    fun click(info: CalculationHistoryDb.CalculationInfo?)
    fun getLifecycleOwner(): LifecycleOwner
    fun updateHistoryItem(info: CalculationHistoryDb.CalculationInfo)
    fun deleteHistoryItem(info: CalculationHistoryDb.CalculationInfo)
    fun updateDbInProgress(): Boolean
}