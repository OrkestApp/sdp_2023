package com.github.orkest.View

import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.rule.GrantPermissionRule
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test


/**
 * Remove Ignore, works on a local machine but not on the cirrus ci
 * Because it does not have a camera
 */
class CameraMockEnvironmentTesting {

    //@get:Rule Enable for testing on local machine
    //val composeTestRule = createAndroidComposeRule<MockCamera>()

    @get:Rule
    var permissionCamera: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    @Before
    fun setUp() {}

    /**
     * This test checks that the CameraView is displayed correctly
     */
    @Ignore
    @Test
    fun testCapturedImage() {
        // Provide a mock URI
        val mockUri = Uri.parse(
            "/Users/evguenirousselot/Desktop/sdp_2023/app/src/androidTest/java/com/github/orkest/View/test_image.jpeg\n")

        composeTestRule.setContent {
            // Call the CapturedImage function
            composeTestRule.activity.cameraView.CapturedImage(
                capturedImageUri = mockUri,
                onBackClick = { /* Do nothing in this test */ }
            )
        }

        // Assert that the expected components are displayed
        composeTestRule.onNodeWithContentDescription("Captured Image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Back Button").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithTag("Save Button").assertIsDisplayed().assertHasClickAction()
    }

    /**
     * This test checks that the TakePictureButton is displayed correctly
     */
    @Ignore
    @Test
    fun testTakePictureButton() {
        var pictureTaken = false

        composeTestRule.setContent {
            // Call the TakePictureButton function
            composeTestRule.activity.cameraView.TakePictureButton(
                onTakePictureClick = { pictureTaken = true },
                modifier = Modifier
            )
        }

        // Assert that the button is displayed
        composeTestRule.onNodeWithContentDescription("Take Picture Button").assertIsDisplayed()

        // Click the button
        composeTestRule.onNodeWithContentDescription("Take Picture Button").performClick()

        // Assert that the click action was performed
        assert(pictureTaken)
    }

    /**
     * This test checks that the SwitchCameraButton is displayed correctly
     */
    @Ignore
    @Test
    fun testCameraPreview() {

        composeTestRule.activity.cameraView.hasCamera = true

        composeTestRule.setContent {
            // Call the CameraPreview function
            composeTestRule.activity.cameraView.CameraPreview(
                lifecycleOwner = composeTestRule.activity,
                onImageCaptured = { /* Do nothing in this test */ },
                true
            )
        }

        // Assert that the expected components are displayed
        composeTestRule.onNodeWithTag("Camera Preview Box").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Take Picture Button").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithTag("Switch Camera Button").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithTag("Back Button").assertIsDisplayed().assertHasClickAction()
    }

}