package com.example.melearning.fragments.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.R
import com.example.melearning.databinding.HeaderListItemBinding

class HeaderListAdapter: LoadStateAdapter<HeaderListViewHolder>() {
    override fun onBindViewHolder(holder: HeaderListViewHolder, loadState: LoadState) {
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): HeaderListViewHolder {
        return HeaderListViewHolder(parent)
    }
}

class HeaderListViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).
        inflate(R.layout.header_list_item, parent, false)
)
{
    private val binding = HeaderListItemBinding.bind(itemView)

    fun bind() {

    }
}