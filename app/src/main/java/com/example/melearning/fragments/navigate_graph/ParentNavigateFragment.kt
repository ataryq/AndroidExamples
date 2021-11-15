package com.example.melearning.fragments.navigate_graph

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.melearning.R
import com.example.melearning.databinding.NavigateGraphFragmentBinding
import com.example.melearning.fragments.BaseBindFragment2
import com.example.melearning.fragments.main_activity.ActivityProvider

class ParentNavigateFragment:
    BaseBindFragment2<NavigateGraphFragmentBinding>(NavigateGraphFragmentBinding::inflate)
{
    override fun initViews() {
        val navHostFragment = childFragmentManager.
            findFragmentById(R.id.navigateFragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomAppBar.setupWithNavController(navController)

        (activity as ActivityProvider).setBackInterceptor {
            if(!navController.popBackStack()) {
                (activity as ActivityProvider).removeBackInterceptor()
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as ActivityProvider).removeBackInterceptor()
    }

}