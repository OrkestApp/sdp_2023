package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.ui.EditProfileActivity
import com.github.orkest.ui.EditProfileScreen
import com.github.orkest.ui.authentication.AuthViewModel
import com.github.orkest.ui.profile.EditProfileViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){
        composeTestRule.setContent {
            val viewModel = EditProfileViewModel()
            viewModel.setProfilePic(byteArrayOf(1,1,1,1,1))
            viewModel.setBio("two chains")
            EditProfileScreen(EditProfileActivity(), viewModel)
        }
    }

    @Test
    fun componentsDisplayOnScreen(){
        //composeTestRule.onAllNodesWithText("Create Your Profile").assertAll(isEnabled())
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("edit picture").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bio:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Username:").assertIsDisplayed()
    }

}