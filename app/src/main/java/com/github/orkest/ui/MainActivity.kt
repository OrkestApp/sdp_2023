package com.github.orkest.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.github.orkest.data.Constants
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.data.PlaySpotify
import com.github.orkest.data.Providers
import com.github.orkest.shazam.domain.ShazamConstants
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
                Constants.CURRENT_LOGGED_USER,
                this
            )
        }

        ShazamConstants.askRecordPermission(this)
        Notification(this,null).createNotificationChannel()
        Notification(this,null).promptUserToEnableNotifications()
    }
    override fun onStart() {
        super.onStart()
        FireStoreDatabaseAPI().searchUserInDatabase(Constants.CURRENT_LOGGED_USER).whenComplete() { user, _ ->
            if (user != null) {
                Constants.CURRENT_USER_PROVIDER = Providers.valueOf(user.serviceProvider.uppercase())
                Log.d("Provider", Constants.CURRENT_USER_PROVIDER.toString())

                if (Constants.CURRENT_USER_PROVIDER == Providers.SPOTIFY)
                    PlaySpotify.setupSpotifyAppRemote(this)
            }
        }

    }

    /**
     * Callback for the result from requesting permissions.
     * This method is invoked for every call on [ActivityCompat.requestPermissions]
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("ShazamActivity", "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ShazamConstants.REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "The permission was succesfully granted! You van now shazam songs!",
                    Toast.LENGTH_LONG).show()
            } else {
                // Permission denied
                //Display a text box dialog to inform user that the app needs the permission to work
                Toast.makeText(this, "The app needs the microphone access to analyze " +
                        "which song you are listening to", Toast.LENGTH_LONG).show()
            }
        }
    }
}



