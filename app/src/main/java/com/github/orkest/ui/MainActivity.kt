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
import com.github.orkest.ui.notification.Notification


open class MainActivity : AppCompatActivity() {

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

        PermissionConstants.askRecordPermission(this)
        PermissionConstants.askCameraPermission(this)
        Notification(this,null).createNotificationChannel()
        Notification(this,null).promptUserToEnableNotifications()
    }
    override fun onStart() {

        super.onStart()
        Log.d("USER",Constants.CURRENT_LOGGED_USER)
        FireStoreDatabaseAPI().searchUserInDatabase(Constants.CURRENT_LOGGED_USER).thenAccept { user ->
            Log.d("USER PROV",user.serviceProvider)
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val result = handlePermissionsResult(requestCode, grantResults)
        when (result) {
            "AUDIO_PERMISSION_GRANTED" -> Toast.makeText(this, "The permission was successfully granted! You can now shazam songs!",
                Toast.LENGTH_LONG).show()
            "AUDIO_PERMISSION_DENIED" -> Toast.makeText(this, "The app needs the microphone access to analyze " +
                    "which song you are listening to", Toast.LENGTH_LONG).show()
            "CAMERA_PERMISSION_GRANTED" -> Toast.makeText(this, "The permission was successfully granted! " +
                    "You can now share pictures and videos with your friends",
                Toast.LENGTH_LONG).show()
            "CAMERA_PERMISSION_DENIED" -> Toast.makeText(this, "The app needs the camera access to share songs with your friends", Toast.LENGTH_LONG).show()
        }
    }

    open fun handlePermissionsResult(requestCode: Int, grantResults: IntArray): String {
        if (requestCode == PermissionConstants.AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                return "AUDIO_PERMISSION_GRANTED"
            } else {
                // Permission denied
                return "AUDIO_PERMISSION_DENIED"
            }
        }
        if(requestCode == PermissionConstants.CAMERA_PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                return "CAMERA_PERMISSION_GRANTED"
            } else {
                // Permission denied
                return "CAMERA_PERMISSION_DENIED"
            }
        }
        return "UNKNOWN_REQUEST_CODE"
    }
}



