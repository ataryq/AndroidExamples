package com.example.melearning.fragments.cicerone

import android.os.Bundle
import android.view.View
import com.example.melearning.R
import com.example.melearning.databinding.CiceroneChildFragmentBinding
import com.example.melearning.fragments.BaseBindFragment

class CiceroneChildFragment(private val numChild: Int = 0):
    BaseBindFragment<CiceroneChildFragmentBinding>()
{
    override fun layoutId() = R.layout.cicerone_child_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.childTextVIew.text = resources.getString(R.string.cicerone_child, numChild.toString())
    }
}