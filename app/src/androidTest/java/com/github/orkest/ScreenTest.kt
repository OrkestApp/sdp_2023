package com.github.orkest

import androidx.compose.material.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.github.orkest.ui.EditProfileSetting
import org.junit.Rule
import org.junit.Test

class ScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testEditProfileSetting() {
        composeTestRule.setContent {
            EditProfileSetting {
                Text(text = "Test Content")
            }
        }

        composeTestRule.onNodeWithText("Test Content").assertExists()
    }
}
