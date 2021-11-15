package com.example.melearning.fragments.navigate_graph

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.melearning.R
import com.example.melearning.databinding.HomeNavigateFragmentBinding
import com.example.melearning.fragments.BaseBindFragment2
import com.example.melearning.fragments.main_activity.ActivityProvider

class HomeNavigateFragment:
    BaseBindFragment2<HomeNavigateFragmentBinding>(HomeNavigateFragmentBinding::inflate)
{
    override fun initViews() {
        val currentDeepLevel = (arguments?.getInt("deepLevel") ?: 0) + 1

        binding.textView16.text = currentDeepLevel.toString()

        binding.button.setOnClickListener {
            goToHomeNested(currentDeepLevel)
        }

        binding.button2.setOnClickListener {
            goToAnimation()
        }

        binding.button3.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun goToHomeNested(level: Int) {
        findNavController().navigateUp()
        findNavController().navigate(
            R.id.action_homeNavigateFragment_self,
            Bundle().apply { putInt("deepLevel", level) }
        )
    }

    private fun goToAnimation() {
        findNavController().navigate(
            R.id.action_homeNavigateFragment_to_animationFragment2
        )
    }

}