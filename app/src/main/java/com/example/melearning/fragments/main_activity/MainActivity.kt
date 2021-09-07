package com.example.melearning.fragments.main_activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.melearning.AppThemeController
import com.example.melearning.FragmentManagerUtils.Companion.checkClassCurrentFragment
import com.example.melearning.FragmentManagerUtils.Companion.showFragment
import com.example.melearning.FragmentManagerUtils.Companion.showFragmentWithDefaultAnim
import com.example.melearning.R
import com.example.melearning.di.ActivityDaggerComponent
import com.example.melearning.di.MyViewModel
import com.example.melearning.fragments.AnimationFragment
import com.example.melearning.fragments.SettingsFragment
import com.example.melearning.fragments.SharedViewFragment
import com.example.melearning.fragments.calculation.CalculationFragment
import com.example.melearning.fragments.cicerone.CiceroneBaseFragment
import com.example.melearning.fragments.rx_fragment.RxFragment
import com.example.custom_ui.KeyboardController
import com.example.melearning.fragments.ShimmeringExampleFragment
import com.example.melearning.fragments.paging.PagingFragment
import com.google.android.material.appbar.MaterialToolbar
import dagger.Module
import dagger.Provides
import org.koin.android.viewmodel.ext.android.viewModel

@Module
class MainActivity : AppCompatActivity() {
    @Provides fun providerActivity() = this as AppCompatActivity
    @Provides fun providerContext(): Context = applicationContext
    private lateinit var themeController: AppThemeController
    private val viewModel: MainActivityViewModel by viewModels()
    private var toolbarNavIcon: Drawable? = null
    val myKoinViewModel: MyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeController = AppThemeController(this)
        setTheme(themeController.getTheme())

        setContentView(R.layout.activity_main)
        ActivityDaggerComponent.instance.build(this)
        KeyboardController.instance.initialize(this)

        setupTopAppBar()
        supportFragmentManager.addOnBackStackChangedListener {
            checkBackButtonVisibility()
        }
        showFragment<CalculationFragment>(supportFragmentManager)

        startExamples()
    }

    private fun startExamples() {
//        startLiveDataTest()
//        startKotlinFlowTests()
//        startKoinTests(this)
    }

    private fun checkBackButtonVisibility() {
        val isNotHome = !checkClassCurrentFragment<CalculationFragment>(supportFragmentManager)
        if(isNotHome != viewModel.backIconVisible.value)
            viewModel.backIconVisible.postValue(isNotHome)
    }

    private fun setupTopAppBar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.activity_toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        toolbarNavIcon = toolbar.navigationIcon

        viewModel.backIconVisible.observe(this, {
            toolbar.navigationIcon = if(it) toolbarNavIcon else null
        })

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.app_bar_settings -> {
                    showFragment<SettingsFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_shared_fragment -> {
                    showFragmentWithDefaultAnim<SharedViewFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_animation_fragment -> {
                    showFragmentWithDefaultAnim<AnimationFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_rx_fragment -> {
                    showFragmentWithDefaultAnim<RxFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_cicerone_example -> {
                    showFragmentWithDefaultAnim<CiceroneBaseFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_tests -> {
                    startExamples()
                    true
                }
                R.id.app_bar_shimmering_example -> {
                    showFragmentWithDefaultAnim<ShimmeringExampleFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_paginating_example -> {
                    showFragmentWithDefaultAnim<PagingFragment>(supportFragmentManager)
                    true
                }

                else -> false
            }
        }
    }
}