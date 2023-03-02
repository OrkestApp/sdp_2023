package com.github.emilehreich.bootcamp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest{
    private lateinit var scenario: ActivityScenario<Activity>

    @get:Rule
    val composeAndroidRule = createEmptyComposeRule()


    @Before
    fun setUp() {

        scenario = ActivityScenario.launch(
            createActivityIntent(
                InstrumentationRegistry.getInstrumentation().targetContext,
            )
        )
    }

    private fun createActivityIntent(
        context: Context
    ): Intent {
        val intent = Intent(context, GreetingActivity::class.java).putExtra("name", "Steve")
        return intent
    }
    @Test
    fun greeting(){
        composeAndroidRule.onNode(hasText("Welcome Steve")).assertIsDisplayed()
    }

}




