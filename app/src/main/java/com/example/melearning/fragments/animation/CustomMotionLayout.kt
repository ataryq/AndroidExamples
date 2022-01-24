package com.example.melearning.fragments.animation

import com.example.melearning.FragmentManagerUtils
import com.example.melearning.databinding.MotionLayoutBinding
import com.example.melearning.databinding.PopupWindowMotionLayout2Binding
import com.example.melearning.databinding.PopupWindowMotionLayoutBinding
import com.example.melearning.fragments.BaseBindFragment2

class CustomMotionLayout: BaseBindFragment2<MotionLayoutBinding>(MotionLayoutBinding::inflate) {
    class MotionLayoutPopupWindow:
        BaseBindFragment2<PopupWindowMotionLayout2Binding>(PopupWindowMotionLayout2Binding::inflate) {
        override fun initViews() {

        }
    }

    override fun initViews() = with(binding) {
        motionLayoutHideButton.setOnClickListener {
            motionLayout.transitionToStart()
        }
        motionLayoutHideButton.background
        motionLayoutShowPopupButton.setOnClickListener {
            val fragment = MotionLayoutPopupWindow()
            FragmentManagerUtils.addFragment(parentFragmentManager, fragment)
        }
    }
}