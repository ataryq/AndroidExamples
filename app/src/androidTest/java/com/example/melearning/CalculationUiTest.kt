package com.example.melearning

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.melearning.fragments.calculation.CalculationFragment
import com.example.melearning.fragments.main_activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculationUiTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    lateinit var fragment: CalculationFragment
    lateinit var db: DataProvider

    @Before
    fun setUp() {
        runBlocking(Dispatchers.Main) {
            val context = ApplicationProvider.getApplicationContext<Context>()

            activityScenarioRule.scenario.onActivity { it ->
                fragment = it.supportFragmentManager.fragments.last() as CalculationFragment
                db = fragment.mDb
            }
        }
    }


    @Test
    fun calcualtionTest() {
        onView(withId(R.id.resultTV)).check(matches(ViewMatchers.withText("1644761,04")))
        onView(withId(R.id.startSumET)).check(matches(isDisplayed()))
        onView(withId(R.id.startSumET)).perform(clearText())
        onView(withId(R.id.startSumET)).perform(typeText("100"))
        onView(withId(R.id.resultTV)).check(matches(ViewMatchers.withText("836923,80")))
    }

    private fun setTextToEditText(id: Int, text: String) {
        onView(withId(id)).perform(clearText())
        onView(withId(id)).perform(typeText(text))
        Espresso.closeSoftKeyboard()
    }

    @Test
    fun checkDatabase() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)

        setTextToEditText(R.id.incomeET, "100")
        setTextToEditText(R.id.startSumET, "1000")
        setTextToEditText(R.id.percentET, "12")
        setTextToEditText(R.id.numPeriodsET, "60")

        onView(withId(R.id.saveResultButton)).perform(click())
        onView(withId(R.id.saveResultButton)).perform(click())

        runBlocking(Dispatchers.Main) {
            Thread.sleep(100)

            db.getAll().observeForever { item ->
                assert(item.last().income == 100.0)
                assert(item.last().initial == 1000.0)
                assert(item.last().percent == 12.0)
                assert(item.last().periods == 60.0)
            }
        }

        println("checkDatabase_end")
    }
}