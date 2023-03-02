package com.github.emilehreich.bootcamp

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.emilehreich.bootcamp.MainActivity.Companion.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun setAndGet() {
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        database = Firebase.database
        database.useEmulator("10.0.2.2", 9000)

        onView(withId(R.id.phoneBox)).perform(typeText("1234567890"))
        onView(withId(R.id.mailBox)).perform(typeText("test@wouhou.ch"))

        onView(withId(R.id.setButton)).perform(click())

        onView(withId(R.id.mailBox)).perform(typeText(""))
        onView(withId(R.id.getButon)).perform(click())

        // check if displayed mail is correct
        onView(withText("test@wouhou.ch")).check(matches(isDisplayed()))
    }

}