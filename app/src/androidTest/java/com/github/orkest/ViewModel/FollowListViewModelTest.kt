package com.github.orkest.ViewModel

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import org.junit.Before
import org.junit.Rule
import com.github.orkest.R
import com.github.orkest.View.FollowListActivity
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FollowListViewModelTest {

    private lateinit var viewModel: FollowListViewModel
    val dbAPI = FireStoreDatabaseAPI()

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

        dbAPI.addUserInDatabase(user1).get()
        dbAPI.addUserInDatabase(user2).get()
        dbAPI.addUserInDatabase(user3).get()
    }

    /**@Test
    fun followersUsernameListIsFetchedCorrectly(){
        viewModel = FollowListViewModel(username = user1.username, isFollowersList = true)
        dbAPI.fetchFollowList(user1.username, true).thenApply{
            assertEquals(user1.followers, it)
        }


    }**/

    /**
    @Test
    fun followersUserListIsFetchedCorrectly(){
        viewModel = FollowListViewModel(username = user1.username, isFollowersList = true)
        assertEquals(user1.followers, viewModel.retrieveFollowList()) // on compare des users !!!
    }**/


}