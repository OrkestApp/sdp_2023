package com.github.orkest.View

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.orkest.ui.Camera.CameraView
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
            //Thread.sleep(10000)
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

}