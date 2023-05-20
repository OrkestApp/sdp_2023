package com.github.orkest.View.feed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.data.Comment
import com.github.orkest.data.Constants
import com.github.orkest.data.OrkestDate
import com.github.orkest.ui.feed.CommentBox
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class CommentBoxTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCommentBox() {
        val comment = Comment("testUser", OrkestDate(LocalDateTime.now(Constants.DB_ZONE_ID)), "testText")

        composeTestRule.setContent {
            CommentBox(comment)
        }

        composeTestRule.onNodeWithTag("comment_username").assert(hasText(comment.username))
        composeTestRule.onNodeWithTag("comment_text").assert(hasText(comment.text))
        composeTestRule.onNodeWithTag("comment_date").assert(hasText(comment.date.toString()))
        //composeTestRule.onNodeWithTag("display_pic").assertIsDisplayed()
    }
}
