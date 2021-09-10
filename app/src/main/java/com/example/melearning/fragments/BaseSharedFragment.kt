package com.example.melearning.fragments

import android.transition.Transition
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.viewbinding.ViewBinding
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.R
import com.example.melearning.fragments.paging.PagingDetailedItemFragment

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
abstract class BaseSharedFragment<T: ViewBinding>(
    val controlPostpone: Boolean = true
): BaseBindFragment<T>()
{
    private var sharedViews: Map<String, View> = mapOf()

    override fun onCreateViewEnd() {
        super.onCreateViewEnd()

        if(activityViewModel.sharedAnyData != null) {
            if(controlPostpone) {
                postponeEnterTransition()
            }
        }
    }

    open fun initSharedFragmentFrom(map: Map<String, View>, exitTransitionId: Int) {
        activityViewModel.sharedAnyData = null

        exitTransition = getTransition(exitTransitionId)
        excludeViewsFromTranslation(exitTransition as Transition)

        sharedElementEnterTransition = getTransition(R.transition.enter_shared_image)
//        excludeViewsFromTranslation(sharedElementEnterTransition as Transition)

        setSharedElements(map)
        setExitSharedElementCallback(createSharedCallback("from"))
        setEnterSharedElementCallback(createSharedCallback("from"))

        if(controlPostpone) {
            startPostponedEnterTransition()
        }
    }

    open fun initSharedFragmentTo(map: Map<String, View>, sharedTransitionId: Int) {
        sharedElementEnterTransition = getTransition(sharedTransitionId)
        excludeViewsFromTranslation(sharedElementEnterTransition as Transition)
        setSharedElements(map)
        setEnterSharedElementCallback(createSharedCallback("to"))
        setExitSharedElementCallback(createSharedCallback("to"))
        if(controlPostpone) {
            startPostponedEnterTransition()
        }
    }

    private fun excludeViewsFromTranslation(translation: Transition) {
        for(item in sharedViews.values) {
            translation.excludeTarget(item, true)
        }
    }

    fun showSharedFragment(data: Any?) {
        activityViewModel.sharedAnyData = data

        FragmentManagerUtils.showFragment<PagingDetailedItemFragment>(parentFragmentManager, {
            for(item in sharedViews) {
                addSharedElement(item.value, item.key)
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSharedData() = activityViewModel.sharedAnyData as T?

    private fun setSharedElements(map: Map<String, View>) {
        sharedViews = map
        setTransactionNames()
    }

    private fun setTransactionNames() {
        for(item in sharedViews) {
            item.value.transitionName = item.key
        }
    }

    private fun createSharedCallback(tag: String = "") = createSharedCallback(sharedViews, tag)

    fun createSharedCallback(_sharedViews: Map<String, View>, tag: String = "")
            = object : SharedElementCallback() {
        override fun onMapSharedElements(
            names: List<String?>,
            sharedElements: MutableMap<String?, View?>)
        {
            for(name in names) {
//                println("[$tag] SharedElementCallback name: $name")
                if(_sharedViews.containsKey(name)) {
//                    println("[$tag] elem found: ${_sharedViews[name]?.transitionName}")
                    sharedElements[name] = _sharedViews[name]
                }
            }
        }
    }
}