package com.example.melearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnClickListener {
    fun click(info:CalculationInfo?)
}

class RecycleItemViewHolder(private val view: View,
                            var listener:OnClickListener? = null,
                            var mInfo:CalculationInfo? = null): RecyclerView.ViewHolder(view) {
    var mListener = listener

    init {
        view.setOnClickListener{
            mListener?.click(mInfo)
        }
    }
}

class RecycleViewAdapter(listener:OnClickListener?): RecyclerView.Adapter<RecycleItemViewHolder>() {
    var mClickListener: OnClickListener? = listener

    var data = ArrayList<CalculationInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycle_item_view, parent, false)

        return RecycleItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        println("size: ${data.size}")
        return data.size
    }

    override fun onBindViewHolder(holder: RecycleItemViewHolder, position: Int) {
        if(position >= data.size)
            return

        var info = data[position]
        holder.mInfo = info
        holder.mListener = mClickListener
        var recyclerView = holder.itemView.findViewById<LinearLayout>(R.id.container)

        recyclerView.findViewById<TextView>(R.id.percentTV).text = info.mPercent.toString()
        recyclerView.findViewById<TextView>(R.id.startSumTV).text = info.mInitial.toString()
        recyclerView.findViewById<TextView>(R.id.periodsTV).text = info.mPeriods.toString()
        recyclerView.findViewById<TextView>(R.id.incomeTV).text = info.mIncome.toString()
    }
}