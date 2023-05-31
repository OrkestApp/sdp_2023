package com.github.orkest.View

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.data.Constants
import com.github.orkest.data.User
import com.github.orkest.ui.FollowList
import com.github.orkest.ui.profile.ProfileActivity
import com.github.orkest.ui.FollowListActivity
import com.github.orkest.ui.FollowListViewModel
import com.github.orkest.ui.theme.OrkestTheme
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
class MockFollowListViewModel(private val userList: MutableList<User>): FollowListViewModel("Jerome", false) {

    override fun retrieveFollowList(): LiveData<MutableList<User>> {
        val userListLiveData = MutableLiveData<MutableList<User>>()
        userListLiveData.postValue(userList)
        return userListLiveData
    }

}

@RunWith(AndroidJUnit4::class)
class FollowListActivityTest{

    private lateinit var intent: Intent
    private val viewModel: MockFollowListViewModel = MockFollowListViewModel(mutableListOf(User("Jerome")))

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp(){
        intent = Intent(ApplicationProvider.getApplicationContext(), FollowListActivity::class.java)
        intent.putExtra("username", "Philippe")

    }

    @Test
    fun backButton_navigatesBack() {
        composeTestRule.setContent {
            OrkestTheme { FollowList(ProfileActivity(Constants.APPLICATION_CONTEXT), viewModel = viewModel) }
        }
        val activityScenario = launchActivity<FollowListActivity>(intent)
        activityScenario.moveToState(Lifecycle.State.RESUMED)
        Intents.init()
        composeTestRule.onNodeWithContentDescription("Back button").performClick()
        // Verify that the FollowListActivity is closed
        assertTrue(activityScenario.state.isAtLeast(Lifecycle.State.DESTROYED))
        Intents.release()
        activityScenario.close()
    }

    @Test
    fun profileRowNavigatesToUserProfile(){
        composeTestRule.setContent {
            OrkestTheme { FollowList(ProfileActivity(Constants.APPLICATION_CONTEXT), viewModel = viewModel) }
        }
        Intents.init()
        composeTestRule.onNodeWithTag("Profile Row", useUnmergedTree = true).performClick()
        // Check if the Profile Activity has been launched
        intended(hasComponent(ProfileActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun profilePictureIsDisplayed(){
        composeTestRule.setContent {
            OrkestTheme { FollowList(ProfileActivity(Constants.APPLICATION_CONTEXT), viewModel = viewModel) }
        }
        composeTestRule.onNodeWithContentDescription("Contact profile picture").assertIsDisplayed()
    }

    @Test
    fun usernameIsDisplayed(){
        composeTestRule.setContent {
            OrkestTheme { FollowList(ProfileActivity(Constants.APPLICATION_CONTEXT), viewModel = viewModel) }
        }
        composeTestRule.onNodeWithTag("Username", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun displayFollowersList() {
        composeTestRule.setContent {
            OrkestTheme { FollowList(ProfileActivity(Constants.APPLICATION_CONTEXT), viewModel = viewModel) }
        }
        // Launch the activity with the followers parameter
        intent.putExtra("isFollowers", true)
        val scenario = launchActivity<FollowListActivity>(intent)

        // Check that the "Followers" text is displayed
        composeTestRule.onNodeWithText("Followers").assertExists()
        scenario.close()
    }

    @Test
    fun displayFollowingsList() {
        composeTestRule.setContent {
            OrkestTheme { FollowList(ProfileActivity(Constants.APPLICATION_CONTEXT), viewModel = viewModel) }
        }
        // Launch the activity with the followings parameter
        intent.putExtra("isFollowers", false)
        val scenario = launchActivity<FollowListActivity>(intent)

        // Check that the "Followings" text is displayed
        composeTestRule.onNodeWithText("Followings").assertExists()
        scenario.close()
    }
}
