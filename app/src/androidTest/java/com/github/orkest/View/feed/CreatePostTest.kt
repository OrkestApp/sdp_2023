package com.github.orkest.View.feed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.ViewModel.feed.MockPostViewModel
import com.github.orkest.data.Song
import com.github.orkest.ui.feed.CreatePost
import com.github.orkest.ui.feed.EditPostScreen
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CreatePostTest{

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var viewModel: MockPostViewModel

    @Before
    fun setup(){
        // Start the app
        viewModel = MockPostViewModel()
        viewModel.updateSong(Song("TestTitle","TestArtist"))
        composeTestRule.setContent {
            EditPostScreen(viewModel = viewModel, activity = CreatePost())
        }
    }

    @Test
    fun clickCreatePostFinishesActivity(){

        //TODO: Find a way to test this
        val node = composeTestRule.onNodeWithText("Publish")
        node.performClick()
        //Checks if the activity is finished
//        Thread.sleep(10000)
//        node.assertDoesNotExist()
    }

    @Test
    fun buttonIsClickable(){
        composeTestRule.onNodeWithText("Publish").assertIsDisplayed()
        composeTestRule.onNodeWithText("Publish").assertHasClickAction()
    }

    @Test
    fun descriptionFieldIsDisplayed(){
        composeTestRule.onNodeWithText("Post Description").assertIsDisplayed()
    }

    @Test
    fun songPostUpdatesBasedOnSong() {
        assertEquals(viewModel.getSong(), Song("TestTitle","TestArtist"))
        composeTestRule.onNodeWithText("TestTitle").assertIsDisplayed()
        composeTestRule.onNodeWithText("TestArtist").assertIsDisplayed()
    }

    //Not necessary to test the other, since this is just a demo activity






}