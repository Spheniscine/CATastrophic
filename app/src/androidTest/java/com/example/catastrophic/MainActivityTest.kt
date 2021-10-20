package com.example.catastrophic

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.catastrophic.ui.adapter.GridAdapter
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun recyclerView_isDisplayed() {
        Espresso.onView(withId(R.id.recycler_view))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun clicking_on_grid_opens_pager() {
        Espresso.onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<GridAdapter.ImageViewHolder>(0, click()))

        Espresso.onView(withId(R.id.view_pager))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun clicking_on_grid_then_back_hides_pager() {
        Espresso.onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<GridAdapter.ImageViewHolder>(0, click()))

        Espresso.pressBack()

        Espresso.onView(withId(R.id.view_pager))
            .check(doesNotExist())
    }
}