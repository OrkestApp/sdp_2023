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
        if(cameraView.hasCamera){
            composeTestRule.onNodeWithTag("Camera Preview").assertIsDisplayed()
            composeTestRule.onNodeWithTag("Switch Camera Button").assertIsDisplayed().assertHasClickAction()
            composeTestRule.onNodeWithTag("Take Picture Button").assertIsDisplayed().assertHasClickAction()
        } else {
            composeTestRule.onNodeWithTag("No Camera Text").assertIsDisplayed()
        }

    }

    @Test
    fun capturedImagePreviewIsDisplayed(){
        if(cameraView.hasCamera) {
            //click on take picture button
            composeTestRule.onNodeWithTag("Take Picture Button").performClick()

            Thread.sleep(1000) //Wait for the UI to update
            composeTestRule.onNodeWithContentDescription("Captured Image").assertIsDisplayed()
            composeTestRule.onNodeWithTag("Back Button").assertIsDisplayed().assertHasClickAction()
            composeTestRule.onNodeWithTag("Save Button").assertIsDisplayed().assertHasClickAction()
        }
    }
}