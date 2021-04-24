package com.example.melearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.CalculationHistoryDb.CalculationInfo
import com.example.melearning.databinding.CalculatorHistoryItemViewBinding
import dagger.*
import javax.inject.Inject

interface OnClickListener {
    fun click(info: CalculationInfo?)
}

class RecycleItemViewHolder(private var mBinding: CalculatorHistoryItemViewBinding)
                                : RecyclerView.ViewHolder(mBinding.root) {

    private var mEventHandler: CalculationInfoEvent? = null
    private lateinit var mInfo: CalculationInfo

    init {
        mBinding.root.setOnClickListener{
            mEventHandler?.value = Event(mInfo)
        }
    }

    fun bind(info: CalculationInfo, eventHandler: CalculationInfoEvent?) {
        mInfo = info
        mEventHandler = eventHandler
        mBinding.calculationInfo = info
    }
}

class RecycleViewAdapter @Inject constructor(db: DataProvider):
    RecyclerView.Adapter<RecycleItemViewHolder>() {

    private var mDataProvider: DataProvider = db
    private var mEventHandler: CalculationInfoEvent? = null
    private lateinit var mLifecycleOwner: LifecycleOwner

    private var mData = ArrayList<CalculationInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setEventHandler(eventHandler: CalculationInfoEvent) {
        mEventHandler = eventHandler
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        mLifecycleOwner = lifecycleOwner
        observeData()
    }

    private fun observeData() {
        mDataProvider.getAll().observe(mLifecycleOwner, Observer {
            println("fillData Observer it: ${it.size}")
            mData = ArrayList(it)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var binding: CalculatorHistoryItemViewBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.calculator_history_item_view, parent, false)

        return RecycleItemViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: RecycleItemViewHolder, position: Int) {
        if(position >= mData.size)
            return
        holder.bind(mData[position], mEventHandler)
    }
}