package com.github.orkest.View

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

class DeezerWelcomeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContent {
            createViewForDeezer()
        }
    }


    @Composable
    fun createViewForDeezer(){
        var text by remember { mutableStateOf("Enter userName ") }
        TextField(value = text, onValueChange = { text = it})
        Button(onClick = { saveTokenInDatabase(); launchMainActivity()}, content = { Text("Start to use Deezer")})


    }


    private fun launchMainActivity(){

    }

    @Preview
    @Composable
    fun preview_DeezerWelcome(){
        createViewForDeezer()
    }

    private fun saveTokenInDatabase(){

    }


}