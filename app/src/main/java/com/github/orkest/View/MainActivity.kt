package com.github.orkest.View

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.orkest.Constants
import com.github.orkest.R
import com.github.orkest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(Constants.currentLoggedUser == ""){
                Log.e(TAG, "currentLoggedUser is empty", IllegalArgumentException())
            }
            NavigationBar.CreateNavigationBar(navController = rememberNavController(), Constants.currentLoggedUser)
        }
    }
}
