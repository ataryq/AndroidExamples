package com.example.melearning.fragments

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.custom_ui.UiUtils
import com.example.melearning.R
import com.example.melearning.databinding.ScrollableFragmentBinding


class ScrollableFragment: BaseBindFragment<ScrollableFragmentBinding>() {
    override fun layoutId() = R.layout.scrollable_fragment

    override fun onCreateViewEnd() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val collToolbar = binding.collapsingToolbarLayout
        collToolbar.title = "Test Title"
        val color = UiUtils.getAttributeColor(R.attr.colorPrimaryDark, requireContext())
        collToolbar.setContentScrimColor(color)
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}