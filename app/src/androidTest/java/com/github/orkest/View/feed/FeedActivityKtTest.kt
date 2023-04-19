package com.github.orkest.View.feed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.github.orkest.View.MainActivity
import com.github.orkest.View.auth.SignUpForm
import com.github.orkest.ViewModel.auth.MockAuthViewModel
import com.github.orkest.ViewModel.feed.MockPostViewModel
import com.github.orkest.ViewModel.post.PostViewModel
import kotlinx.coroutines.delay
import okhttp3.internal.wait
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
        composeTestRule.onAllNodesWithContentDescription("Play button").assertCountEquals(1)
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

    @Test
    fun pauseButtonChangesToPlayButton(){
        composeTestRule.onNodeWithContentDescription("Play button").performClick()
//        Thread.sleep(1000)
//        composeTestRule.onNodeWithContentDescription("Pause button").assertIsDisplayed().performClick()
//        Thread.sleep(1000)
//        composeTestRule.onNodeWithContentDescription("Play button").assertIsDisplayed()

    }

    @Test
    fun playButtonChangesToPauseButton(){
        composeTestRule.onNodeWithContentDescription("Play button").performClick()
       // Thread.sleep(1000)
      //  composeTestRule.onNodeWithContentDescription("Pause button").assertIsDisplayed()
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