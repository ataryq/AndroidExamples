package com.example.melearning.fragments

import android.transition.Transition
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.viewbinding.ViewBinding
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.fragments.paging.view.PagingDetailedItemFragment

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
abstract class BaseSharedFragment<T: ViewBinding>(
    val controlPostpone: Boolean = true
): BaseBindFragment<T>()
{
    private var sharedViews: Map<String, View> = mapOf()

    override fun onCreateViewEnd() {
        super.onCreateViewEnd()

        if(activityViewModel.mapSharedData.isEmpty()) {
            if(controlPostpone) {
                postponeEnterTransition()
            }
        }
    }

    open fun initSharedFragmentFrom(map: Map<String, View>,
                                    exitTransitionId: Int) {
        activityViewModel.mapSharedData = mapOf()

        exitTransition = getTransition(exitTransitionId)

        setSharedElements(map)
//        excludeViewsFromTranslation(exitTransition as Transition)

        setExitSharedElementCallback(createSharedCallback("from"))

        if(controlPostpone) {
            startPostponedEnterTransition()
        }
    }

    open fun initSharedFragmentTo(map: Map<String, View>, sharedTransitionId: Int) {
        sharedElementEnterTransition = getTransition(sharedTransitionId)

        setSharedElements(map)
        setEnterSharedElementCallback(createSharedCallback("to"))
        if(controlPostpone) {
            startPostponedEnterTransition()
        }
    }

    private fun excludeViewsFromTranslation(translation: Transition) {
        for(item in sharedViews.values) {
            translation.excludeTarget(item, true)
        }
    }

    fun showSharedFragment(mapSharedData: Map<String, Any>) {
        activityViewModel.mapSharedData = mapSharedData

        FragmentManagerUtils.showFragment<PagingDetailedItemFragment>(parentFragmentManager, {
            for(item in sharedViews) {
                addSharedElement(item.value, item.key)
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSharedData(paramName: String) = activityViewModel.mapSharedData[paramName] as T?

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
                println("[$tag] SharedElementCallback name: $name")
                if(_sharedViews.containsKey(name)) {
                    println("[$tag] elem found: ${_sharedViews[name]}")
                    sharedElements[name] = _sharedViews[name]
                }
            }
        }
    }
}