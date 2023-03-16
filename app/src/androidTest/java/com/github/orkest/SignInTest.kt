package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.github.orkest.View.auth.SignIn
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for the sign in screen.
 * Because of Google's API and the absence of caching on the emulators,
 * correct and incorrect input of credentials was manually tested
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

    /**
     * Check if logo present
     */
    @Test
    fun testLogoPresent() {
        composeTestRule.onNodeWithTag("logo", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}