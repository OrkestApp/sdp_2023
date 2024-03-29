package com.github.orkest.View.feed

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import com.github.orkest.ViewModel.feed.MockPostViewModel
import com.github.orkest.domain.persistence.AppDatabase
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
            val context = LocalContext.current
            val database = Room.databaseBuilder(context, AppDatabase::class.java, "test-database")
                .build()
            FeedActivity(database, context, viewModel)
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
        composeTestRule.onAllNodesWithContentDescription("Play button")
            .assertAll(isEnabled()).assertAll(hasClickAction())
    }

    @Test
    fun reactionButtonDisplay(){
        composeTestRule.onAllNodesWithTag("comment_button")
            .assertAll(isEnabled())
        composeTestRule.onAllNodesWithTag("share_button")
            .assertAll(isEnabled())
    }


    @Test
    fun reactionButtonsClick() {
        composeTestRule.onAllNodesWithTag("comment_button").assertAll(hasClickAction())
        composeTestRule.onAllNodesWithTag("share_button").assertAll(hasClickAction())
    }

    @Test
    fun likButton(){
        composeTestRule.onAllNodesWithTag("like_button").assertAll(isEnabled())
        composeTestRule.onAllNodesWithTag("like_button").assertAll(hasClickAction())
        composeTestRule.onAllNodesWithTag("Number of likes").assertAll(isEnabled())
    }

    @Test
    fun pauseButtonChangesToPlayButton(){
        //composeTestRule.onNodeWithContentDescription("Play button").performClick()
//        Thread.sleep(1000)
//        composeTestRule.onNodeWithContentDescription("Pause button").assertIsDisplayed().performClick()
//        Thread.sleep(1000)
//        composeTestRule.onNodeWithContentDescription("Play button").assertIsDisplayed()

    }

    @Test
    fun playButtonChangesToPauseButton(){
        composeTestRule.onAllNodesWithContentDescription("Play button").onFirst().performClick()
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