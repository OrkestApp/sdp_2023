package com.github.orkest.View

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.ui.FollowListActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FollowListActivityTest {

    private lateinit var intent: Intent

    @get:Rule
    val composeTestRule = createAndroidComposeRule<FollowListActivity>()

    @Before
    fun setUp(){
        intent = Intent(ApplicationProvider.getApplicationContext(), FollowListActivity::class.java)
        intent.putExtra("username", "Philippe")
    }

    @Test
    fun backButton_navigatesBack() {
        Intents.init()
        // Click the back button
        composeTestRule.onNodeWithContentDescription("Back button").performClick()
        // Check if the Profile Activity has been launched
        intended(hasComponent(ProfileActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun displayFollowersList() {
        // Launch the activity with the followers parameter
        intent.putExtra("isFollowers", true)
        val scenario = launchActivity<FollowListActivity>(intent)

        // Check that the "Followers" text is displayed
        composeTestRule.onNodeWithText("Followers").assertExists()
        scenario.close()
    }

    @Test
    fun displayFollowingsList() {
        // Launch the activity with the followings parameter
        intent.putExtra("isFollowers", false)
        val scenario = launchActivity<FollowListActivity>(intent)

        // Check that the "Followings" text is displayed
        composeTestRule.onNodeWithText("Followings").assertExists()
        scenario.close()
    }


    @Test
    fun displayPreviewProfiles() {
        // Check that there are 3 profile previews displayed
        composeTestRule.onAllNodesWithContentDescription("Contact profile picture").assertCountEquals(3)
        composeTestRule.onAllNodesWithText("Philippe").assertCountEquals(3)
    }
}
