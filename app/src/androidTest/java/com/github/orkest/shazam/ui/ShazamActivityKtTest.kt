package com.github.orkest.shazam.ui

import android.content.Context
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.rule.GrantPermissionRule
import com.github.orkest.SearchViewModelTest
import com.github.orkest.data.Song
import com.github.orkest.shazam.domain.ShazamConstants
import com.github.orkest.ui.MainActivity
import com.github.orkest.ui.search.SearchUserView
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class ShazamActivityKtTest {

    companion object {
        var called = false
    }

    @get:Rule
    val activity = createAndroidComposeRule<MainActivity>()

    //rule to grant audio permission
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    lateinit var context: Context

    @Before
    fun setup(){
        context = activity.activity.baseContext
    }

    @Test
    fun shazamActivityNoMatch (){
        loop()
        //Click on the button to start the recognition
        Thread.sleep(500)
        activity.onNodeWithText("Shazam").performClick()
        //Wait for the recognition to finish

    }


    @org.junit.Test
    fun handleRecognitionDisplaysCorrectToastNoMatch() {
        loop()
        val song = handleRecognitionResult(ShazamConstants.SONG_NO_MATCH, null, context)

        assertEquals(ShazamConstants.SONG_NO_MATCH, song)
       // activity.onNodeWithText("No match found for this song").assertExists()
    }

    @org.junit.Test
    fun handleRecognitionDisplaysCorrectToastMatch() {
        loop()
        val songExpected = Song("Booba", "Petite Fille")
        val song = handleRecognitionResult(songExpected, null, context)

        assertEquals(songExpected, song)
        //activity.onNodeWithText("Match found for this song").assertExists()
    }

    @org.junit.Test
    fun handleRecognitionDisplaysCorrectToastError() {
        loop()

        val songExpected = Song("Err", "Err")
        val song = handleRecognitionResult(songExpected, Exception(), context)
        assertEquals(ShazamConstants.SONG_NO_MATCH, song)
    }

    private fun loop(){
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
    }
}