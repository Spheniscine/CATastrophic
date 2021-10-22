package com.github.spheniscine.catastrophic

import android.content.pm.ActivityInfo
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.spheniscine.catastrophic.ui.adapter.GridAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    companion object {
        const val OFFSCREEN_POSITION = 25
    }

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private fun onActivity(action: (MainActivity) -> Unit) =
        activityScenarioRule.scenario.onActivity(action)

    @Test
    fun recyclerView_isDisplayed() {
        Espresso.onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
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

    // If this test fails on a device, MainActivityTest.OFFSCREEN_POSITION must be increased
    @Test
    fun OFFSCREEN_POSITION_is_really_offscreen() {
        Espresso.onView(withId(R.id.recycler_view))
            .check(matches(not(withViewAtPosition(OFFSCREEN_POSITION, isDisplayed()))))
    }

    @Test
    fun scrolling_viewPager_affects_recyclerView_after_back() {
        Espresso.onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<GridAdapter.ImageViewHolder>(0, click()))

        repeat(OFFSCREEN_POSITION) {
            Espresso.onView(withId(R.id.view_pager))
                .perform(swipeLeft())
        }

        Espresso.pressBack()

        Espresso.onView(withId(R.id.recycler_view))
            .check(matches(withViewAtPosition(OFFSCREEN_POSITION, isDisplayed())))
    }

    @Test
    fun offscreen_scroll_is_preserved() {
        onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        Espresso.onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<GridAdapter.ImageViewHolder>(
                OFFSCREEN_POSITION))

        onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        Espresso.onView(withId(R.id.recycler_view))
            .check(matches(withViewAtPosition(OFFSCREEN_POSITION, isDisplayed())))
    }
}