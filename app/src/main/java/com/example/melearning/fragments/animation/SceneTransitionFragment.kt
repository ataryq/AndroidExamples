package com.example.melearning.fragments.animation

import android.transition.ChangeBounds
import android.transition.Scene
import android.transition.TransitionManager
import com.example.melearning.databinding.SceneTransitionFragmentBinding
import com.example.melearning.fragments.BaseBindFragment2
import com.example.melearning.R
import com.example.melearning.databinding.Scene2TransitionFragmentBinding


class SceneTransitionFragment:
    BaseBindFragment2<SceneTransitionFragmentBinding>(SceneTransitionFragmentBinding::inflate) {
    override fun initViews() {

        val scene2: Scene = Scene.getSceneForLayout(
            binding.root,
            R.layout.scene_2_transition_fragment,
            requireContext()
        )

        val scene2Binding = Scene2TransitionFragmentBinding.bind(scene2.sceneRoot)

        binding.button4.setOnClickListener {
            TransitionManager.go(scene2, ChangeBounds())
        }

        binding.button5.setOnClickListener {
            println("back clicked")
        }

        scene2Binding.button5.setOnClickListener {
            println("back 2 clicked")
            scene2.exit()
        }

    }
}