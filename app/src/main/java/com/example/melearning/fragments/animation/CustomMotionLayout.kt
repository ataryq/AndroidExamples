package com.example.melearning.fragments.animation

import com.example.melearning.FragmentManagerUtils
import com.example.melearning.databinding.MotionLayoutBinding
import com.example.melearning.databinding.PopupWindowMotionLayoutBinding
import com.example.melearning.fragments.BaseBindFragment2

class CustomMotionLayout: BaseBindFragment2<MotionLayoutBinding>(MotionLayoutBinding::inflate) {
    class MotionLayoutPopupWindow:
        BaseBindFragment2<PopupWindowMotionLayoutBinding>(PopupWindowMotionLayoutBinding::inflate) {
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