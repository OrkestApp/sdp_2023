package com.github.orkest

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.github.orkest.View.sharing.SharingComposeActivity
import org.junit.Rule
import org.junit.Test

class SharingMusicTest {

    // to launch sharingActivity when running tests
    @get:Rule
    val intentsTestRule = IntentsTestRule(SharingComposeActivity::class.java)


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHandling() {

        val resultData = Intent()
        val url = "https://open.spotify.com/track/2TpxZ7JUBn3uw46aR7qd6V?si=2e9f6f6f6f6f6f6f"
        resultData.putExtra("url", url)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(toPackage("com.github.orkest.View.sharing")).respondWith(result)

        composeTestRule.onNodeWithText("Select a user to share with").assertIsDisplayed()
        composeTestRule.onNodeWithText("search User").assertIsDisplayed()
    }


}