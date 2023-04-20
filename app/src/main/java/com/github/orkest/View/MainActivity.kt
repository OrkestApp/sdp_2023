package com.github.orkest.View

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.github.orkest.Constants
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.PlaySpotify
import com.github.orkest.Model.Providers


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(Constants.CURRENT_LOGGED_USER == ""){
                Log.e(TAG, "currentLoggedUser is empty", IllegalArgumentException())
            }
            NavigationBar.CreateNavigationBar(navController = rememberNavController(), Constants.CURRENT_LOGGED_USER)
        }
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
