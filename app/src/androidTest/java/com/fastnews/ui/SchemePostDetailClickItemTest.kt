package com.fastnews.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fastnews.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SchemePostDetailClickItemTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(SchemeActivity::class.java)

    @Test
    fun schemePostDetailSareTest() {
        Thread.sleep(6000)
        val cardView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.timeline_rv),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val textView = onView(
            allOf(
                withText("Comments"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Comments")))

        pressBack()
        Thread.sleep(4000)

        val cardView4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.timeline_rv),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        cardView4.perform(click())

        val cardView5 = onView(
            allOf(
                withId(R.id.include_detail_post_header),
                childAtPosition(
                    allOf(
                        withId(R.id.detail_post_container),
                        childAtPosition(
                            withClassName(`is`("android.widget.ScrollView")),
                            0
                        )
                    ),
                    0
                )
            )
        )
        cardView5.perform(scrollTo(), click())

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navegar para cima"),
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        Thread.sleep(4000)

        val cardView6 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.timeline_rv),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        cardView6.perform(click())

        pressBack()
        Thread.sleep(4000)

        val recyclerView = onView(
            allOf(
                withId(R.id.timeline_rv),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomsheet),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
