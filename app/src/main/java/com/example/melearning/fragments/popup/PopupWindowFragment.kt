package com.example.melearning.fragments.popup

import com.example.melearning.FragmentManagerUtils
import com.example.melearning.R
import com.example.melearning.databinding.PopupFragmentBinding
import com.example.melearning.databinding.PopupWindowBinding
import com.example.melearning.fragments.BaseBindFragment

class PopupWindowFragment: BaseBindFragment<PopupFragmentBinding>() {
    override fun layoutId() = R.layout.popup_fragment

    override fun onCreateViewEnd() {
        binding.showPopupButton.setOnClickListener {
            val fragment = PopupFragment<PopupWindowBinding>(R.layout.popup_window)
            fragment.onViewCreatedEnd {
                fragment.layoutBinding().dismissButton.setOnClickListener {
                    fragment.dismiss()
                }
                fragment.setHeightsByPercent( listOf(0.0f, 0.5f, 1.0f) )
                fragment.setDefaultSegmentIndex(1)
            }

            FragmentManagerUtils.addFragment(parentFragmentManager, fragment, {
                setCustomAnimations(R.anim.move_in_from_bottom, R.anim.move_out_left,
                    R.anim.move_in_right, R.anim.move_out_to_bottom)
            })
        }
    }
}