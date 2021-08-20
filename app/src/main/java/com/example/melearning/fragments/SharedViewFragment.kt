package com.example.melearning.fragments

import android.os.Bundle
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.commit
import androidx.fragment.app.replace
//import androidx.transition.TransitionSet
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.FragmentManagerUtils.Companion.checkFragmentIsAlreadyAdded
import com.example.melearning.FragmentManagerUtils.Companion.fragmentTag
import com.example.melearning.R
import com.example.melearning.databinding.SharedViewFragmentBinding


class SharedViewFragment: BaseFragment() {
    private lateinit var binding: SharedViewFragmentBinding
    private var clickedImage: Int = -1

    override fun layoutId() = R.layout.shared_view_fragment

    override fun setBinding(binding: ViewDataBinding) {
        this.binding = binding as SharedViewFragmentBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        initPhoto(binding.photo1, R.drawable.img_1)
        initPhoto(binding.photo2, R.drawable.img_3)
        exitTransition = getTransaction(R.transition.exit_shared_image)
//        postponeEnterTransition()
        initSharedCallback()

        return root
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
        makeAbove(getImageViewByIndex(clickedImage))
    }

    private fun getImageViewByIndex(index: Int): ImageView {
        return if(index == R.drawable.img_1) binding.photo1
        else binding.photo2
    }

    private fun initSharedCallback() {
        setExitSharedElementCallback(object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>, sharedElements: MutableMap<String?, View?>)
                {
                    if(names.isEmpty()) return
                    val imageIndex = names[0]?.toInt() ?: return
                    getImageViewByIndex(imageIndex)
                    sharedElements[names[0]] = getImageViewByIndex(imageIndex)
                }
            })
    }

    private fun initPhoto(photoImageView: ImageView, resourceId: Int) {
        photoImageView.transitionName = resourceId.toString()

        photoImageView.setOnClickListener {
            clickedImage = resourceId
            makeAbove(getImageViewByIndex(clickedImage))
            showLargePhoto(it, resourceId)
        }
    }

    private fun makeAbove(v: View?) {
        v?.translationZ = 1f
        v?.invalidate()
    }

    private fun showLargePhoto(it: View, photoId: Int) {
        val fragmentManager = activity?.supportFragmentManager ?: return
        if(checkFragmentIsAlreadyAdded<LargeImageFragment>(fragmentManager)) return

        (exitTransition as TransitionSet?)?.excludeTarget(it, true)

        fragmentManager.commit(true) {
            setReorderingAllowed(true)
            //setCustomAnimations(R.anim.fade_out, R.anim.fade_out, R.anim.fade_out, R.anim.fade_out)
            addSharedElement(it, it.transitionName)
            val className = fragmentTag<LargeImageFragment>()
            val bundle = Bundle()
            bundle.putInt("large_photo_id", photoId)
            replace<LargeImageFragment>(R.id.fragment_container_view, className, bundle)
            addToBackStack(FragmentManagerUtils.backStackTag<LargeImageFragment>())
        }
    }
}