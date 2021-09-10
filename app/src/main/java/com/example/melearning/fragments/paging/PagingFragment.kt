package com.example.melearning.fragments.paging

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.custom_ui.UiUtils
import com.example.melearning.R
import com.example.melearning.databinding.PagingFragmentBinding
import com.example.melearning.examples.PostInfo
import com.example.melearning.fragments.BaseSharedFragment
import kotlinx.coroutines.flow.collectLatest


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
            postponeEnterTransition()
        }
        dataLoaded.observe(viewLifecycleOwner) {
            println("dataLoaded: $it")
            if(it) {
//                startPostponedEnterTransition()
            }
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
        binding.list.adapter = adapter//.withLoadStateFooter()

        lifecycleScope.launchWhenCreated {
            viewModel.getPosts().collectLatest {
                adapter.submitData(it)
            }
        }

        scrollToPosition()
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

    private fun scrollToPosition() {
        binding.list.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.list.removeOnLayoutChangeListener(this)
                val layoutManager: RecyclerView.LayoutManager? = binding.list.layoutManager
                if(lastChosenItemOrder < 0) return
                val viewAtPosition = layoutManager?.findViewByPosition(lastChosenItemOrder)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null ||
                    layoutManager.isViewPartiallyVisible(
                        viewAtPosition, false, true))
                        {
                            binding.list.post {
                                println("scroll to position: $lastChosenItemOrder")
                                layoutManager?.scrollToPosition(lastChosenItemOrder)
                            }
                        }
            }
        })
    }

    override fun onLoaded(item: PagingAdapterViewHolder) {
        val sharedData = getSharedData<PostInfo>() ?: return

        if(item.postInfo.id == sharedData.id) {
            lastChosenItemOrder = sharedData.listOrder
            println("item found at position: $lastChosenItemOrder")
            initShared(item)
            startPostponedEnterTransition()
        }
    }

    override fun onClick(chosenItem: PagingAdapterViewHolder) {
        println("from: onClick")

        if(!(dataLoaded.value as Boolean)) return

        initShared(chosenItem)
        showSharedFragment(chosenItem.postInfo)
    }

    private fun initShared(chosenItem: PagingAdapterViewHolder) {
        initSharedFragmentFrom(
            mapOf(
                ImageTransitionName to chosenItem.imageView(),
                TitleTransitionName to chosenItem.titleView(),
                ContentTransitionName to chosenItem.contextView(),
                CardTransitionName to chosenItem.cardView(),
                DividerTransitionName to chosenItem.titleDividerView()
            ),
            R.transition.grid_exit_transition)
    }
}