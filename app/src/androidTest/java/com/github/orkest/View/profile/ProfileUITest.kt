package com.github.orkest.View.profile

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.data.Constants
import com.github.orkest.data.Profile
import com.github.orkest.R
import com.github.orkest.View.theme.OrkestTheme
import com.github.orkest.ViewModel.profile.MockProfileViewModel
import org.junit.runner.RunWith
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class ProfileUITest {

    private var viewModel: MockProfileViewModel = MockProfileViewModel("JohnSmith")
    private var newViewModel: MockProfileViewModel = MockProfileViewModel("newUser")
    private  lateinit  var John: Profile

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        Constants.CURRENT_LOGGED_USER = "JohnSmith"

        John = Profile("JohnSmith", R.drawable.profile_picture, "I like everything", 10, 2)
        viewModel.loadData(John.username, John.bio, John.nbFollowers, John.nbFollowings, John.profilePictureId)

        val newUser = Profile("newUser", R.drawable.profile_picture,"",1,1)
        newViewModel.loadData(newUser.username,newUser.bio,newUser.nbFollowers,newUser.nbFollowings,newUser.profilePictureId)
    }

    @Test
    fun profileScreen_displaysRightValues() {
        composeTestRule.setContent {
            ProfileActivitySetting {
                ProfileActivityScreen(ProfileActivity(), viewModel = viewModel)
            }
        }
        composeTestRule.onNodeWithText(John.username).assertIsDisplayed()
        composeTestRule.onNodeWithText(John.bio).assertIsDisplayed()
        composeTestRule.onNodeWithText("${John.nbFollowers}\nfollowers").assertIsDisplayed()
        composeTestRule.onNodeWithText("${John.nbFollowings}\nfollowings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("${R.drawable.profile_picture}").assertIsDisplayed()

        composeTestRule.onNodeWithText("Favorite Songs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Favorite Artists").assertIsDisplayed()
    }

    @Test
    fun navigationDrawerComponentsDisplayOnScreen() {
        composeTestRule.setContent {
            ProfileActivitySetting {
                ProfileActivityScreen(ProfileActivity(), viewModel = viewModel)
            }
        }

        // drawer elements displayed when opening the drawer
        composeTestRule.onNodeWithContentDescription("Drawer Icon").performClick()
        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("Privacy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Help").assertIsDisplayed()

        // elements not displayed when closing the drawer
        composeTestRule.onNodeWithContentDescription("Drawer Icon").performClick()
        composeTestRule.onNodeWithText("Notifications").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Privacy").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Help").assertIsNotDisplayed()
    }

    @Test
    fun addFavoriteSongsAndArtistsButtonsAreDisplayedAndClickable() {
         composeTestRule.setContent {
            ProfileActivitySetting {
                ProfileActivityScreen(ProfileActivity(), viewModel = viewModel)
            }
        }

        val numAddButtons = composeTestRule.onAllNodesWithContentDescription("Add Button").fetchSemanticsNodes().size
        assert(numAddButtons == 2)
        composeTestRule.onAllNodesWithContentDescription("Add Button").assertAll(hasClickAction())
        composeTestRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    @Test
    fun profileScreen_updatesWhenValuesChangedInDatabase() {
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        val newUsername = "Mike"
        val newBio = "New Bio"
        val newNbFollowers = 1000
        val newNbFollowings = 42
        val newProfilePictureId = R.drawable.ic_notifications_black_24dp

        viewModel.loadData(newUsername,newBio,newNbFollowers,newNbFollowings,newProfilePictureId)

        composeTestRule.onNodeWithText(newUsername).assertIsDisplayed()
        composeTestRule.onNodeWithText(newBio).assertIsDisplayed()
        composeTestRule.onNodeWithText("$newNbFollowers\nfollowers").assertIsDisplayed()
        composeTestRule.onNodeWithText("$newNbFollowings\nfollowings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("$newProfilePictureId").assertIsDisplayed()
        //composeTestRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullProfilePictureId() {
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        viewModel.setProfilePictureId(null)
        composeTestRule.onNodeWithContentDescription("${R.drawable.profile_picture}").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullNbFollowers(){
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        viewModel.setNbFollowers(null)
        composeTestRule.onNodeWithText("${0}\nfollower").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullNbFollowings(){
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        viewModel.setNbFollowings(null)
        composeTestRule.onNodeWithText("${0}\nfollowing").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullDescription(){
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        viewModel.setBio(null)
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun editButton_isDisplayed_when_Current_Profile_Displayed(){
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        composeTestRule.onNodeWithText("Edit Profile").assertIsDisplayed()
    }

    @Test
    fun followButton_click_updates_to_unfollow() {
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = newViewModel, scaffoldState, coroutineScope) }
        }
        newViewModel.setIsUserFollowed(false)
        val button = composeTestRule.onNodeWithText("Follow")

        button.performClick()
        composeTestRule.onNodeWithText("Unfollow").assertIsDisplayed()
    }

    @Test
    fun unfollowButton_click_updates_to_follow() {
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = newViewModel, scaffoldState, coroutineScope) }
        }
        newViewModel.setIsUserFollowed(true)
        val button = composeTestRule.onNodeWithText("Unfollow")

        button.performClick()
        composeTestRule.onNodeWithText("Follow").assertIsDisplayed()
    }


    /**
     * The sign out button is displayed
     */
    @Test
    fun signOutButton_displayed() {
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        composeTestRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    /**
     * Clicking on the sign out button redirects to the login screen
     */
    @Test
    fun signOutButton_click() {
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            OrkestTheme { topProfile(viewModel = viewModel, scaffoldState, coroutineScope) }
        }
        composeTestRule.onNodeWithText("Sign Out").performClick()
        composeTestRule.onNodeWithText("Sign in with Google").assertIsDisplayed()
    }

}