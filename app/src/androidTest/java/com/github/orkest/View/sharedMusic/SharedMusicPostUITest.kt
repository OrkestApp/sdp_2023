package com.github.orkest.View.sharedMusic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.Constants
import com.github.orkest.Model.Profile
import com.github.orkest.Model.Song
import com.github.orkest.View.sharing.ui.theme.OrkestTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedMusicPostUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp(){
        val song = Constants.DUMMY_RUDE_BOY_SONG
        val message = "It's amazing."
        val profile = Profile(username = "JohnDoe")

        // Launch the composable function
        composeTestRule.setContent {
            OrkestTheme {
                sharedMusicPost(profile, song, message)
            }
        }
    }


    @Test
    fun songCoverIsDisplayed(){
        composeTestRule.onNodeWithContentDescription("Cover of the album")
            .assertIsDisplayed()
    }

    // Test if the song name, author, and album are displayed correctly
    @Test
    fun songInfoAreDisplayed(){
        composeTestRule.onNodeWithText("Rude Boy")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Rihanna (Rated R)")
            .assertIsDisplayed()
    }

    @Test
    fun profilePictureIsDisplayed(){
        composeTestRule.onNodeWithContentDescription("Profile Picture of the user")
            .assertIsDisplayed()
    }

    @Test
    fun playButtonIsDisplayed(){
        composeTestRule.onNodeWithContentDescription("Play button")
            .assertIsDisplayed()
    }

    @Test
    fun messageIsDisplayed(){
        composeTestRule.onNodeWithText("It's amazing.")
            .assertIsDisplayed()
    }

    @Test
    fun informativeTextIsDisplayed(){
        composeTestRule.onNodeWithText("JohnDoe shared this music with you")
            .assertIsDisplayed()
    }

    @Test
    fun addButtonToPlayListIsDisplayed() {
        composeTestRule.onNodeWithContentDescription("Add to playlist")
            .assertIsDisplayed()
    }

    @Test
    fun clickableComponentsReactWell(){
        composeTestRule.onNodeWithContentDescription("Add to playlist")
            .assert(hasClickAction())
        composeTestRule.onNodeWithContentDescription("Play button")
            .assert(hasClickAction())
        composeTestRule.onNodeWithContentDescription("Profile Picture of the user")
            .assert(hasClickAction())
    }

}