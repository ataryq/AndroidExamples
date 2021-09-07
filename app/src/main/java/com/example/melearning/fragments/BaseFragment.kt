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
import com.example.melearning.fragments.main_activity.MainActivityViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment: Fragment() {
    abstract fun layoutId(): Int
    abstract fun setBinding(binding: ViewDataBinding)
    protected val mActivityViewModel: MainActivityViewModel by activityViewModels()
    protected val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater,
            layoutId(),
            container,
            false)

        binding.lifecycleOwner = this
        setBinding(binding)
        return binding.root
    }

    fun getTransaction(transactionId: Int): Transition =
        TransitionInflater.from(requireContext()).inflateTransition(transactionId)

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}