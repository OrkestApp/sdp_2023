package com.github.orkest.View

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.lifecycle.MutableLiveData
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.ViewModel.profile.ProfileViewModel
import com.github.orkest.ui.theme.OrkestTheme
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.R
import com.github.orkest.View.profile.ProfileTopInterface
import com.github.orkest.View.profile.topProfile
import com.github.orkest.ViewModel.MockProfileViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileUITest {

    //private val db = Firebase.firestore




    private var viewModelJohn: MockProfileViewModel = MockProfileViewModel("JohnDoe")
    private var viewModelRebecca: MockProfileViewModel = MockProfileViewModel("RebeccaSmith")


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_displaysRightValues() {

        composeTestRule.setContent {
            OrkestTheme {
                topProfile(viewModel = viewModelJohn
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hello, I'm John!").assertIsDisplayed()
        composeTestRule.onNodeWithText("100\nfollowers").assertIsDisplayed()
        composeTestRule.onNodeWithText("50\nfollowings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("${viewModelJohn.profilePictureId.value}").assertIsDisplayed()
    }

    @Test
    fun profileScreen_updatesWhenValuesChangedInDatabase() {


        composeTestRule.setContent {
            OrkestTheme {
                topProfile(viewModel = viewModelJohn
                )
            }
        }

        /**
         * CHANGE JOHN'S DATA
         */

        // Then
        composeTestRule.onNodeWithText("Jane Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Data Scientist").assertIsDisplayed()
        composeTestRule.onNodeWithText("200\nfollowers").assertIsDisplayed()
        composeTestRule.onNodeWithText("150\nfollowings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("${viewModelRebecca.profilePictureId.value}").assertIsDisplayed()
    }

}