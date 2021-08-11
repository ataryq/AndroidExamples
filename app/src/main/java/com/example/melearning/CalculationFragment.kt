package com.example.melearning

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.melearning.CalculationHistoryDb.CalculationInfo
import com.example.melearning.databinding.PercentCalculationFragmentBinding
import com.example.melearning.history_adapter.HistoryItemListener
import com.example.melearning.ui_utils.CustomEditTextWrapper
import javax.inject.Inject

class CalculationFragment: Fragment(), HistoryItemListener {
    @Inject lateinit var mDb: DataProvider
    @Inject lateinit var mActivity: AppCompatActivity
    private lateinit var mViewModel: CalculationViewModel
    private lateinit var mBinding: PercentCalculationFragmentBinding
    private val mEditTextWrappers = ArrayList<CustomEditTextWrapper>()
    private var mBottomNavDrawerFragment: BottomNavigationFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDaggerComponent.instance.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater,
            R.layout.percent_calculation_fragment,
            container,
            false)
        mBinding.lifecycleOwner = this
        mViewModel = CalculationFragmentViewModelFactory.createModel(this, mDb)
        mBinding.viewModel = mViewModel

        editTextsSetup()

        return mBinding.root
    }

    private fun editTextsSetup() {
        mBinding.editTextContainer.setOnClickListener {
            println("click editTextContainer")
        }
        if(mEditTextWrappers.isNotEmpty()) {
            for(wrapper in mEditTextWrappers) {
                wrapper.clear()
            }
            mEditTextWrappers.clear()
        }

        mEditTextWrappers.add(CustomEditTextWrapper(mBinding.percentET, viewLifecycleOwner))
        mEditTextWrappers.add(CustomEditTextWrapper(mBinding.startSumET, viewLifecycleOwner))
        mEditTextWrappers.add(CustomEditTextWrapper(mBinding.numPeriodsET, viewLifecycleOwner))
        mEditTextWrappers.add(CustomEditTextWrapper(mBinding.incomeET, viewLifecycleOwner))
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
                R.id.app_bar_clear_table -> {
                    showClearAllDialog()
                    true
                }
                R.id.app_bar_settings -> {
                    println("app_bar_settings")
                    true
                }
                else -> false
            }
        }

        mBinding.bottomAppBar.setNavigationOnClickListener {
            println("onOptionsItemSelected")
            mBottomNavDrawerFragment = BottomNavigationFragment(this, mDb)
            val fragManager = mActivity.supportFragmentManager
            mBottomNavDrawerFragment!!.show(fragManager, BottomNavigationFragment::class.java.name)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {

        menu.setHeaderTitle("HeaderTitle")
        menu.add(0, 0, 0, "homescreen")
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun showClearAllDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.clear_table_dialog).show()

        dialog.findViewById<Button>(R.id.yes_btn)
            .setOnClickListener{
                mDb.clear()
                dialog.dismiss()
            }

        dialog.findViewById<Button>(R.id.no_btn)
            .setOnClickListener{ dialog.dismiss() }
    }

    override fun click(info: CalculationInfo?) {
        if(info != null) {
            mViewModel.setCalculationInfo(info)
        }
    }

    override fun getLifecycleOwner() = viewLifecycleOwner

    override fun updateHistoryItem(info: CalculationInfo) {
        mDb.update(info)
    }

    override fun deleteHistoryItem(info: CalculationInfo) {
        mDb.delete(info)
    }

    override fun updateDbInProgress() = mDb.updateDbInProgress()
}