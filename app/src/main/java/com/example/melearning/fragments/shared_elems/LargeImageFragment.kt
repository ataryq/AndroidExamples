package com.example.melearning.fragments.shared_elems

import com.example.melearning.R
import com.example.melearning.databinding.LargeImageFragmentBinding
import com.example.melearning.fragments.BaseSharedFragment

class LargeImageFragment: BaseSharedFragment<LargeImageFragmentBinding>() {
    override fun layoutId() = R.layout.large_image_fragment

    override fun onCreateViewEnd() {
        sharedElementEnterTransition = getTransition(R.transition.enter_shared_image)

        binding.largePhoto.setImageResource(R.drawable.img_1)
        binding.largePhoto.transitionName = "shared"

        val sharedMap = mapOf(binding.largePhoto.transitionName to binding.largePhoto)
        setEnterSharedElementCallback(createSharedCallback(sharedMap, "2"))
    }
}