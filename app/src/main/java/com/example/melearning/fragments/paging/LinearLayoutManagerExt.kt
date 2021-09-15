package com.example.melearning.fragments.paging

import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class LinearLayoutManagerExt @JvmOverloads constructor (
    val name : String,
    val fragment: Fragment,
    val adapter : RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    attrs : AttributeSet? = null,
    defStyleAttr : Int = 0,
    defStyleRes : Int = 0
) : LinearLayoutManager(fragment.requireContext(),attrs,defStyleAttr,defStyleRes){

    protected var savedState : SavedState? = fragment.arguments?.getParcelable(name)

    private val lifecycleObserver = object  : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if(event == Lifecycle.Event.ON_PAUSE) {
                val parc = onSaveInstanceState()
                if (fragment.arguments == null) {
                    fragment.arguments = Bundle()
                }
                fragment.arguments?.putParcelable(name, parc)
                fragment.lifecycle.removeObserver(this)
            }
        }
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if(savedState != null && itemCount > 0){
                println("LinearLayoutManagerExt: restore offset position")
                onRestoreInstanceState(savedState)
                savedState = null
            }
            if(itemCount > 0){
                adapter.unregisterAdapterDataObserver(this)
            }
        }
    }

    init {
        fragment.lifecycle.addObserver(lifecycleObserver)
        adapter.registerAdapterDataObserver(adapterDataObserver )
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        try{
            adapter.unregisterAdapterDataObserver(adapterDataObserver )
        } catch (_ : Exception){}
    }

}
