package com.example.melearning

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.melearning.fragments.calculation.CalculationFragment
import com.example.melearning.fragments.main_activity.MainActivity
import com.example.melearning.fragments.testing.EspressoUiTestsFragment
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelpExamples {
    @get:Rule var activityScenarioRule = ActivityTestRule(MainActivity::class.java)
    @get:Rule var activityScenarioRule2 = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var scenario: FragmentScenario<EspressoUiTestsFragment>

    fun getCurrentFragment(): Fragment =
        activityScenarioRule.activity.supportFragmentManager.fragments.last()

    fun getCurrentFragment2() {
        activityScenarioRule2.scenario.onActivity {
            it.supportFragmentManager.fragments.last()
        }
    }

    fun getContext(): Context = ApplicationProvider.getApplicationContext<Context>()
}