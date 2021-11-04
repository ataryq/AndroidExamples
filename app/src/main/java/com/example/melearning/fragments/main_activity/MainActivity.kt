package com.example.melearning.fragments.main_activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.melearning.AppThemeController
import com.example.melearning.FragmentManagerUtils.Companion.checkClassCurrentFragment
import com.example.melearning.FragmentManagerUtils.Companion.showFragment
import com.example.melearning.FragmentManagerUtils.Companion.showFragmentAnimated
import com.example.melearning.R
import com.example.melearning.di.ActivityDaggerComponent
import com.example.melearning.di.MyViewModel
import com.example.melearning.fragments.AnimationFragment
import com.example.melearning.fragments.SettingsFragment
import com.example.melearning.fragments.calculation.CalculationFragment
import com.example.melearning.fragments.cicerone.CiceroneBaseFragment
import com.example.melearning.fragments.rx_fragment.RxFragment
import com.example.custom_ui.KeyboardController
import com.example.melearning.fragments.ScrollableFragment
import com.example.melearning.fragments.bottom_shirt.BottomShirtWindowFragment
import com.example.melearning.fragments.ShimmeringExampleFragment
import com.example.melearning.fragments.moxy.MoxyFragment
import com.example.melearning.fragments.paginating_sample.LightPaginating
import com.example.melearning.fragments.paging.view.PagingFragment
import dagger.Module
import dagger.Provides
import org.koin.android.viewmodel.ext.android.viewModel
import android.view.Menu


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    @SuppressLint("UseSupportActionBar")
    private fun setupTopAppBar() {
        val toolbar = findViewById<Toolbar>(R.id.activity_toolbar)
        setSupportActionBar(toolbar)

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
                    showFragmentAnimated<PagingFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_animation_fragment -> {
                    showFragmentAnimated<AnimationFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_rx_fragment -> {
                    showFragmentAnimated<RxFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_cicerone_example -> {
                    showFragmentAnimated<CiceroneBaseFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_tests -> {
                    startExamples()
                    true
                }
                R.id.app_bar_shimmering_example -> {
                    showFragmentAnimated<ShimmeringExampleFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_paginating_example -> {
                    showFragmentAnimated<LightPaginating>(supportFragmentManager)
                    true
                }
                R.id.app_bar_bottom_shirt -> {
                    showFragmentAnimated<BottomShirtWindowFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_moxy -> {
                    showFragmentAnimated<MoxyFragment>(supportFragmentManager)
                    true
                }
                R.id.app_bar_buffer -> {
                    showFragmentAnimated<ScrollableFragment>(supportFragmentManager)
                    true
                }

                else -> false
            }
        }
    }
}