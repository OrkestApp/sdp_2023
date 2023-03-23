package com.github.orkest.View

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController

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
    }
}
