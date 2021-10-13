package com.example.melearning.fragments.moxy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.melearning.R
import com.example.melearning.databinding.MoxyFragmentBinding
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class MoxyFragment: MvpAppCompatFragment(), MoxyView {
    private lateinit var binding: MoxyFragmentBinding

    @InjectPresenter
    lateinit var presenter: MoxyPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        val bindView: ViewDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.moxy_fragment, container, false)

        binding = bindView as MoxyFragmentBinding

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.view1.setOnClickListener { presenter.viewClicked() }
        binding.view2.setOnClickListener { presenter.viewClicked() }
        binding.editText.setText(presenter.editText)
        binding.editText.addTextChangedListener {
            presenter.editText = it.toString()
        }
    }

    override fun clickedView() {
        val back2 = binding.view2.background
        binding.view2.background = binding.view1.background
        binding.view1.background = back2
    }
}