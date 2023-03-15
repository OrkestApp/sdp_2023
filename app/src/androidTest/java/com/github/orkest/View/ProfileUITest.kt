package com.github.orkest.View

import androidx.compose.ui.test.assertIsDisplayed
import com.github.orkest.ui.theme.OrkestTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.Model.Profile
import com.github.orkest.R
import com.github.orkest.View.profile.topProfile
import com.github.orkest.ViewModel.MockProfileViewModel
import org.junit.runner.RunWith
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class ProfileUITest {

    private var viewModel: MockProfileViewModel = MockProfileViewModel("JohnSmith")

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        val John = Profile(
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

        composeTestRule.onNodeWithText("JohnSmith").assertIsDisplayed()
        composeTestRule.onNodeWithText("I like everything").assertIsDisplayed()
        composeTestRule.onNodeWithText("10\nfollowers").assertIsDisplayed()
        composeTestRule.onNodeWithText("2\nfollowings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("${R.drawable.profile_picture}").assertIsDisplayed()
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
    }

}