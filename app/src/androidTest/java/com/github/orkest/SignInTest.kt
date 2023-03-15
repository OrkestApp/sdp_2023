package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.github.orkest.View.auth.SignIn
import com.github.orkest.View.auth.SignUpForm
import com.github.orkest.ViewModel.auth.AuthViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for the sign in screen.
 */
class SignInTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    @Before
    fun setup(){
        // Start the app
        composeTestRule.setContent {
            SignIn(navController = rememberNavController())
        }
    }

    /**
     * Tests that the sign in button is displayed.
     */
    @Test
    fun testSignInButtonIsDisplayed() {
        composeTestRule.onNodeWithText("Sign in with Google", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    /**
     * Tests that the sign in button can be clicked.
     */
    @Test
    fun testSignInButtonCanBeClicked() {
        composeTestRule.onNodeWithText("Sign in with Google", useUnmergedTree = true)
            .performClick()
    }
}