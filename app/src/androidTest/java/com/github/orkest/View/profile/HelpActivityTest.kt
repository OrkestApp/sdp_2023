package com.github.orkest.View.profile

import android.content.Intent
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.data.Constants
import com.github.orkest.data.Profile
import com.github.orkest.R
import com.github.orkest.ui.theme.OrkestTheme
import com.github.orkest.ViewModel.profile.MockProfileViewModel
import com.github.orkest.ui.profile.HelpActivity
import org.junit.runner.RunWith
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class HelpActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            val context = LocalContext.current
            context.startActivity(
                Intent(context, HelpActivity::class.java)
            )
        }
    }

    @Test
    fun returnButtonIsClickable() {
        composeTestRule.onNodeWithTag("back_arrow").assertHasClickAction()
    }

    @Test
    fun topBarElementsAreDisplayed() {
        composeTestRule.onNodeWithTag("back_arrow").assertIsDisplayed()
        composeTestRule.onNodeWithTag("info_title").assertIsDisplayed()
    }

    @Test
    fun aboutTextIsDisplayed() {
        composeTestRule.onNodeWithTag("about_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("about_text").assertIsDisplayed()
    }

    @Test
    fun tutorialTextIsDisplayed() {
        composeTestRule.onNodeWithTag("tutorial_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("tutorial_text").assertIsDisplayed()
    }

}