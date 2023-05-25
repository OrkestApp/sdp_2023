package com.github.orkest

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.data.Constants
import com.github.orkest.ui.feed.CreatePost
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatePostTest {

    @Test
    fun onCreateTest() {
        // Define your intent data
        val intent = Intent(ApplicationProvider.getApplicationContext(), CreatePost::class.java).apply {
            putExtra("isVideo", true)
            putExtra("URI", "your_uri")
            putExtra(Constants.SONG_NAME, "song_name")
            putExtra(Constants.SONG_ARTIST, "song_artist")
            putExtra(Constants.SONG_ALBUM, "song_album")
        }

        // Launch the activity with the intent
        val scenario = ActivityScenario.launch<CreatePost>(intent)

        // Add your verification code here

        // Close the activity at the end of the test
        scenario.close()
    }

}
