package com.example.melearning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.melearning.databinding.BottomNavigationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class BottomNavigationFragment(private val mEventHandler: CalculationInfoEvent):
    BottomSheetDialogFragment(){

    @Inject lateinit var mAdapter: RecycleViewAdapter
    lateinit var mBinding: BottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDaggerComponent.instance.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.bottom_navigation, container, false)
        mAdapter.setEventHandler(mEventHandler)
        mAdapter.setLifecycleOwner(viewLifecycleOwner)

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