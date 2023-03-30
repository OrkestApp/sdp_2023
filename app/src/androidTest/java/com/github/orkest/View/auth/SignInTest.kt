package com.github.orkest.View.auth

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.github.orkest.View.MainActivity
import com.github.orkest.ViewModel.auth.MockAuthViewModel
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
            SignIn(navController = rememberNavController(), MockAuthViewModel())
        }
    }

    /**
     * Tests that the sign up button is displayed.
     */
    @Test
    fun testSignInButtonIsDisplayed() {
        composeTestRule.onNodeWithText("Sign up with Google", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    /**
     * Tests that the sign up button can be clicked.
     */
    @Test
    fun testSignUpButtonCanBeClicked() {
        composeTestRule.onNodeWithText("Sign up with Google", useUnmergedTree = true)
            .performClick()
    }

    /**
     * Check if logo is present
     */
    @Test
    fun testLogoPresent() {
        composeTestRule.onNodeWithTag("logo", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    /**
     * Check that the TextField for the username is present
     */
    @Test
    fun testUsernameTextFieldPresent() {
        composeTestRule.onNodeWithText("Username", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    /**
     * Check that the TextField can be clicked
     */
    @Test
    fun testUsernameTextFieldCanBeClicked() {
        composeTestRule.onNodeWithText("Username", useUnmergedTree = true)
            .performClick()
    }

    /**
     * Check that the Sign in button is present
     */
    @Test
    fun testSignInButtonPresent() {
        composeTestRule.onNodeWithText("Sign in with Google", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    /**
     * Check that the Sign in button can be clicked
     */
    @Test
    fun testSignInButtonCanBeClicked() {
        composeTestRule.onNodeWithText("Sign in with Google", useUnmergedTree = true)
            .performClick()
    }







    /////////////
    /*
    @Test
    fun existingUsernameLaunchesMain(){
        Intents.init()
        composeTestRule.onNodeWithText("Current Username")
            .performTextInput(MockAuthViewModel.EXISTING_USER)

        composeTestRule.onNodeWithText("Sign In").performClick()
        Intents.intended((IntentMatchers.hasComponent(MainActivity::class.java.name)))
        Intents.release()
    }

    @Test
    fun noPermissionDisplaysError(){
        composeTestRule.onNodeWithText("Current Username")
            .performTextInput(MockAuthViewModel.NO_PERMISSIONS)

        composeTestRule.onNodeWithText("Sign In").performClick()

        composeTestRule.onNodeWithText("No permissions for this user!").assertIsDisplayed()
    }*/

}