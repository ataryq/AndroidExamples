package com.example.melearning.fragments.paging

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.custom_ui.UiUtils
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.R
import com.example.melearning.databinding.PagingFragmentBinding
import com.example.melearning.examples.PostInfo
import com.example.melearning.fragments.BaseBindFragment
import kotlinx.coroutines.flow.collectLatest

class PagingFragment: BaseBindFragment<PagingFragmentBinding>(), ClickListener<PostInfo> {
    companion object {
        const val PostponeUntilDataLoaded = false
    }

    private val adapter = PagingAdapter(this)
    private val viewModel: PagingFragmentViewModel by viewModels()
    private val dataLoaded = MutableLiveData(false)

    override fun layoutId() = R.layout.paging_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(PostponeUntilDataLoaded) {
            postponeEnterTransition()
            dataLoaded.observe(viewLifecycleOwner, {
                dataLoaded.removeObservers(viewLifecycleOwner)
                startPostponedEnterTransition()
            })
        }

        initSwipeRefresh()
        initList()
        initShimmeringEffect()
    }

    private fun initShimmeringEffect() {
        binding.shimmerLayout.startShimmer()
        dataLoaded.observe(viewLifecycleOwner, {
            if(it) {
                println("stop shimmering")
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.list.visibility = View.VISIBLE
            }
        })
    }

    private fun initList() {
        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.getPosts().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun isDataLoaded(loadStates: CombinedLoadStates): Boolean {
        val isLoading = loadStates.refresh is LoadState.Loading
        return binding.swipeRefresh.isRefreshing && !isLoading
    }

    private fun initSwipeRefresh() {
        val uiUtils = UiUtils(requireContext())
        binding.swipeRefresh.setColorSchemeColors(uiUtils.getAccentColor())
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(uiUtils.getPrimaryColor())

        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        adapter.addLoadStateListener { loadStates ->
            if(isDataLoaded(loadStates))
                dataLoaded.postValue(true)

            binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
        }
    }

    override fun onClick(chosenItem: PostInfo) {
        viewModel.chosenPost = chosenItem
        FragmentManagerUtils.showFragmentWithDefaultAnim<PagingDetailedItemFragment>(parentFragmentManager)
    }
}