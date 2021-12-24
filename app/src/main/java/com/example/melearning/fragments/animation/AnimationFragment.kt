package com.example.melearning.fragments.animation

import android.animation.ObjectAnimator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.example.custom_ui.doOnEnd
import com.example.custom_ui.startAnimationFromId
import com.example.melearning.R
import com.example.melearning.databinding.AnimationFragmentBinding
import com.example.melearning.fragments.BaseBindFragment

class AnimationFragment: BaseBindFragment<AnimationFragmentBinding>() {
    override fun layoutId() = R.layout.animation_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.greenSquare.apply {
            setOnClickListener {
                animate().translationXBy(100f)
                    .translationYBy(100f)
                    .setDuration(400)
                    .setInterpolator(DecelerateInterpolator())
                    .doOnEnd {
                        animate().setListener(null)
                        animate().setDuration(700).alphaBy(-0.4f).start()
                    }
                    .start()
            }
        }

        binding.warningButton.apply {
            setOnClickListener {
                it.startAnimationFromId(R.anim.little_shaking)
            }
        }

        binding.hideButton.setOnClickListener {
            binding.hideLayout.animate()
                .setInterpolator(FastOutLinearInInterpolator())
                .setDuration(300)
                .translationX(1000f)
                .start()
        }

        binding.pressButton.setOnClickListener {
            ObjectAnimator.ofFloat(it, "scaleX", 3f)
                .setDuration(1000)
                .start()
        }

        binding.playPauseButton.setOnClickListener {
            val avd: AnimatedVectorDrawable = binding.playPauseButton.drawable as AnimatedVectorDrawable
            avd.start()
        }
    }
}
