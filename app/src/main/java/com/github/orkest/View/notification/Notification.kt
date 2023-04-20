package com.github.orkest.View.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import com.github.orkest.Constants.Companion.NOTIFICATION_CHANNEL_ID

class Notification {

    /**
     * The notification channel is required for Android 8.0 and above
     * To inform the user to enable the notifications for this app
     */
    fun createNotificationChannel(context: Context) {
        val name = "Your Channel Name"
        val descriptionText = "Your Channel Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Build the notification manager to enable notifications
     * The choice will be saved in the phone's parameters
     */
    fun promptUserToEnableNotifications(activity: Context) {
        val notificationManager = NotificationManagerCompat.from(activity)
        if (!notificationManager.areNotificationsEnabled()) {
            val alertDialog = AlertDialog.Builder(activity)
                .setTitle("Enable Notifications")
                .setMessage("To receive important updates, please enable notifications for this app.")
                .setPositiveButton("Enable", null)
                .setNegativeButton("Cancel", null)
                .create()

            //display of the alert dialog
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                    setTextColor(Color.BLUE) // set the blue color for the enable button
                    setOnClickListener {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                        }
                        activity.startActivity(intent)
                        alertDialog.dismiss()
                    }
                }
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                    setTextColor(Color.RED) // set the red color for the cancel button
                }
            }

            alertDialog.show()
        } else {
            // Notifications are already enabled
        }
    }
}