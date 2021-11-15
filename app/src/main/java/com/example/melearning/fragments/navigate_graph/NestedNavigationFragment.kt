package com.example.melearning.fragments.navigate_graph

import androidx.navigation.fragment.findNavController
import com.example.melearning.R
import com.example.melearning.databinding.NestedNavigationFragmentBinding
import com.example.melearning.fragments.BaseBindFragment2

class NestedNavigationFragment:
    BaseBindFragment2<NestedNavigationFragmentBinding>(
        NestedNavigationFragmentBinding::inflate)
{
    override fun initViews() {
        binding.nestedGoRx.setOnClickListener{
            findNavController().navigate(R.id.action_nested_to_rx)
        }

        binding.nestedGoBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nestedGoUp.setOnClickListener {
            findNavController().navigateUp()
        }

    }
}