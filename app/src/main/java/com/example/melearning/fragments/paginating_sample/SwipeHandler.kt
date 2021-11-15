package com.example.melearning.fragments.paginating_sample

import android.annotation.SuppressLint
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.melearning.fragments.paging.adapter.PagingAdapter
import com.example.melearning.fragments.paging.adapter.PagingAdapterViewHolder

@SuppressLint("NotifyDataSetChanged")
class SwipeHandler(adapter: PagingAdapter) : ItemTouchHelper.Callback() {
    private val adapter: PagingAdapter = adapter
    private var swipedViewHolder: PagingAdapterViewHolder? = null

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val myViewHolder: PagingAdapterViewHolder = viewHolder as PagingAdapterViewHolder
        return if (swipedViewHolder !== myViewHolder) {
            makeMovementFlags(0, ItemTouchHelper.LEFT)
        } else {
            0
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        undo()
        swipedViewHolder = viewHolder as PagingAdapterViewHolder?
        adapter.notifyDataSetChanged()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (dX < 0) {
            val myViewHolder: PagingAdapterViewHolder = viewHolder as PagingAdapterViewHolder
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                myViewHolder.binding.root,
                dX / 4,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (dX < 0) {
            val myViewHolder: PagingAdapterViewHolder = viewHolder as PagingAdapterViewHolder
            getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                myViewHolder.binding.root,
                dX / 4,
                dY,
                actionState,
                isCurrentlyActive
            )
        }


    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val myViewHolder: PagingAdapterViewHolder = viewHolder as PagingAdapterViewHolder
            getDefaultUIUtil().onSelected(myViewHolder.binding.root)
        }
    }

    fun undo() {
        if (swipedViewHolder != null) {
            getDefaultUIUtil().clearView(swipedViewHolder!!.binding.root)
            adapter.notifyDataSetChanged()
            swipedViewHolder = null
        }
    }

    fun remove() {
        if (swipedViewHolder != null) {
//            getDefaultUIUtil().clearView(swipedViewHolder!!.binding.root)
//            adapter.remove(swipedViewHolder!!.adapterPosition)
//            adapter.notifyDataSetChanged()
//            swipedViewHolder = null
        }
    }

}
