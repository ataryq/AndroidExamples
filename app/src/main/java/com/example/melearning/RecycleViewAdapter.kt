package com.example.melearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.CalculationHistoryDb.CalculationInfo
import dagger.*

@Module
class RecycleViewAdapterModule {
    @ActivityScope
    @Provides
    fun provideRecycleViewAdapter(db: DataProvider) = RecycleViewAdapter(db)
}

interface OnClickListener {
    fun click(info: CalculationInfo?)
}

class RecycleItemViewHolder(private val view: View,
                            var mListener:OnClickListener? = null,
                            var mInfo: CalculationInfo? = null)
                                : RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener{
            mListener?.click(mInfo)
        }
    }
}

class RecycleViewAdapter(db: DataProvider):
    RecyclerView.Adapter<RecycleItemViewHolder>(),
    Utils.Callback {

    private var mDataProvider: DataProvider = db
    private var mClickListener: OnClickListener? = null

    private var mData = ArrayList<CalculationInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        println("set mDataProvider")
        mDataProvider.setDataChangedCallback(this)
        fillData()
    }

    fun setClickListener(clickListener: OnClickListener) {
        mClickListener = clickListener
    }

    override fun called() {
        fillData()
    }

    private fun fillData() {
        mDataProvider.getAll(object : LoadDataListener {
            override fun getData(data: List<CalculationInfo>) {
                if(data.isNotEmpty())
                    mData = ArrayList(data)
                println("got data")
                notifyDataSetChanged()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.calculator_history_item_view, parent, false)

        return RecycleItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        //println("size: ${mData.size}")
        return mData.size
    }

    override fun onBindViewHolder(holder: RecycleItemViewHolder, position: Int) {
        if(position >= mData.size)
            return

        var info = mData[position]
        holder.mInfo = info
        holder.mListener = mClickListener
        var recyclerView = holder.itemView.findViewById<LinearLayout>(R.id.container)

        recyclerView.findViewById<TextView>(R.id.percentTV).text = info.percent.toString()
        recyclerView.findViewById<TextView>(R.id.startSumTV).text = info.initial.toString()
        recyclerView.findViewById<TextView>(R.id.periodsTV).text = info.periods.toString()
        recyclerView.findViewById<TextView>(R.id.incomeTV).text = info.income.toString()
    }
}