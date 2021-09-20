package com.example.melearning.fragments.paging.adapter

interface PagingPostsListener: ClickListener<PagingAdapterViewHolder> {
    fun onLoaded(item: PagingAdapterViewHolder)
}