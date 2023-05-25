package com.github.orkest.View

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import com.github.orkest.data.Constants
import com.github.orkest.shazam.domain.ShazamConstants
import com.github.orkest.ui.Camera.CameraView
import com.github.orkest.ui.feed.CreatePost
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CameraViewTest {

    private lateinit var cameraView: CameraView

    @get:Rule
    val composeTestRule = createAndroidComposeRule<CameraView>()

    @Before
    fun setUp(){
        cameraView = composeTestRule.activity
    }

    @Test
    fun cameraPreviewBoxIsDisplayed(){
        composeTestRule.onNodeWithTag("Camera Preview Box").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Back Button").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun cameraPreviewIsDisplayed(){
        if(cameraView.hasCameraAccess){
            composeTestRule.onNodeWithTag("Camera Preview").assertIsDisplayed()
            composeTestRule.onNodeWithTag("Video").assertIsDisplayed()
            composeTestRule.onNodeWithTag("Picture").assertIsDisplayed()
            composeTestRule.onNodeWithTag("Switch Camera Button").assertIsDisplayed().assertHasClickAction()
            composeTestRule.onNodeWithTag("Take Picture Button").assertIsDisplayed().assertHasClickAction()
        } else {
            composeTestRule.onNodeWithTag("No Camera Text").assertIsDisplayed()
        }
    }

    @Test
    fun takeVideoButtonGetsDisplayed(){
        if(cameraView.hasCameraAccess){
            //Go to the video mode
            composeTestRule.onNodeWithTag("Video").performClick()
            Thread.sleep(3000)
            // Perform video recording. Click twice on the button to record and finish recording
            composeTestRule.onNodeWithTag("Take Video Button").assertIsDisplayed().assertHasClickAction().performClick()
            Thread.sleep(3000)
            composeTestRule.onNodeWithTag("Take Video Button").performClick()
            Thread.sleep(10000)
            //composeTestRule.onNodeWithTag("Captured Video").assertIsDisplayed()
        }
    }
    

    @Test
    fun capturedImagePreviewIsDisplayed(){
        if(cameraView.hasCameraAccess) {
            composeTestRule.onNodeWithTag("Take Picture Button").assertIsDisplayed().assertHasClickAction()
            //click on take picture button
            composeTestRule.onNodeWithTag("Take Picture Button").performClick()
            Thread.sleep(10000) //Wait for the UI to update
            composeTestRule.onNodeWithContentDescription("Captured Image").assertIsDisplayed()
            composeTestRule.onNodeWithTag("Back Button").assertIsDisplayed().assertHasClickAction()
            composeTestRule.onNodeWithTag("Save Button").assertIsDisplayed().assertHasClickAction()
        }
    }

    @Test
    fun takePictureSendsIntentToPostCreationWithShazamSong() {
        Intents.init()
        //Set the song to be found
        ShazamConstants.SONG_FOUND = Constants.DUMMY_RUDE_BOY_SONG
        //click on take picture button
        composeTestRule.onNodeWithTag("Take Picture Button").performClick()
        Thread.sleep(3000) //Wait for the UI to update
        //Click on save button
        composeTestRule.onNodeWithTag("Save Button").performClick()
        //Check if the intent was sent to the post creation activity
        Intents.intended(hasComponent(CreatePost::class.java.name))
        //Check if the intent was sent to the post creation activity with correct extras
        Intents.intended(hasExtra(Constants.SONG_NAME, Constants.DUMMY_RUDE_BOY_SONG.Title))
        Intents.intended(hasExtra(Constants.SONG_ARTIST, Constants.DUMMY_RUDE_BOY_SONG.Artist))
        Intents.release()
    }


}