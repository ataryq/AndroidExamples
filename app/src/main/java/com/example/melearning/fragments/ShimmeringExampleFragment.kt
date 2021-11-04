package com.example.melearning.fragments

import com.example.melearning.databinding.ShimmeringExampleFragmentBinding

class ShimmeringExampleFragment:
    BaseBindFragment2<ShimmeringExampleFragmentBinding>
        (ShimmeringExampleFragmentBinding::inflate)
{
    override fun initViews() {
        binding.shimmerLayout.showShimmer(true)
        binding.shimmerLayout.startShimmer()
    }
}