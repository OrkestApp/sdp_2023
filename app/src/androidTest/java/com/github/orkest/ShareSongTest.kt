package com.github.orkest

import com.github.orkest.domain.Authorization
import org.junit.Test
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.orkest.data.Constants
import com.github.orkest.ui.sharing.SharingComposeActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule



class SpotifyAuthorizationTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(SharingComposeActivity::class.java)

    @Before
    fun setUp() {
        // Initialize the Intents class
        Intents.init()
        Constants.CURRENT_LOGGED_USER= "Power Ranger Yas"
        Log.d("LA CON DE TES MORTS BEFORE", Constants.CURRENT_LOGGED_USER)

    }

    @After
    fun tearDown() {
        // Release the resources used by the Intents class
        Intents.release()
    }


    @Test
    fun testCodeVerifier(){
        val codeVerifier = Authorization.generateRandomString()
        assert(codeVerifier.length <= 128)
    }


    @Test
    fun testShareSong(){
        // Define the extra text and the action for the intent

        // add spotify song

        val extraText = "https://open.spotify.com/track/2TpxZ7JUBn3uw46aR7qd6V?si=2b8e1b8b5f2a4f0f"
        val action = Intent.ACTION_SEND

        // Create the intent with the extra text and the action
        val intent = Intent(action).apply {
            putExtra(Intent.EXTRA_TEXT, extraText)
            type = "text/plain"
        }

        // Launch the activity with the intent
        activityRule.scenario.onActivity { activity ->
            activity.startActivity(intent)
        }

        // Verify that the activity was launched with the intent
        Intents.intended(hasAction(action))
        Intents.intended(hasExtraWithKey(Intent.EXTRA_TEXT))
        Intents.intended(hasExtra(Intent.EXTRA_TEXT, extraText))

        // check that the activity runs without errors till the end of its lifecycle
        activityRule.scenario.moveToState(Lifecycle.State.DESTROYED)

    }





}



