package com.example.melearning.fragments.paging

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.melearning.R
import com.example.melearning.databinding.PagingItemDetailedFragmentBinding
import com.example.melearning.fragments.BaseBindFragment

class PagingDetailedItemFragment: BaseBindFragment<PagingItemDetailedFragmentBinding>() {
    private val viewModel: PagingFragmentViewModel by viewModels()

    override fun layoutId() = R.layout.paging_item_detailed_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chosenPost?.apply {
            binding.pagingDetItemTitle.text = title
            binding.pagingDetItemContent.text = body
        }
    }
}