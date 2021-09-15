package com.example.melearning.fragments.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.melearning.R
import com.example.melearning.examples.PostInfo

class PagingAdapter(private val listener: PagingPostsListener):
    PagingDataAdapter<PostInfo, PagingAdapterViewHolder>(PostComparator) {
    companion object {
        val PostComparator = object : DiffUtil.ItemCallback<PostInfo>() {
            override fun areContentsTheSame(oldItem: PostInfo, newItem: PostInfo): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: PostInfo, newItem: PostInfo): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: PagingAdapterViewHolder, position: Int) {
        getItem(position)?.let {
            it.listOrder = position
            holder.bind(it, listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paging_item, parent, false)
        return PagingAdapterViewHolder(view)
    }
}