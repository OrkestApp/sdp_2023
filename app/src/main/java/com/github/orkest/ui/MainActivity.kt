package com.github.orkest.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.github.orkest.data.Constants
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.data.PlaySpotify
import com.github.orkest.data.Providers
import com.github.orkest.ui.notification.Notification


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(Constants.CURRENT_LOGGED_USER == ""){
                Log.e(TAG, "currentLoggedUser is empty", IllegalArgumentException())
            }
            NavigationBar.CreateNavigationBar(
                navController = rememberNavController(),
                Constants.CURRENT_LOGGED_USER
            )
        }

        Notification(this, null).createNotificationChannel()
        Notification(this, null).promptUserToEnableNotifications(this)
    }
    override fun onStart() {
        super.onStart()
        FireStoreDatabaseAPI().searchUserInDatabase(Constants.CURRENT_LOGGED_USER).whenComplete() { user, _ ->
            if (user != null) {
                Constants.CURRENT_USER_PROVIDER = Providers.valueOf(user.serviceProvider)
            }
        }
        if (Constants.CURRENT_USER_PROVIDER == Providers.SPOTIFY)
             PlaySpotify.setupSpotifyAppRemote(this)
    }


}
