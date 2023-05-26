package com.github.orkest.View

import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.github.orkest.bluetooth.data.BluetoothServiceManager
import com.github.orkest.bluetooth.domain.BluetoothConstants
import com.github.orkest.bluetooth.ui.BluetoothActivity
import com.github.orkest.data.Constants
import com.github.orkest.ui.profile.ProfileViewModel
import org.junit.Rule
import org.junit.Test

class bluetoothUI {

    @get:Rule
    var composeTestRule =  createComposeRule()
    @Test
    fun btUiTest(){
        composeTestRule.setContent {
            BluetoothActivity(true).toastinette()
        }

    }
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

                BluetoothConstants.MESSAGE_WRITE -> {
                    // construct a string from the buffer

                }
                BluetoothConstants.MESSAGE_READ -> {
                    // construct a string from the valid bytes in the buffer
                    val msgReceived = String(msg.obj as ByteArray, 0, msg.arg1)
                    val follow = ProfileViewModel(Constants.APPLICATION_CONTEXT, msgReceived)
                    follow.updateCurrentUserFollowings(true)
                    follow.updateUserFollowers(true)

                }
                BluetoothConstants.MESSAGE_TOAST -> {
                    val msgReceived= msg.data.getString("toast").toString()
                }
            }
        }
    }
    @Test
    fun ShowUser(){
        composeTestRule.setContent {
            BluetoothActivity(true).BluetoothActivityStart(BluetoothServiceManager(handler),
                BluetoothActivity(true)
            )
        }

        composeTestRule.onNodeWithText("RECEIVE").assertIsDisplayed()

    }
}