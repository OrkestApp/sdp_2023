package com.github.orkest

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.github.orkest.Model.Providers
import com.github.orkest.View.EditProfileActivity
import com.github.orkest.View.EditProfileScreen
import com.github.orkest.View.auth.SignUpForm
import com.github.orkest.ViewModel.auth.AuthViewModel
import com.github.orkest.ViewModel.auth.MockAuthViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: AuthViewModel
    @Before
    fun setup(){
        composeTestRule.setContent {
            EditProfileScreen(MockAuthViewModel())
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


    @Test
    fun navigationDrawerComponentsDisplayOnScreen() {
        val navDrawer = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)
        composeTestRule.onNode(navDrawer).performClick()
        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("Privacy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Help").assertIsDisplayed()
    }

}