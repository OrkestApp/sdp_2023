package com.github.orkest.View.auth

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.github.orkest.data.Providers
import com.github.orkest.ui.MainActivity
import com.github.orkest.ViewModel.auth.MockAuthViewModel
import com.github.orkest.ui.authentication.SignUpForm
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpFormTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: MockAuthViewModel
    @Before
    fun setup(){
        // Start the app
        viewModel = MockAuthViewModel()
        composeTestRule.setContent {
            SignUpForm( viewModel)
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun componentsDisplayOnScreen(){
        composeTestRule.onAllNodesWithText("Create Your Profile").assertAll(isEnabled())
        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Spotify").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create Profile").assertIsDisplayed()

    }

    @Test
    fun buttonsCanBeClicked(){
        composeTestRule.onNodeWithText("Username").assert(hasClickAction())
        composeTestRule.onNodeWithText("Profile Description").assert(hasClickAction())
        composeTestRule.onNodeWithText("Spotify").assert(hasClickAction())
        composeTestRule.onNodeWithText("Create Profile").assert(hasClickAction())

    }

    @Test
    fun textComponentHasNoClick(){
        composeTestRule.onAllNodesWithText("Create Your Profile").assertAll(hasNoClickAction())
    }

    @Test
    fun inputFieldsUpdateViewAndViewModelWhenInput(){
        composeTestRule.waitUntil{
            composeTestRule.onAllNodesWithText("Username").fetchSemanticsNodes().size ==1
        }
        composeTestRule.onNodeWithText("Username").performTextInput("Steve")
        composeTestRule.onNodeWithText("Steve").assertIsDisplayed()
        //his has to be put after we check the view to be sure we didn't check this info too soon
        assert(viewModel.getUsername().text == "Steve")
        composeTestRule.waitUntil{
            composeTestRule.onAllNodesWithText("Profile Description").fetchSemanticsNodes().size ==1
        }
        composeTestRule.onNodeWithText("Profile Description").performTextInput("bio")
        composeTestRule.onNodeWithText("bio").assertIsDisplayed()
        assert(viewModel.getBio().text == "bio")
    }

    @Test
    fun dropDownMenuDisplaysOnClick(){
        composeTestRule.onNodeWithTag("providerButton").performClick()

        composeTestRule.onNodeWithTag("option Spotify").assertExists()
        composeTestRule.onNodeWithTag("option Deezer").assertExists()
        composeTestRule.onNodeWithTag("option Apple Music").assertExists()

        composeTestRule.onNodeWithTag("option Spotify").assert(hasClickAction())
        composeTestRule.onNodeWithTag("option Deezer").assert(hasClickAction())
        composeTestRule.onNodeWithTag("option Apple Music").assert(hasClickAction())
    }

    @Test
    fun dropDownMenuHidesOnClick(){
        composeTestRule.onNodeWithTag("providerButton").performClick()
        composeTestRule.onNodeWithTag("option Deezer").performClick()

        composeTestRule.onNodeWithTag("option Spotify").assertDoesNotExist()
        composeTestRule.onNodeWithTag("option Deezer").assertDoesNotExist()
        composeTestRule.onNodeWithTag("option Apple Music").assertDoesNotExist()

    }

    @Test
    fun clickOnDropDownUpdatesViewAndViewModel(){
        composeTestRule.onNodeWithText("Spotify").performClick()

        composeTestRule.onNodeWithText("Deezer").performClick()
        composeTestRule.onNodeWithText("Deezer").assertIsDisplayed()
        assert(viewModel.getProvider() == Providers.DEEZER)

        composeTestRule.onNodeWithText("Deezer").performClick()

        composeTestRule.onNodeWithText("Apple Music").performClick()
        composeTestRule.onNodeWithText("Apple Music").assertIsDisplayed()
        assert(viewModel.getProvider() == Providers.APPLE_MUSIC)


        composeTestRule.onNodeWithText("Apple Music").performClick()

        composeTestRule.onNodeWithText("Spotify").performClick()
        composeTestRule.onNodeWithText("Spotify").assertIsDisplayed()
        assert(viewModel.getProvider() == Providers.SPOTIFY)
    }

    @Test
    fun alreadyExistingUserNameDisplaysError(){
        composeTestRule.onNodeWithText("Username")
            .performTextInput(MockAuthViewModel.EXISTING_USER)

        composeTestRule.onNodeWithText("Create Profile").performClick()

        composeTestRule.onNodeWithText("This username already exists!").assertIsDisplayed()
    }

    @Test
    fun validUsernameLaunchesMain(){
        Intents.init()
        composeTestRule.onNodeWithText("Username")
            .performTextInput(MockAuthViewModel.VALID_USER)

        composeTestRule.onNodeWithText("Create Profile").performClick()
        intended((hasComponent(MainActivity::class.java.name)))
        Intents.release()
    }

    @Test
    fun emptyUsernameDisplaysError(){
        composeTestRule.onNodeWithText("Username")
            .performTextInput("")

        composeTestRule.onNodeWithText("Create Profile").performClick()

        composeTestRule.onNodeWithText("Username cannot be empty!").assertIsDisplayed()
    }

    @Test
    fun noConnectionDisplaysError(){
        composeTestRule.onNodeWithText("Username")
            .performTextInput(MockAuthViewModel.NO_CONNECTION)

        composeTestRule.onNodeWithText("Create Profile").performClick()

        composeTestRule.onNodeWithText("Sorry, something went wrong ... " +
                "Please check your Internet connection").assertIsDisplayed()
    }

}