package com.github.orkest.View.auth

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.github.orkest.ViewModel.auth.MockAuthViewModel
import com.github.orkest.ui.authentication.SignIn
import com.github.orkest.ui.authentication.isSignedInOffline
import com.github.orkest.ui.authentication.loadUserCredentials
import com.github.orkest.ui.authentication.saveUserCredentials
import com.github.orkest.ui.profile.cleanSigningCache
import junit.framework.TestCase.assertEquals
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
    private lateinit var context: Context
    @Before
    fun setup(){
        // Start the app
        composeTestRule.setContent {
            SignIn(navController = rememberNavController(), MockAuthViewModel())
        }
        context = ApplicationProvider.getApplicationContext()
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

    /**
     * Check that offline signing in correctly stores and loads the credentials
     */
    @Test
    fun offlineSignIn_correctlyStores() {

        val context: Context = ApplicationProvider.getApplicationContext()

        // Mock credentials
        val username = "test"
        val email = "test@test.ch"

        // Save and load user credentials
        saveUserCredentials(context, username, email)
        val (savedUsername, savedEmail) = loadUserCredentials(context)

        // Check if the provided user credentials do not match the saved ones
        assertEquals(username, savedUsername)
        assertEquals(email, savedEmail)

        // Clear SharedPreferences after each test
        val sharedPref = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

    /**
     * Checks if credentials are present in SharedPreferences
     */
    @Test
    fun correctlyChecksOffline() {
        val context: Context = ApplicationProvider.getApplicationContext()

        // Mock credentials
        val username = "test"
        val email = "test@test.ch"

        // Save user credentials
        saveUserCredentials(context, username, email)

        //check offline
        assertEquals(true, isSignedInOffline(context))

        // Clear SharedPreferences after each test
        val sharedPref = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

    /**
     * Correctly cleans the elements in SharedPreferences
     */
    @Test
    fun correctlyCleansCache(){
        val context: Context = ApplicationProvider.getApplicationContext()

        // Mock credentials
        val username = "test"
        val email = "test@test.ch"

        // Save user credentials
        saveUserCredentials(context, username, email)

        // Clear user credentials
        cleanSigningCache(context)

        // Load the new values
        val (savedUsername, savedEmail) = loadUserCredentials(context)

        // Check if the provided user credentials are empty
        assertEquals(null, savedUsername)
        assertEquals(null, savedEmail)

        // Clear SharedPreferences after each test
        val sharedPref = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

}