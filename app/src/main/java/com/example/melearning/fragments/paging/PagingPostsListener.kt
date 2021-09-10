package com.example.melearning.fragments.paging

interface PagingPostsListener: ClickListener<PagingAdapterViewHolder> {
    fun onLoaded(item: PagingAdapterViewHolder)
}