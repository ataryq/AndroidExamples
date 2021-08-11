package com.example.melearning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.melearning.databinding.BottomNavigationBinding
import com.example.melearning.history_adapter.HistoryAdapter
import com.example.melearning.history_adapter.HistoryItemListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationFragment(private val mListener: HistoryItemListener,
                               private val mDataProvider: DataProvider):
    BottomSheetDialogFragment(){

    private lateinit var mAdapter: HistoryAdapter
    private lateinit var mBinding: BottomNavigationBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.bottom_navigation, container, false)

        mAdapter = HistoryAdapter(mDataProvider, mListener)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mBinding.historyCalculations.adapter = mAdapter
    }

    override fun onPause() {
        dismiss()
        super.onPause()
    }

}