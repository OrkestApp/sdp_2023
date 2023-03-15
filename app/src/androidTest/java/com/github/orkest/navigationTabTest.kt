package com.github.orkest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.github.orkest.View.NavigationBar
import org.junit.Rule
import org.junit.Test

class navigationTabTest {

    @get:Rule
    val composeTestRule =  createComposeRule()

    @Test
    fun canAccessTheNavigationTabFromMainActivity(){
        composeTestRule.setContent {
            NavigationBar.CreateNavigationBar(navController = rememberNavController())
        }

        val searchTabAccess = composeTestRule.onNodeWithText("Search")

        searchTabAccess.assertIsDisplayed()

        searchTabAccess.performClick()

        composeTestRule.onNodeWithText("").assertIsDisplayed()
    }
}