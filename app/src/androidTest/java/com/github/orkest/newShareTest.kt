package com.github.orkest

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class newShareTest {
    @get:Rule
    var composeTestRule = createComposeRule()

    @Test
    fun verifyShareWithFriendButton() {
        // Verify that the "Share with a friend" button is displayed
        composeTestRule.setContent {
            Column {
                Text(text = "Share with a friend or publish a post?")
                val context = LocalContext.current
                Button(onClick = {
                    // share with a friend


                }) {
                    Text(text = "Share with a friend")
                }
                Button(onClick = {

                }) {
                    Text(text = "Publish a post")
                }
            }
        }
        composeTestRule.onNodeWithText("Share with a friend or publish a post?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Share with a friend").assertIsDisplayed()
        composeTestRule.onNodeWithText("Publish a post").assertIsDisplayed()

        composeTestRule.onNodeWithText("Publish a post").performClick()


    }

    @Test
    fun verifyPostButton() {
        // Verify that the "Share with a friend" button is displayed
        composeTestRule.setContent {
            Column {
                Text(text = "Share with a friend or publish a post?")
                val context = LocalContext.current
                Button(onClick = {
                    // share with a friend


                }) {
                    Text(text = "Share with a friend")
                }
                Button(onClick = {

                }) {
                    Text(text = "Publish a post")
                }
            }
        }
        composeTestRule.onNodeWithText("Share with a friend or publish a post?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Share with a friend").assertIsDisplayed()
        composeTestRule.onNodeWithText("Publish a post").assertIsDisplayed()

        composeTestRule.onNodeWithText("Share with a friend").performClick()


    }
}