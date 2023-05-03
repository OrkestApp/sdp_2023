package com.github.orkest.View

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.orkest.ui.Camera.CameraView
import org.junit.Rule
import org.junit.Test

class CameraViewTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<CameraView>()

    @Test
    fun cameraPreviewBoxIsDisplayed(){
        composeTestRule.onNodeWithTag("Camera Preview Box").assertIsDisplayed()
    }

    @Test
    fun cameraPreviewIsDisplayed(){
        composeTestRule.onNodeWithTag("Camera Preview").assertIsDisplayed()
    }

    @Test
    fun switchCameraButtonDisplayed(){
        composeTestRule.onNodeWithTag("Switch Camera Button").assertIsDisplayed().assertHasClickAction()
    }


    @Test
    fun takePictureButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("Take Picture Button").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun backButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("Back Button").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun capturedImagePreviewIsDisplayed(){
        //click on take picture button
        composeTestRule.onNodeWithTag("Take Picture Button").performClick()

        Thread.sleep(1000) //Wait for the UI to update
        composeTestRule.onNodeWithContentDescription("Captured Image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Back Button").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithTag("Save Button").assertIsDisplayed().assertHasClickAction()
    }
}