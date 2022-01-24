package com.example.melearning.fragments.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.melearning.R
import com.example.melearning.fragments.paging.PostData

class PagingAdapter(private val listener: PagingPostsListener):
    PagingDataAdapter<PostData, PagingAdapterViewHolder>(PostComparator) {
    companion object {
        val PostComparator = object : DiffUtil.ItemCallback<PostData>() {
            override fun areContentsTheSame(oldItem: PostData, newItem: PostData): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: PostData, newItem: PostData): Boolean =
                oldItem.postInfo.id == newItem.postInfo.id
        }
    }

    override fun onBindViewHolder(holder: PagingAdapterViewHolder, position: Int) {
        getItem(position)?.let {
            it.order = position
            holder.bind(it, listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paging_item, parent, false)
        view.doOnPreDraw {
            println("PagingAdapter: onCreateViewHolder: doOnPreDraw")
        }
        return PagingAdapterViewHolder(view)
    }
}