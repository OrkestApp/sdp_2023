package com.github.orkest.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.orkest.data.Constants
import com.github.orkest.domain.DeezerApiIntegration
import com.github.orkest.domain.FireStoreDatabaseAPI

class DeezerWelcomeActivity : AppCompatActivity(){

    /**
     * This will be used later in the code
     */

    private var code =""
    private val db = FireStoreDatabaseAPI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = intent
        val deepLink: String = intent.data.toString()
        val uri: Uri = Uri.parse(deepLink)
        //FOLLOWING OPERATIONS NEEDS TO BE DONE SEQUENCIALY BECAUSE THEY ALL NEED PREVIOUS RESULTS
        val codeValue: String? = uri.getQueryParameter("code")
        if (codeValue != null) {
            code = codeValue
            val future = db.storeDeezerInformationsInDatabase(Constants.CURRENT_LOGGED_USER,codeValue) //store the token in the database
            future.thenAccept { if ( ! it) {
                Log.d("DB_OPERATION","Fail to store token in the database")
            }
                else { // waits for the access token to be successfully stored in the database
                    db.getUserDeezerInformations(Constants.CURRENT_LOGGED_USER).thenAccept { data ->
                        if(data.access_token != "" && data.access_token != null){
                            val userIdFuture = DeezerApiIntegration().fetchTheUserIdInTheDeezerDatabase(data.access_token!!)
                            userIdFuture.thenAccept { // waits for the user Id fetch in the database
                                user -> val playlistIdFuture = DeezerApiIntegration().createANewPlaylistOnTheUserProfile(user.id,data.access_token!!)
                                playlistIdFuture.thenAccept { // waits for the playlist to be created on the user's profile and then return the playlist ID
                                    playlistId ->
                                    if (playlistId != "" && playlistId !=null){
                                    db.storeDeezerInformationsInDatabase(Constants.CURRENT_LOGGED_USER,data.access_token!!, user.id,playlistId) // Finaly store all the user's deezer credentials in the database
                                    }
                                    else {
                                        Log.d("DEEZER_OAUTH_FAIL", "Failed to store deezer informations")
                                    }
                                }
                            }

                        }
                    }
            }

            }
        }
        else{
            Log.d("DEEZER_OAUTH_FAIL","token was empty")
        }


        setContent {
            CreateViewForDeezer()
        }
    }
    @Composable
    fun CreateViewForDeezer(){
        val context = LocalContext.current
        Column() {
            Text(text = "Hello ${Constants.CURRENT_LOGGED_USER}")
            Button(
                onClick = { launchMainActivity(context)},
                content = { Text("Start to use Deezer")},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow))
        }
    }
    private fun launchMainActivity(context: Context){
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    @Preview
    @Composable
    fun preview_DeezerWelcome(){
        CreateViewForDeezer()
    }



}