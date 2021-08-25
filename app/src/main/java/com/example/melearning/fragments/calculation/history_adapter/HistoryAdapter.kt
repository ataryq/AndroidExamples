package com.example.melearning.fragments.calculation.history_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.CalculationHistoryDb.CalculationInfo
import com.example.melearning.DataProvider
import com.example.melearning.R
import com.example.melearning.databinding.CalculatorHistoryItemBinding

class HistoryAdapter constructor(private var mDataProvider: DataProvider,
                                         private var mListener: HistoryItemListener):
    RecyclerView.Adapter<HistoryAdapterItem>() {

    private var mData = ArrayList<CalculationInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        observeData()
        setHasStableIds(true)
    }

    private fun observeData() {
        mDataProvider.getAll().observe(mListener.getLifecycleOwner(), {
            mData = ArrayList(it)
        })
    }

    override fun onViewDetachedFromWindow(holder: HistoryAdapterItem) {
        holder.onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapterItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CalculatorHistoryItemBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.calculator_history_item, parent, false)

        println("onCreateViewHolder")

        return HistoryAdapterItem(binding, mListener)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: HistoryAdapterItem, position: Int) {
        if(position >= mData.size)
            return
        println("onBindViewHolder position: $position")
        holder.bind(mData[position])
    }
}