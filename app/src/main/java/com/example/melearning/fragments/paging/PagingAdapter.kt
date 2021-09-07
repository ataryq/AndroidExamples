package com.example.melearning.fragments.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.R
import com.example.melearning.examples.PostInfo

class PagingAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bind(postInfo: PostInfo, listener: ClickListener<PostInfo>) {
        itemView.findViewById<TextView>(R.id.paging_item_title).text = postInfo.title
        itemView.findViewById<TextView>(R.id.paging_item_content).text = postInfo.body
        itemView.findViewById<CardView>(R.id.paging_card_holder).setOnClickListener {
            listener.onClick(postInfo)
        }
    }
}

class PagingAdapter(private val listener: ClickListener<PostInfo>):
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
        getItem(position)?.let { holder.bind(it, listener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paging_item, parent, false)
        return PagingAdapterViewHolder(view)
    }
}