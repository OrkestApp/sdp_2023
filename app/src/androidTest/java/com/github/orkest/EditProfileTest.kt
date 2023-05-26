package com.github.orkest

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.graphics.drawable.toBitmap
import com.github.orkest.ui.EditProfileActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayOutputStream


class EditProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){

        composeTestRule.setContent {
            val context = LocalContext.current
            val intent = Intent(context, EditProfileActivity::class.java)

            val d = context.getDrawable(R.drawable.blank_profile_pic)
            val stream = ByteArrayOutputStream()
            d?.toBitmap()?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bitmapdata: ByteArray = stream.toByteArray()

            intent.putExtra("bio", "tqt")
            intent.putExtra("profilePic", bitmapdata)
            context.startActivity(intent)
        }
    }

    @Test
    fun componentsDisplayOnScreen(){
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("edit picture").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bio:").assertIsDisplayed()
    }

    @Test
    fun cancelIsClickable() {
        composeTestRule.onNodeWithText("Cancel").assertHasClickAction()
    }

    @Test
    fun saveIsClickable() {
        composeTestRule.onNodeWithText("Save").assertHasClickAction()
    }
}