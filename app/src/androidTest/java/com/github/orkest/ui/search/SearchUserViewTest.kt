package com.github.orkest.ui.search

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.github.orkest.bluetooth.ui.BluetoothActivity
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchUserViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            SearchUserView.SearchUi(viewModel = MockSearchViewModel())
        }
    }

    @Test
    fun UIDisplaysSearchBar() {
        composeTestRule.onNodeWithTag("SearchBar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search a user here").assertIsDisplayed()
        composeTestRule.onNodeWithTag("search_logo").assertIsDisplayed()
    }

    @Test
    fun UiDisplaysOrkestText() {
        //DISPLAY OF Orkest name
        composeTestRule.onNodeWithTag("O").assertIsDisplayed()
        composeTestRule.onNodeWithTag("R").assertIsDisplayed()
        composeTestRule.onNodeWithTag("K").assertIsDisplayed()
        composeTestRule.onNodeWithTag("E").assertIsDisplayed()
        composeTestRule.onNodeWithTag("S").assertIsDisplayed()
        composeTestRule.onNodeWithTag("T").assertIsDisplayed()
    }

    @Test
    fun UiDisplaysOrkestLogo() {
        //DISPLAY OF Orkest logo
        composeTestRule.onNodeWithTag("logoOrkest").assertIsDisplayed()
        composeTestRule.onNodeWithTag("copyright").assertIsDisplayed()
    }

    @Test
    fun bluetoothButtonDisplayed(){
        composeTestRule.onNodeWithTag("bluetoothButton").assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun bluetoothButtonLaunchesIntent() {
        Intents.init()
        composeTestRule.onNodeWithTag("bluetoothButton").performClick()
        Intents.intended(hasComponent(BluetoothActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun titleDisplay() {
        composeTestRule.onNodeWithTag("searchTitle").assertIsDisplayed()
    }


}