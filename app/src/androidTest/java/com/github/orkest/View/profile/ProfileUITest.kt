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
import com.github.orkest.Model.Profile
import com.github.orkest.R
import com.github.orkest.View.profile.topProfile
import com.github.orkest.ViewModel.profile.MockProfileViewModel
import org.junit.runner.RunWith
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class ProfileUITest {

    private var viewModel: MockProfileViewModel = MockProfileViewModel("JohnSmith")
    private  lateinit  var John: Profile

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        John = Profile(
            "JohnSmith",
            R.drawable.profile_picture,
            "I like everything",
            10,
            2
        )
        viewModel.loadData(
            John.username,
            John.bio,
            John.nbFollowers,
            John.nbFollowings,
            John.profilePictureId
        )

        composeTestRule.setContent {
            OrkestTheme { topProfile(viewModel = viewModel) }
        }
    }


    @Test
    fun profileScreen_displaysRightValues() {

        composeTestRule.onNodeWithText(John.username).assertIsDisplayed()
        composeTestRule.onNodeWithText(John.bio).assertIsDisplayed()
        composeTestRule.onNodeWithText("${John.nbFollowers}\nfollowers").assertIsDisplayed()
        composeTestRule.onNodeWithText("${John.nbFollowings}\nfollowings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("${R.drawable.profile_picture}").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    @Test
    fun profileScreen_updatesWhenValuesChangedInDatabase() {
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
        composeTestRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullProfilePictureId() {
        viewModel.setProfilePictureId(null)
        composeTestRule.onNodeWithContentDescription("${R.drawable.profile_picture}").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullNbFollowers(){
        viewModel.setNbFollowers(null)
        composeTestRule.onNodeWithText("${0}\nfollower").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullNbFollowings(){
        viewModel.setNbFollowings(null)
        composeTestRule.onNodeWithText("${0}\nfollowing").assertIsDisplayed()
    }

    @Test
    fun loadData_withNullDescription(){
        viewModel.setBio(null)
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    /**
     * Clicking on the sign out button redirects to the login screen
     */
    @Test
    fun signOutButton_click() {
        composeTestRule.onNodeWithText("Sign Out").performClick()
        composeTestRule.onNodeWithText("Sign in with Google").assertIsDisplayed()
    }

}