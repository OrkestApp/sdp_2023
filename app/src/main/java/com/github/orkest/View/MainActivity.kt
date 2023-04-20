package com.github.orkest.View

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.github.orkest.Constants
import com.github.orkest.View.notification.Notification

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(Constants.CURRENT_LOGGED_USER == ""){
                Log.e(TAG, "currentLoggedUser is empty", IllegalArgumentException())
            }
            NavigationBar.CreateNavigationBar(navController = rememberNavController(), Constants.CURRENT_LOGGED_USER)
        }

        Notification().createNotificationChannel(this)
        Notification().promptUserToEnableNotifications(this)
    }



}


