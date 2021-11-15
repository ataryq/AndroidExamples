package com.example.melearning.fragments.animation

import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import com.example.melearning.databinding.ConstraintAnimationFragmentBinding
import com.example.melearning.fragments.BaseBindFragment2

class ConstraintLayoutAnimationFragment:
    BaseBindFragment2<ConstraintAnimationFragmentBinding>(ConstraintAnimationFragmentBinding::inflate)
{
    override fun initViews() {
        with(binding) {
            start.setOnClickListener {
                val fade: Transition = Fade()
                fade.duration = 1000
                TransitionManager.beginDelayedTransition(rootLayout, fade)
                start.visibility = View.GONE
                group.visibility = View.VISIBLE

            }
        }
    }
}