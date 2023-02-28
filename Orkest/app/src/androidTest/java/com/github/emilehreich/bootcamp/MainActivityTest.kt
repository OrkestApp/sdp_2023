package com.github.emilehreich.bootcamp

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testWritingOfName() {
        onView(ViewMatchers.withId(R.id.mainText)).perform(ViewActions.replaceText("Steve"))
        closeSoftKeyboard()
        onView(withText("Steve")).check(matches(isDisplayed()))
    }

    @Test
    fun testStartButton() {
        Intents.init()

        onView(withId(R.id.mainText)).perform(ViewActions.replaceText("Steve"))
        closeSoftKeyboard()

        onView(withId(R.id.mainButton)).perform(click())
        intended(
            allOf(
                hasComponent(GreetingActivity::class.java.name),
                hasExtra("name", "Steve")
            )
        )

        Intents.release()
    }

}