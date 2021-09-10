package com.example.melearning.fragments.paging

import com.example.melearning.R
import com.example.melearning.Utils
import com.example.melearning.databinding.PagingItemDetailedFragmentBinding
import com.example.melearning.examples.PostInfo
import com.example.melearning.fragments.BaseSharedFragment

class PagingDetailedItemFragment: BaseSharedFragment<PagingItemDetailedFragmentBinding>() {
    override fun layoutId() = R.layout.paging_item_detailed_fragment

    override fun onCreateViewEnd() {
        super.onCreateViewEnd()

        getSharedData<PostInfo>()?.apply {
            binding.pagingDetItemTitle.text = title
            binding.pagingDetItemContent.text = body
        }

        println("glide start load")

        Utils.glideLoadImage(R.drawable.img_1, binding.pagingDetItemImage) {
            initSharedFragment()
        }
    }

    private fun initSharedFragment() {
        println("[to] init shared")
        initSharedFragmentTo(
            mapOf(
                PagingFragment.ImageTransitionName to binding.pagingDetItemImage,
                PagingFragment.TitleTransitionName to binding.pagingDetItemTitle,
                PagingFragment.ContentTransitionName to binding.pagingDetItemContent,
                PagingFragment.CardTransitionName to binding.detCardHolder,
                PagingFragment.DividerTransitionName to binding.pagingDetTitleDivider
            ),
            R.transition.enter_shared_image)
    }
}