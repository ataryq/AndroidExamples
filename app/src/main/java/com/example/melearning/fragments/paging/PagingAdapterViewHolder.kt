package com.example.melearning.fragments.paging

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.R
import com.example.melearning.examples.PostInfo

class PagingAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {
    lateinit var postInfo: PostInfo

    fun bind(postInfo: PostInfo, listener: PagingPostsListener) {
        if(postInfo.title.length > 25)
            postInfo.title = postInfo.title.substring(0, 25)
        this.postInfo = postInfo
        titleView().text = postInfo.title
        contextView().text = postInfo.body
        cardView().setOnClickListener {
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