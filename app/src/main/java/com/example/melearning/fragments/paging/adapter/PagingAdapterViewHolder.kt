package com.example.melearning.fragments.paging.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.databinding.PagingItemBinding
import com.example.melearning.fragments.paging.PostData
import com.squareup.picasso.Picasso
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.SwipeLayout.SwipeListener


class PagingAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {
    lateinit var postData: PostData
    val binding = PagingItemBinding.bind(view)

    fun bind(_postData: PostData, listener: PagingPostsListener) {
        this.postData = _postData
        val postInfo = postData.postInfo

        if(postInfo.title.length > 25)
            postInfo.title = postInfo.title.substring(0, 25)

        binding.pagingItemTitle.text = postInfo.title
        binding.pagingItemContent.text = postInfo.body
        binding.pagingCardHolder.setOnClickListener {
            listener.onClick(this)
        }

        initSwipeLayout()

        Picasso.get().load(postData.imageInfo.thumbnailUrl).into(binding.pagingItemImage)
        listener.onLoaded(this)
    }

    private fun initSwipeLayout() {
        val swipeLayout = binding.swipeLayout
        swipeLayout.isSwipeEnabled = true
        swipeLayout.showMode = SwipeLayout.ShowMode.LayDown
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, binding.bottomWrapper)

        swipeLayout.addSwipeListener(object : SwipeListener {
            override fun onClose(layout: SwipeLayout) {
                //when the SurfaceView totally cover the BottomView.
            }

            override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {
                //you are swiping.
            }

            override fun onStartOpen(layout: SwipeLayout) {}

            override fun onOpen(layout: SwipeLayout) {
                //when the BottomView totally show.
            }

            override fun onStartClose(layout: SwipeLayout) {}
            override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {
                //when user's hand released.
            }
        })
    }
}