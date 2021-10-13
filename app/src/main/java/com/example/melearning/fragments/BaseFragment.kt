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

abstract class BaseFragment(private val enableDebugLog: Boolean = true): Fragment() {
    abstract fun layoutId(): Int
    abstract fun setBinding(binding: ViewDataBinding)
    protected val activityViewModel: MainActivityViewModel by activityViewModels()
    protected val disposable = CompositeDisposable()

    protected var onViewCreatedLambda: () -> Unit = {}

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

        onCreateViewEnd()
        onViewCreatedLambda()
        return binding.root
    }

    fun onViewCreatedEnd(initLambda: () -> Unit) {
        this.onViewCreatedLambda = initLambda
    }

    open fun onCreateViewEnd() {}

    fun debugPrintln(message: Any?) {
        if(enableDebugLog) {
            println(message)
        }
    }

    fun getTransition(transactionId: Int): Transition =
        TransitionInflater.from(requireContext()).inflateTransition(transactionId)

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}