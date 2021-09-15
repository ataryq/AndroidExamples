package com.example.melearning.fragments.paging

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.R
import com.example.melearning.databinding.PagingItemBinding
import com.example.melearning.examples.PostInfo

class PagingAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {
    lateinit var postInfo: PostInfo
    val binding = PagingItemBinding.bind(view)

    fun bind(postInfo: PostInfo, listener: PagingPostsListener) {
        if(postInfo.title.length > 25)
            postInfo.title = postInfo.title.substring(0, 25)

        this.postInfo = postInfo

        binding.pagingItemTitle.text = postInfo.title
        binding.pagingItemContent.text = postInfo.body
        binding.pagingCardHolder.setOnClickListener {
            listener.onClick(this)
        }
        listener.onLoaded(this)
    }

    fun imageView(): ImageView = itemView.findViewById<ImageView>(R.id.paging_item_image)
    fun titleView(): TextView = itemView.findViewById<TextView>(R.id.paging_item_title)
    fun contextView(): TextView = itemView.findViewById<TextView>(R.id.paging_item_content)
    fun cardView(): CardView = itemView.findViewById<CardView>(R.id.paging_card_holder)
    fun titleDividerView(): View = itemView.findViewById<View>(R.id.paging_title_divider)
}