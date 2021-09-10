package com.example.melearning.fragments.shared_elems

import com.example.melearning.FragmentManagerUtils
import com.example.melearning.R
import com.example.melearning.databinding.SharedViewFragmentBinding
import com.example.melearning.fragments.BaseSharedFragment

class SimpleSharedFragment: BaseSharedFragment<SharedViewFragmentBinding>() {
    override fun layoutId() = R.layout.shared_view_fragment

    override fun onCreateViewEnd() {
        binding.photo1.transitionName = "shared"

        binding.photo1.setOnClickListener {
            FragmentManagerUtils.showFragment<LargeImageFragment>(parentFragmentManager, {
                addSharedElement(it, it.transitionName)
            })
        }

        exitTransition = getTransition(R.transition.exit_shared_image)

        val sharedViews = mapOf("shared" to binding.photo1)
        setExitSharedElementCallback(createSharedCallback(sharedViews, "1"))
    }
}