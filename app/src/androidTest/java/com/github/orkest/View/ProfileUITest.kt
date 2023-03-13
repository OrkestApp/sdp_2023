package com.github.orkest.View

import androidx.compose.ui.test.assertIsDisplayed
import androidx.lifecycle.MutableLiveData
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.ViewModel.profile.ProfileViewModel
import com.github.orkest.ui.theme.OrkestTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.R
import com.github.orkest.View.profile.topProfile
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileUITest {


    @get:Rule
    val composeTestRule = createAndroidComposeRule<ProfileActivity>()

    private var viewModel: ProfileViewModel = ProfileViewModel()

    init{
        viewModel.username = MutableLiveData("testUser")
        viewModel.bio = MutableLiveData("testBio")
        viewModel.nbFollowers = MutableLiveData(10)
        viewModel.nbFollowings = MutableLiveData(20)
        viewModel.profilePictureId = MutableLiveData(R.drawable.profile_picture)
    }



    @Test
    fun testProfileScreenDisplayed() {
        composeTestRule.setContent {
            OrkestTheme {
                topProfile(viewModel = viewModel
                )
            }
        }

        // Assert that the username is displayed
        composeTestRule.onNodeWithText("testUser").assertIsDisplayed()

        // Assert that the bio is displayed
        composeTestRule.onNodeWithText("testBio").assertIsDisplayed()

        // Assert that the number of followers is displayed
        composeTestRule.onNodeWithText("10\nfollowers").assertIsDisplayed()

        // Assert that the number of followings is displayed
        composeTestRule.onNodeWithText("20\nfollowers").assertIsDisplayed()

        // Assert that the profile picture is displayed
        composeTestRule.onNodeWithContentDescription("Profile picture").assertIsDisplayed()

        // Assert that the "Edit Profile" button is displayed
        composeTestRule.onNodeWithText("Edit Profile").assertIsDisplayed()
    }

}