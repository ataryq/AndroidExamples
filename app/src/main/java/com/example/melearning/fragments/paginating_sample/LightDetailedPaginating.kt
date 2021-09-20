package com.example.melearning.fragments.paginating_sample

import com.example.melearning.R
import com.example.melearning.databinding.PagingItemDetailedFragmentBinding
import com.example.melearning.examples.PostInfo
import com.example.melearning.fragments.BaseBindFragment
import com.example.melearning.fragments.paginating_sample.LightPaginating.Companion.SharedDataTransitionName

class LightDetailedPaginating: BaseBindFragment<PagingItemDetailedFragmentBinding>() {
    override fun layoutId() = R.layout.paging_item_detailed_fragment

    override fun onCreateViewEnd() {
        (activityViewModel.mapSharedData[SharedDataTransitionName] as PostInfo?)?.apply {
            binding.pagingDetItemTitle.text = title
            binding.pagingDetItemContent.text = body
            binding.pagingDetItemImage.setImageResource(R.drawable.img_1)
        }
    }
}