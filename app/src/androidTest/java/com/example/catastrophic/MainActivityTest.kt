package com.example.catastrophic

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.ViewPagerActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.catastrophic.ui.adapter.GridAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

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
            .check(matches(isDisplayed()))
    }

    @Test
    fun clicking_on_grid_then_back_hides_pager() {
        Espresso.onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<GridAdapter.ImageViewHolder>(0, click()))

        Espresso.pressBack()

        Espresso.onView(withId(R.id.view_pager))
            .check(doesNotExist())
    }

    fun withViewAtPosition(position: Int, itemMatcher: Matcher<View>) =
        object: BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val vh = item?.findViewHolderForAdapterPosition(position)
                return vh != null && itemMatcher.matches(vh.itemView)
            }
        }

    @Test
    fun scrolling_viewPager_affects_recyclerView_after_back() {
        Espresso.onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<GridAdapter.ImageViewHolder>(0, click()))

        repeat(25) {
            Espresso.onView(withId(R.id.view_pager))
                .perform(swipeLeft())
        }

        Espresso.pressBack()

        Espresso.onView(withId(R.id.recycler_view))
            .check(matches(withViewAtPosition(25, isDisplayed())))
    }
}