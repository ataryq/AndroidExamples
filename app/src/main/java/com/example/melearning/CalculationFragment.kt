package com.example.melearning

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.melearning.CalculationHistoryDb.CalculationInfo
import com.example.melearning.databinding.PercentCalculationFragmentBinding
import javax.inject.Inject

class CalculationFragment:
    Fragment(),
    OnClickListener{

    @Inject lateinit var mDb: DataProvider
    @Inject lateinit var mActivity: AppCompatActivity
    @Inject lateinit var mViewModel: CalculationFragmentViewModel
    lateinit var mBinding: PercentCalculationFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDaggerComponent.instance.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.percent_calculation_fragment,
            container, false)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initBottomActionBar()
    }

    private fun initBottomActionBar() {
        mBinding.saveResultButton.setOnClickListener {
            mViewModel.saveCalculationToHistory()
        }

        mBinding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.app_bar_search -> {
                    showClearAllDialog()
                    true
                }
                else -> false
            }
        }

        mBinding.bottomAppBar.setNavigationOnClickListener {
            println("onOptionsItemSelected")
            val bottomNavDrawerFragment = BottomNavigationFragment(mViewModel.mUpdateCalcInfoHandler)
            val fragManager = mActivity.supportFragmentManager
            if(fragManager != null)
                bottomNavDrawerFragment.show(fragManager, bottomNavDrawerFragment.tag)
        }
    }

    private fun showClearAllDialog() {
        var dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.clear_table_dialog).show()

        dialog.findViewById<Button>(R.id.yes_btn)
            .setOnClickListener{
                mDb.clear();
                dialog.dismiss()
            }

        dialog.findViewById<Button>(R.id.no_btn)
            .setOnClickListener{ dialog.dismiss() }
    }

    override fun click(info: CalculationInfo?) {
        if(info != null)
            mViewModel.setCalculationInfo(info)
    }
}