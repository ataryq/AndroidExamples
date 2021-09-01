package com.example.melearning.fragments.cicerone

import android.os.Bundle
import android.view.View
import com.example.melearning.R
import com.example.melearning.databinding.CiceroneBaseFragmentBinding
import com.example.melearning.fragments.BaseBindFragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

class CiceroneBaseFragment: BaseBindFragment<CiceroneBaseFragmentBinding>() {
    override fun layoutId() = R.layout.cicerone_base_fragment

    private val cicerone = Cicerone.create()
    private val router get() = cicerone.router
    private val navigatorHolder get() = cicerone.getNavigatorHolder()
    private var navigator: AppNavigator? = null
    private var childNum = 0
    private var rootChildFragment: FragmentScreen = goToNextChild()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            navigator = AppNavigator(it, R.id.ciceroneContainerFragment)
            navigatorHolder.setNavigator(navigator!!)
            router.navigateTo(rootChildFragment)

            binding.goNextChildFragment.setOnClickListener {
                router.navigateTo(goToNextChild())
            }
            binding.goToStart.setOnClickListener {
                router.backTo(rootChildFragment)
            }
        }
    }

    private fun goToNextChild() = FragmentScreen {
        CiceroneChildFragment(++childNum)
    }

    override fun onStart() {
        super.onStart()
        navigator?.let { navigatorHolder.setNavigator(it) }
    }

    override fun onStop() {
        navigatorHolder.removeNavigator()
        super.onStop()
    }
}