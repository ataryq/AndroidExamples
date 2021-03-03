package com.example.melearning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_navigation.*

class BottomNavigationFragment(private val mClickListener: OnClickListener):
    BottomSheetDialogFragment(),
    OnClickListener {

    private val mAdapter = RecycleViewAdapter(this)

    override fun onStart() {
        super.onStart()

        historyCalculations.adapter = mAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            var db = MainActivity.getCachedDb(this.requireContext())
            mAdapter.setDataProvider(db)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_navigation, container, false)
    }

    override fun click(info: CalculationHistoryDb.CalculationInfo?) {
        mClickListener.click(info)
        dismiss()
    }
}