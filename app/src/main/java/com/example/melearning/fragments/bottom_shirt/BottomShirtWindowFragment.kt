package com.example.melearning.fragments.bottom_shirt

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.custom_ui.UiUtils
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.R
import com.example.melearning.databinding.PopupFragmentBinding
import com.example.melearning.databinding.PopupWindowBinding
import com.example.melearning.fragments.BaseBindFragment


class BottomShirtWindowFragment: BaseBindFragment<PopupFragmentBinding>() {
    override fun layoutId() = R.layout.popup_fragment

    @SuppressLint("InflateParams")
    override fun onCreateViewEnd() {
        binding.showPopupButton.setOnClickListener {
            val fragment = BottomShirtFragment<PopupWindowBinding>(R.layout.popup_window)
            fragment.onViewCreatedEnd {
                fragment.layoutBinding().dismissButton.setOnClickListener {
                    FragmentManagerUtils.removeFragment(parentFragmentManager, fragment)
                }
                fragment.setHeightsByPercent( listOf(0.0f, 0.5f, 1.0f) )
                fragment.setDefaultSegmentIndex(1)
                fragment.setInsideScrollView(fragment.layoutBinding().scrollView1)
            }

            FragmentManagerUtils.addFragment(parentFragmentManager, fragment, {
                setCustomAnimations(R.anim.move_in_from_bottom, R.anim.move_out_left,
                    R.anim.move_in_right, R.anim.move_out_to_bottom)
            })
        }

        binding.writeCommentButton.setOnClickListener {
            val inflater = LayoutInflater.from(activity)
            val rootView = inflater.inflate(R.layout.comment_fragment, null)
                ?: return@setOnClickListener
            val editText = rootView.findViewById<EditText>(R.id.commentEditText)

            val popupWindow = PopupWindow(
                rootView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )

            popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
            editText.requestFocus()
            UiUtils.imitateTouch(editText)
        }
    }
}