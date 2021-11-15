package com.example.melearning.fragments.paginating_sample

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.R
import com.example.melearning.databinding.PagingFragmentBinding
import com.example.melearning.fragments.BaseBindFragment
import com.example.melearning.fragments.paging.PagingFragmentViewModel
import com.example.melearning.fragments.paging.adapter.PagingAdapter
import com.example.melearning.fragments.paging.adapter.PagingAdapterViewHolder
import com.example.melearning.fragments.paging.adapter.PagingPostsListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LightPaginating:
    BaseBindFragment<PagingFragmentBinding>(),
    PagingPostsListener {

    companion object {
        const val SharedDataTransitionName = "shared_data"
    }

    private var adapter = PagingAdapter(this)

    private val viewModel: PagingFragmentViewModel by viewModels()

    override fun layoutId() = R.layout.paging_fragment

    override fun onCreateViewEnd() {
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPosts().collectLatest {
                println("adapter submit data")
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        setOffShimmering()
    }

    private fun setOffShimmering() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.list.visibility = View.VISIBLE
    }

    override fun onLoaded(item: PagingAdapterViewHolder) {
    }

    override fun onClick(chosenItem: PagingAdapterViewHolder) {
        activityViewModel.mapSharedData =
            mapOf(SharedDataTransitionName to chosenItem.postData.postInfo)

        FragmentManagerUtils.showFragment<LightDetailedPaginating>(parentFragmentManager)
    }

}