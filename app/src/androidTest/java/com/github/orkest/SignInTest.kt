package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.orkest.ui.signin.SignIn
import org.junit.Rule
import org.junit.Test

/**
 * Tests for the sign in screen.
 */
class SignInTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignIn>()

    /**
     * Tests that the sign in button is displayed.
     */
    @Test
    fun testSignInButtonIsDisplayed() {
        composeTestRule.onNodeWithTag("Sign in with Google", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    /**
     * Tests that the sign in button can be clicked.
     */
    @Test
    fun testSignInButtonCanBeClicked() {
        composeTestRule.onNodeWithTag("Sign in with Google", useUnmergedTree = true)
            .performClick()
    }
}