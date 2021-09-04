package com.example.melearning.fragments

import android.os.Bundle
import android.view.View
import com.example.melearning.R
import com.example.melearning.databinding.ShimmeringExampleFragmentBinding

class ShimmeringExampleFragment: BaseBindFragment<ShimmeringExampleFragmentBinding>() {
    override fun layoutId() = R.layout.shimmering_example_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmerLayout.showShimmer(true)
        binding.shimmerLayout.startShimmer()
    }
}