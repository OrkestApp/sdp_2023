package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.github.orkest.View.EditProfileActivity
import com.github.orkest.View.EditProfileScreen
import com.github.orkest.ViewModel.auth.AuthViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CompletableFuture

class EditProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: AuthViewModel
    @Before
    fun setup(){
        composeTestRule.setContent {
            EditProfileScreen(EditProfileActivity())
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


    /**
     * Tests if the user is correctly added to the database
     */
    @Test
    fun testUpdateUserProfile() {

        val testBio = "This is my bio"
        val testNbFollowers = 10
        val testNbFollowings = 34
        val testProfilePicture = 45


        val firestoreDatabaseAPI = FireStoreDatabaseAPI()

        val username = "testUser"
        val updatedData = mapOf("profile.bio" to "banana")

        // Create a user

        val user = User(
            username = username,
            profile = Profile(
                username,
                testProfilePicture,
                testBio,
                testNbFollowers,
                testNbFollowings
            )
        )


        val addUserFuture = firestoreDatabaseAPI.addUserInDatabase(user)
        addUserFuture.join()

        // Update user profile
        firestoreDatabaseAPI.updateUserProfile(username, updatedData)

        // Fetch updated user profile
        val getUserProfileFuture: CompletableFuture<User> =
            firestoreDatabaseAPI.searchUserInDatabase(username)
        val updatedUser = getUserProfileFuture.get()

        // Check if the user profile has been updated with new data
        Assert.assertEquals("banana", updatedUser.profile.bio)
    }

}