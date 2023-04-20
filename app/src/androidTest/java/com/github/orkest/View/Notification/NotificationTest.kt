package com.github.orkest.View.Notification

import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.View.notification.AlertDialogListener
import com.github.orkest.View.notification.Notification
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NotificationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: android.content.Context

    private var alertDialogShown = false

    private val alertDialogListener = object : AlertDialogListener {
        override fun onAlertDialogShown(alertDialog: AlertDialog) {
            alertDialogShown = true
        }
    }

    private val notification = Notification(context, alertDialogListener)

    /**
     * Check if the alert dialog is shown when the notifications are not enabled
     */
    @Test
    fun alertDialog_isShown_whenNotificationsNotEnabled() {
        notification.promptUserToEnableNotifications()
        assertTrue(alertDialogShown)
    }
}
