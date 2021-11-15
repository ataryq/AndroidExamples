package com.example.melearning.fragments.main_activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.custom_ui.KeyboardController
import com.example.melearning.AppThemeController
import com.example.melearning.FragmentManagerUtils.Companion.checkClassCurrentFragment
import com.example.melearning.FragmentManagerUtils.Companion.showFragment
import com.example.melearning.FragmentManagerUtils.Companion.showFragmentAnimated
import com.example.melearning.R
import com.example.melearning.databinding.ActivityMainBinding
import com.example.melearning.di.ActivityDaggerComponent
import com.example.melearning.di.MyViewModel
import com.example.melearning.fragments.ScrollableFragment
import com.example.melearning.fragments.SettingsFragment
import com.example.melearning.fragments.ShimmeringExampleFragment
import com.example.melearning.fragments.animation.AnimationFragment
import com.example.melearning.fragments.animation.ConstraintLayoutAnimationFragment
import com.example.melearning.fragments.bottom_shirt.BottomShirtWindowFragment
import com.example.melearning.fragments.calculation.CalculationFragment
import com.example.melearning.fragments.cicerone.CiceroneBaseFragment
import com.example.melearning.fragments.moxy.MoxyFragment
import com.example.melearning.fragments.navigate_graph.ParentNavigateFragment
import com.example.melearning.fragments.paginating_sample.LightPaginating
import com.example.melearning.fragments.paging.view.PagingFragment
import com.example.melearning.fragments.rx_fragment.RxFragment
import dagger.Module
import dagger.Provides
import org.koin.android.viewmodel.ext.android.viewModel


@Module
class MainActivity : AppCompatActivity(), ActivityProvider {
    @Provides fun providerActivity() = this as AppCompatActivity
    @Provides fun providerContext(): Context = applicationContext
    private lateinit var themeController: AppThemeController
    private val viewModel: MainActivityViewModel by viewModels()
    private var toolbarNavIcon: Drawable? = null
    val myKoinViewModel: MyViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeController = AppThemeController(this)
        setTheme(themeController.getTheme())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun setToolbarTitle(title: String) {
        binding.activityTitleToolbar.text = title
    }

    private fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

    @SuppressLint("UseSupportActionBar")
    private fun setupTopAppBar() {
        val toolbar = findViewById<Toolbar>(R.id.activity_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbarNavIcon = toolbar.navigationIcon

        removeBackInterceptor()

        viewModel.backIconVisible.observe(this, {
            toolbar.navigationIcon = if(it) toolbarNavIcon else null
            if(!it) {
                setToolbarTitle(getApplicationName(applicationContext))
            }
        })

        setToolbarTitle(getApplicationName(applicationContext))

        toolbar.setOnMenuItemClickListener { menuItem ->
            setToolbarTitle(menuItem.title.toString())

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

                R.id.app_bar_scrollable -> {
                    showFragmentAnimated<ScrollableFragment>(supportFragmentManager)
                    true
                }

                R.id.app_bar_navigation -> {
                    showFragmentAnimated<ParentNavigateFragment>(supportFragmentManager)
                    true
                }

                R.id.app_bar_buffer -> {
                    showFragmentAnimated<ConstraintLayoutAnimationFragment>(supportFragmentManager)
                    true
                }

                else -> false
            }
        }
    }

    override fun getToolbar(): Toolbar? = findViewById(R.id.activity_toolbar)

    override fun setBackInterceptor(callback: () -> Unit) {
        getToolbar()?.setNavigationOnClickListener {
            callback()
        }
    }

    override fun removeBackInterceptor() {
        getToolbar()?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}