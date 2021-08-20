package com.example.melearning.fragments

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.melearning.fragments.main_activity.MainActivityViewModel

abstract class BaseBindFragment<T: ViewBinding>: Fragment() {
    abstract fun layoutId(): Int
    protected val mActivityViewModel: MainActivityViewModel by activityViewModels()

    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater,
            layoutId(),
            container,
            false)

        (binding as ViewDataBinding?)?.lifecycleOwner = this
        return binding.root
    }

    fun getTransaction(transactionId: Int): Transition =
        TransitionInflater.from(requireContext()).inflateTransition(transactionId)
}