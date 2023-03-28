package com.github.orkest.View.feed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.github.orkest.View.auth.SignUpForm
import com.github.orkest.ViewModel.auth.MockAuthViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class FeedActivityKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){
        // Start the app
        composeTestRule.setContent {
            FeedActivity()
        }
    }

    @Test
    fun userDisplaysAndClicks(){
        composeTestRule.onAllNodesWithText("Username").assertAll(isEnabled())
            .assertAll(hasClickAction())
        composeTestRule.onAllNodesWithText("Profile Description").assertAll(isEnabled())
            .assertAll(hasClickAction())
    }


    @Test
    fun songCardDisplays() {
        composeTestRule
            .onAllNodesWithContentDescription("Cover of the album of the song Rude Boy by Rihanna")
            .assertAll(isEnabled())

        composeTestRule.onAllNodesWithText("Rude Boy").assertAll(isEnabled())
        composeTestRule.onAllNodesWithText("Rihanna").assertAll(isEnabled())
        composeTestRule.onAllNodesWithText("Rated R").assertAll(isEnabled())
    }

    @Test
    fun playButtonDisplaysAndClick(){
        composeTestRule.onAllNodesWithContentDescription("Play Button")
            .assertAll(isEnabled()).assertAll(hasClickAction())
    }

    @Test
    fun reactionButtonDisplay(){
        composeTestRule.onAllNodesWithTag("comment_button")
            .assertCountEquals(4).assertAll(isEnabled())
        composeTestRule.onAllNodesWithTag("like_button")
            .assertCountEquals(4).assertAll(isEnabled())
        composeTestRule.onAllNodesWithTag("share_button")
            .assertCountEquals(4).assertAll(isEnabled())
    }


    @Test
    fun reactionButtonsClick() {
        composeTestRule.onAllNodesWithTag("comment_button").assertAll(hasClickAction())
        composeTestRule.onAllNodesWithTag("like_button").assertAll(hasClickAction())
        composeTestRule.onAllNodesWithTag("share_button").assertAll(hasClickAction())

    }

}