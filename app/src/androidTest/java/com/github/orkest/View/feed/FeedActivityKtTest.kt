package com.github.orkest.View.feed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.ViewModel.feed.MockPostViewModel
import com.github.orkest.ViewModel.post.PostViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class FeedActivityKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){
        // Start the app
        val viewModel = MockPostViewModel()
        composeTestRule.setContent {
            FeedActivity(viewModel = viewModel)
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
            .assertAll(isEnabled())
        composeTestRule.onAllNodesWithTag("like_button")
            .assertAll(isEnabled())
        composeTestRule.onAllNodesWithTag("share_button")
            .assertAll(isEnabled())
    }


    @Test
    fun reactionButtonsClick() {
        composeTestRule.onAllNodesWithTag("comment_button").assertAll(hasClickAction())
        composeTestRule.onAllNodesWithTag("like_button").assertAll(hasClickAction())
        composeTestRule.onAllNodesWithTag("share_button").assertAll(hasClickAction())
    }

    /*@Test
    fun openedCommentActivityWhenClickOnCommentButton() {
        Intents.init()

        val button = composeTestRule.onNodeWithTag("comment_button")
        button.performClick()
        Intents.intended((IntentMatchers.hasComponent(CommentActivity::class.java.name)))

        Intents.release()
    }*/

}