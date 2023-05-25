package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.ui.feed.CapturedMedia
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatePostVideoFalse {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCapturedMedia_isVideoFalse_displaysImage() {
        composeTestRule.setContent {
            CapturedMedia(null, false)
        }
    }
}
