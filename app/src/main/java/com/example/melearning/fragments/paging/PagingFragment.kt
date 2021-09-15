package com.example.melearning.fragments.paging

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.custom_ui.UiUtils
import com.example.melearning.R
import com.example.melearning.databinding.PagingFragmentBinding
import com.example.melearning.examples.PostInfo
import com.example.melearning.fragments.BaseSharedFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingFragment:
    BaseSharedFragment<PagingFragmentBinding>(false),
    PagingPostsListener
{
    companion object {
        const val ImageTransitionName = "shared_image"
        const val TitleTransitionName = "shared_title"
        const val ContentTransitionName = "shared_content"
        const val CardTransitionName = "shared_card"
        const val DividerTransitionName = "shared_divider"
    }

    private val adapter = PagingAdapter(this)
    private val viewModel: PagingFragmentViewModel by viewModels()
    private val dataLoaded = MutableLiveData(false)
    private var lastChosenItemOrder = -1

    override fun layoutId() = R.layout.paging_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(getSharedData<PostInfo>() != null) {
            println("postponeEnterTransition")
            postponeEnterTransition()
        }

        initSwipeRefresh()
        initList()
        initShimmeringEffect()
    }

    private fun initShimmeringEffect() {
        binding.shimmerLayout.startShimmer()
        dataLoaded.observe(viewLifecycleOwner, {
            if(it) {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.list.visibility = View.VISIBLE
            }
        })
    }

    private fun initList() {
        binding.list.adapter = adapter.withLoadStateHeader(HeaderListAdapter())

        binding.list.scrollBy(0, 1)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPosts().collectLatest {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
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
            if(isDataLoaded(loadStates)) {
                dataLoaded.postValue(true)
            }

            binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
        }
    }

    override fun onLoaded(item: PagingAdapterViewHolder) {
//        println("onLoaded sharedData is null: ${getSharedData<PostInfo>() == null}")

        val sharedData = getSharedData<PostInfo>() ?: return

//        println("sharedData id = ${sharedData.id} item id = ${item.postInfo.id}")

        if(item.postInfo.id == sharedData.id) {
            lastChosenItemOrder = sharedData.listOrder
            println("item with id = ${sharedData.id} found at position: $lastChosenItemOrder")

            initShared(item)
            startPostponedEnterTransition()
        }
    }

    override fun onClick(chosenItem: PagingAdapterViewHolder) {
        println("from: onClick")

        if(!(dataLoaded.value as Boolean)) return

        binding.list.scrollBy(0, 1)
        initShared(chosenItem)
        showSharedFragment(chosenItem.postInfo)
    }

    private fun initShared(chosenItem: PagingAdapterViewHolder) {
        val postfix = chosenItem.postInfo.id

        initSharedFragmentFrom(
            mapOf(
                ImageTransitionName + postfix to chosenItem.binding.pagingItemImage,
                TitleTransitionName + postfix to chosenItem.binding.pagingItemTitle,
                CardTransitionName + postfix to chosenItem.binding.pagingCardHolder,
                DividerTransitionName + postfix to chosenItem.binding.pagingTitleDivider
            ),
            R.transition.grid_exit_transition)
    }
}