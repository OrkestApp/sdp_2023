package com.github.orkest.View

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import android.graphics.Color

private const val NOTIFICATION_CHANNEL_ID = "your_channel_id"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val extras = intent.extras
            var user = ""
            if (extras != null){
                user = extras.getString("username").toString()
            }
            NavigationBar.CreateNavigationBar(navController = rememberNavController(), user)
        }

        createNotificationChannel(this)
        promptUserToEnableNotifications(this)
    }

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
     * Build the notification of enabling them
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

            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                    setTextColor(Color.BLUE)
                    setOnClickListener {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                        }
                        activity.startActivity(intent)
                        alertDialog.dismiss()
                    }
                }
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                    setTextColor(Color.RED)
                }
            }

            alertDialog.show()
        } else {
            // Notifications are already enabled
        }
    }

}


