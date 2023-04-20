package com.github.orkest.View.feed

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.github.orkest.Constants
import com.github.orkest.Model.OrkestDate
import com.github.orkest.Model.Post
import com.github.orkest.View.auth.SignUpForm
import com.github.orkest.ViewModel.auth.MockAuthViewModel
import com.github.orkest.ViewModel.feed.MockPostViewModel
import com.github.orkest.ViewModel.post.PostViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

internal class CommentActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val viewModel = MockPostViewModel()

    @Before
    fun setup(){
        // Start the app
        Constants.CURRENT_LOGGED_USER = "DummyUser"

        composeTestRule.setContent {

            val context = LocalContext.current
            context.startActivity(
                Intent(context, CommentActivity::class.java)
                    .putExtra("post_date", viewModel.postComm.date.toString())
                    .putExtra("post_username", viewModel.postComm.username)
            )

            CommentActivity()
        }
    }




    @Test
    fun textFieldIsDisplayed() {
        composeTestRule.onNodeWithTag("comment_field").assertIsDisplayed()
    }

    @Test
    fun textFieldIsClickable() {
        composeTestRule.onNodeWithTag("comment_field").assertHasClickAction()
    }

    @Test
    fun textFieldDisplaysUserProfilePic() {
        composeTestRule.onAllNodesWithTag("display_pic").assertAll(isEnabled())
    }

    /*@Test
    fun commentAndItsElementsDisplayed() {
        composeTestRule.onNodeWithTag("comment_username").assertIsDisplayed()
        composeTestRule.onNodeWithTag("comment_date").assertIsDisplayed()
        composeTestRule.onNodeWithTag("comment_text").assertIsDisplayed()
    }*/

}