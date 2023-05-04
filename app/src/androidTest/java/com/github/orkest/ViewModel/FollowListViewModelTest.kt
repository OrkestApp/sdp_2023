package com.github.orkest.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.Observer
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.data.Profile
import com.github.orkest.data.User
import org.junit.Before
import org.junit.Rule
import com.github.orkest.R
import com.github.orkest.ui.FollowListViewModel
import junit.framework.TestCase.*
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
    var composeTestRule = createComposeRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dbAPI.addUserInDatabase(user1).get()
        dbAPI.addUserInDatabase(user2).get()
        dbAPI.addUserInDatabase(user3).get()
    }

    @Test
    fun testRetrieveFollowingsList() {
        viewModel = FollowListViewModel(user1.username, false)

        val followListLiveData = viewModel.retrieveFollowList()
        val followListObserver = Observer<MutableList<User>> { followList ->
            assertNotNull(followList)
            assertEquals(2, followList.size)
            assertTrue(followList[0].username == "user2")
            assertTrue(followList[1].username == "user3")
        }
        //Observes the list to finish fetching all the users from database and then apply the tests
        followListLiveData.observeForever(followListObserver)

        // Wait for LiveData to be updated
        Thread.sleep(1000)

        // Remove the observer
        followListLiveData.removeObserver(followListObserver)
    }

    @Test
    fun testRetrieveFollowersList() {
        val viewModel = FollowListViewModel(user1.username, true)

        val followListLiveData = viewModel.retrieveFollowList()
        val followListObserver = Observer<MutableList<User>> { followList ->
            assertNotNull(followList)
            assertEquals(1, followList.size)
            assertTrue(followList[0].username == "user3")
        }
        //Observes the list to finish fetching all the users from database and then apply the tests
        followListLiveData.observeForever(followListObserver)

        // Wait for LiveData to be updated
        Thread.sleep(1000)

        // Remove the observer
        followListLiveData.removeObserver(followListObserver)
    }
}