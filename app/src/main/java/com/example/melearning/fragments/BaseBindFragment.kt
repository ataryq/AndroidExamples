package com.example.melearning.fragments

import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

abstract class BaseBindFragment<T: ViewBinding>: BaseFragment() {
    protected lateinit var binding: T

    @Suppress("UNCHECKED_CAST")
    override fun setBinding(binding: ViewDataBinding) {
        this.binding = binding as T
    }
}