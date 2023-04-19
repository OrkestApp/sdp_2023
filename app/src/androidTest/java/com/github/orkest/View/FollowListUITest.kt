package com.github.orkest.View

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.github.orkest.R
import com.github.orkest.ViewModel.FollowListViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FollowListUITest {

    private lateinit var viewModel: FollowListViewModel

    private val user1 = User(
        username = "user1",
        profile = Profile(username = "user1", profilePictureId = R.drawable.profile_picture, nbFollowers = 2, nbFollowings = 1 ),
        followers = mutableListOf("user2", "user3"),
        followings = mutableListOf("user3")
    )
    private val user2 = User(
        username = "user2",
        profile = Profile(username = "user2", profilePictureId = R.drawable.powerrangerblue, nbFollowers = 1, nbFollowings = 0 ),
        followers = mutableListOf("user1"),
        followings = mutableListOf()
    )
    private val user3 = User(
        username = "user3",
        profile = Profile(username = "user3", profilePictureId = R.drawable.blank_profile_pic, nbFollowers = 1, nbFollowings = 1 ),
        followers = mutableListOf("user1"),
        followings = mutableListOf("user1")
    )

    @get:Rule
    var composeTestRule =  createComposeRule()

    @Before
    fun setUp(){
        val dbAPI = FireStoreDatabaseAPI()
        dbAPI.addUserInDatabase(user1).get()
        dbAPI.addUserInDatabase(user2).get()
        dbAPI.addUserInDatabase(user3).get()
    }


    @Test
    fun back_button_navigates_to_profile_activity() {
        composeTestRule.setContent { FollowListActivity() }
        // Click on the back button
        composeTestRule.onNodeWithContentDescription("Back button").performClick()
        // Check that the ProfileActivity is launched
        composeTestRule.onNodeWithText("ProfileActivity").assertExists()
    }

}