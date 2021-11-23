package com.example.melearning.fragments.animation

import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import com.example.melearning.R
import com.example.melearning.databinding.ConstraintAnimFragmentBinding
import com.example.melearning.fragments.BaseBindFragment2

class ConstraintLayoutAnimationFragment:
    BaseBindFragment2<ConstraintAnimFragmentBinding>(ConstraintAnimFragmentBinding::inflate)
{
    override fun initViews() {
        with(binding) {
            start.setOnClickListener {
                val set = ConstraintSet()
                set.clone(context, R.layout.constraint_anim_fragment_2)
                set.applyTo(rootLayout)

                val transition: Transition = ChangeBounds().apply {
                    interpolator = OvershootInterpolator()
                    duration = 400
                }

                TransitionManager.beginDelayedTransition(rootLayout, transition)
            }
        }
    }
}