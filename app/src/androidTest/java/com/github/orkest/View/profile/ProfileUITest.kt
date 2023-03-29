package com.github.orkest.View.profile


import androidx.compose.ui.test.assertIsDisplayed
import com.github.orkest.ui.theme.OrkestTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.Constants
import com.github.orkest.Model.Profile
import com.github.orkest.R
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
        Constants.currentLoggedUser = "JohnSmith"

        John = Profile("JohnSmith", R.drawable.profile_picture, "I like everything", 10, 2)
        viewModel.loadData(John.username, John.bio, John.nbFollowers, John.nbFollowings, John.profilePictureId)

        val newUser = Profile("newUser", R.drawable.profile_picture,"",1,1)
        newViewModel.loadData(newUser.username,newUser.bio,newUser.nbFollowers,newUser.nbFollowings,newUser.profilePictureId)
    }

    @Test
    fun profileScreen_displaysRightValues() {
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = viewModel) } }
        composeTestRule.onNodeWithText(John.username).assertIsDisplayed()
        composeTestRule.onNodeWithText(John.bio).assertIsDisplayed()
        composeTestRule.onNodeWithText("${John.nbFollowers}\nfollowers").assertIsDisplayed()
        composeTestRule.onNodeWithText("${John.nbFollowings}\nfollowings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("${R.drawable.profile_picture}").assertIsDisplayed()
    }

    @Test
    fun profileScreen_updatesWhenValuesChangedInDatabase() {
        composeTestRule.setContent {
            OrkestTheme { topProfile(viewModel = viewModel) }
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
    }

    @Test
    fun loadData_withNullProfilePictureId() {
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = viewModel) } }
        viewModel.setProfilePictureId(null)
        composeTestRule.onNodeWithContentDescription("${R.drawable.profile_picture}").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullNbFollowers(){
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = viewModel) } }
        viewModel.setNbFollowers(null)
        composeTestRule.onNodeWithText("${0}\nfollower").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullNbFollowings(){
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = viewModel) } }
        viewModel.setNbFollowings(null)
        composeTestRule.onNodeWithText("${0}\nfollowing").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullDescription(){
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = viewModel) } }
        viewModel.setBio(null)
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun editButton_isDisplayed_when_Current_Profile_Displayed(){
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = viewModel) } }
        composeTestRule.onNodeWithText("Edit Profile").assertIsDisplayed()
    }

    @Test
    fun followButton_click_updates_to_unfollow() {
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = newViewModel) } }
        newViewModel.setIsUserFollowed(false)
        val button = composeTestRule.onNodeWithText("Follow")

        button.performClick()
        composeTestRule.onNodeWithText("Unfollow").assertIsDisplayed()
    }

    @Test
    fun unfollowButton_click_updates_to_follow() {
        composeTestRule.setContent { OrkestTheme { topProfile(viewModel = newViewModel) } }
        newViewModel.setIsUserFollowed(true)
        val button = composeTestRule.onNodeWithText("Unfollow")

        button.performClick()
        composeTestRule.onNodeWithText("Follow").assertIsDisplayed()
    }


}