package com.example.melearning.fragments.paging.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.databinding.PagingItemBinding
import com.example.melearning.fragments.paging.PostData
import com.squareup.picasso.Picasso

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

        Picasso.get().load(postData.imageInfo.thumbnailUrl).into(binding.pagingItemImage)

        listener.onLoaded(this)
    }
}