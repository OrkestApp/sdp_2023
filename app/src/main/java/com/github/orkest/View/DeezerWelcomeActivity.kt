package com.github.orkest.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

class DeezerWelcomeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent = intent
        val deepLink: String = intent.getData().toString()

// Parse the deep link into a Uri object

// Parse the deep link into a Uri object
        val uri: Uri = Uri.parse(deepLink)

// Get the value of a query parameter

// Get the value of a query parameter
        val param1Value: String? = uri.getQueryParameter("param1")

        setContent {
            createViewForDeezer()
        }
    }


    @Composable
    fun createViewForDeezer(){
        var text by remember { mutableStateOf("Enter userName ") }
        Column() {
            TextField(value = text, onValueChange = { text = it})
            Button(onClick = { saveTokenInDatabase(); launchMainActivity()}, content = { Text("Start to use Deezer")})

        }


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