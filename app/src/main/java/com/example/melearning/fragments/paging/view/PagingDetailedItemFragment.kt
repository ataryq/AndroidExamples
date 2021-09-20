package com.example.melearning.fragments.paging.view

import android.graphics.drawable.Drawable
import com.example.melearning.R
import com.example.melearning.databinding.PagingItemDetailedFragmentBinding
import com.example.melearning.fragments.BaseSharedFragment
import com.example.melearning.fragments.paging.PostData
import com.example.melearning.fragments.paging.view.PagingFragment.Companion.DataSharedParamName
import com.example.melearning.fragments.paging.view.PagingFragment.Companion.DrawableSharedParamName
import com.squareup.picasso.Picasso

class PagingDetailedItemFragment: BaseSharedFragment<PagingItemDetailedFragmentBinding>() {
    override fun layoutId() = R.layout.paging_item_detailed_fragment

    override fun onCreateViewEnd() {
        super.onCreateViewEnd()

        getSharedData<PostData>(DataSharedParamName)?.apply {
            binding.pagingDetItemTitle.text = postInfo.title
            binding.pagingDetItemContent.text = postInfo.body

            setImage(this)
        }

        initSharedFragment()
    }

    private fun setImage(postData: PostData) {
        var requestCreator = Picasso.get().load(postData.imageInfo.url)

        getSharedData<Drawable>(DrawableSharedParamName)?.apply {
            binding.pagingDetItemImage.setImageDrawable(this)
            requestCreator = requestCreator.placeholder(this)
        }

        requestCreator.into(binding.pagingDetItemImage)
    }

    private fun initSharedFragment() {
        val postfix = getSharedData<PostData>(DataSharedParamName)?.postInfo?.id ?: ""

        println("[to] init shared")
        initSharedFragmentTo(
            mapOf(
                PagingFragment.ImageTransitionName + postfix to binding.pagingDetItemImage,
                PagingFragment.TitleTransitionName + postfix to binding.pagingDetItemTitle,
                PagingFragment.CardTransitionName + postfix to binding.detCardHolder,
                PagingFragment.DividerTransitionName + postfix to binding.pagingDetTitleDivider,
                PagingFragment.ContentTransitionName + postfix to binding.pagingDetItemContent
            ),
            R.transition.enter_shared_image)
    }
}