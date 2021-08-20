package com.example.melearning.fragments

import android.os.Bundle
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import com.example.melearning.R
import com.example.melearning.databinding.LargeImageFragmentBinding


class LargeImageFragment: BaseFragment() {
    private lateinit var binding: LargeImageFragmentBinding

    override fun layoutId() = R.layout.large_image_fragment
    override fun setBinding(binding: ViewDataBinding) {
        this.binding = binding as LargeImageFragmentBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        sharedElementEnterTransition = getTransaction(R.transition.enter_shared_image)
        enterTransition = getTransaction(R.transition.exit_shared_image)
        (enterTransition as TransitionSet).excludeTarget(binding.largePhoto, true)

        arguments?.getInt("large_photo_id")?.let {
            binding.largePhoto.setImageResource(it)
            binding.largePhoto.transitionName = it.toString()
        }

        initSharedCallback()

        return root
    }

    private fun initSharedCallback() {
        setEnterSharedElementCallback(object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String>, sharedElements: MutableMap<String, View>)
                {
                    if(names.isEmpty()) return
                    sharedElements[names[0]] = binding.largePhoto
                }
            })
    }
}