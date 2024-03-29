package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.ui.EditProfileActivity
import com.github.orkest.ui.EditProfileScreen
import com.github.orkest.ui.authentication.AuthViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){
        composeTestRule.setContent {
            EditProfileScreen(EditProfileActivity())
        }
    }

    @Test
    fun componentsDisplayOnScreen(){
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("edit picture").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bio:").assertIsDisplayed()
    }

    @Test
    fun cancelIsClickable() {
        composeTestRule.onNodeWithText("Cancel").assertHasClickAction()
    }

    @Test
    fun saveIsClickable() {
        composeTestRule.onNodeWithText("Save").assertHasClickAction()
    }
}