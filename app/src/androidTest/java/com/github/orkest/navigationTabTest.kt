package com.github.orkest

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.github.orkest.data.Comment
import com.github.orkest.ui.MainActivity
import com.github.orkest.ui.NavigationBar
import com.github.orkest.ui.feed.CommentActivity
import org.junit.Rule
import org.junit.Test

class navigationTabTest {

    @get:Rule
    val composeTestRule =  createComposeRule()
    @Test
    fun showComment(){

        composeTestRule.setContent {
            CommentActivity.CommentBox(comment = Comment("BOB") )
        }
    }

    /*@Test
    fun canAccessEveryNavigationTabFromMainActivity(){
        composeTestRule.setContent {
            NavigationBar.CreateNavigationBar(navController = rememberNavController(),"", MainActivity())
        }

        val searchTabAccess = composeTestRule.onNodeWithText("Search")
        searchTabAccess.assertIsDisplayed()
        searchTabAccess.performClick()


        val profileTab = composeTestRule.onNodeWithText("Profile")
        profileTab.assertIsDisplayed()
        profileTab.assertHasClickAction()

        val feedTab = composeTestRule.onNodeWithText("Feed")
        feedTab.assertIsDisplayed()
        feedTab.performClick()


        val playlistTab = composeTestRule.onNodeWithText("Playlist")
        playlistTab.assertIsDisplayed()
        playlistTab.performClick()

        val shazamTab = composeTestRule.onNodeWithText("Shazam")
        shazamTab.assertIsDisplayed()
        shazamTab.assertHasClickAction()



    }*/
}