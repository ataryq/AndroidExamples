package com.example.melearning

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.melearning.fragments.testing.EspressoUiTestsFragment
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EspressoUiTest {

    private lateinit var scenario: FragmentScenario<EspressoUiTestsFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.CustomAppThemeDark)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    //test check visibility and perform click
    @Test
    fun testButton() {
        onView(withId(R.id.button6)).check(matches(isDisplayed()))
        onView(withId(R.id.button6)).perform(click())
        onView(withId(R.id.button6)).check(matches(not(isDisplayed())))
    }

    //test input edittext and check text
    @Test
    fun testInputFields() {
        onView(withId(R.id.editTextTextPersonName4)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextTextPersonName5)).check(matches(isDisplayed()))

        onView(withId(R.id.editTextTextPersonName4)).perform(clearText())
        onView(withId(R.id.editTextTextPersonName4)).perform(typeText("hello"))
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.editTextTextPersonName5)).check(matches(withText("hello")))
    }
}