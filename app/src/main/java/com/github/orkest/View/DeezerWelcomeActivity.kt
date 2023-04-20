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
import com.github.orkest.Constants
import com.github.orkest.Model.DeezerApiIntegration
import com.github.orkest.Model.FireStoreDatabaseAPI

class DeezerWelcomeActivity : AppCompatActivity(){

    /**
     * This will be used later in the code
     */

    private var code =""
    private var username = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent = intent
        val deepLink: String = intent.data.toString()


        val uri: Uri = Uri.parse(deepLink)


        val codeValue: String? = uri.getQueryParameter("code")
        val future = FireStoreDatabaseAPI().storeTokenInDatabase(Constants.CURRENT_LOGGED_USER,codeValue)
        if (codeValue != null) {
            code = codeValue
        }

        setContent {
            createViewForDeezer()
        }
    }

    //TODO compare typed username with Actual username

    @Composable
    fun createViewForDeezer(){
        var text by remember { mutableStateOf("Enter userName ") }
        Column() {
            TextField(value = text, onValueChange = { text = it; username=it})
            Button(onClick = { }, content = { Text("Start to use Deezer")})

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