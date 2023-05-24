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
import androidx.test.platform.app.InstrumentationRegistry
import com.github.orkest.data.Constants
import com.github.orkest.domain.*
import java.util.concurrent.CompletableFuture

class DeezerWelcomeActivity(private val mock: Boolean) : AppCompatActivity(){

    /**
     * This will be used later in the code
     */

    private var code =""
    private val db = FireStoreDatabaseAPI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HELLO TEST","HELLO")
        var deezerApi = DeezerApiIntegration(deezerApiImplemented())
        var codeValue: String? =""
        if(!mock){
            val intent: Intent = intent
            val deepLink: String = intent.data.toString()
            val uri: Uri = Uri.parse(deepLink)
             codeValue = uri.getQueryParameter("code")

        }

        if(mock){
            deezerApi = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        }
        //FOLLOWING OPERATIONS NEEDS TO BE DONE SEQUENCIALY BECAUSE THEY ALL NEED PREVIOUS RESULTS
        if(codeValue == "" && this.mock){
            codeValue = "DumbUser"
        }
        if (codeValue != null) {
            WelcomeOperation(codeValue = codeValue, deezerApi = deezerApi, username = Constants.CURRENT_LOGGED_USER)
        }
        else{
            Log.d("DEEZER_OAUTH_FAIL","token was empty")
        }


        setContent {
            CreateViewForDeezer()
        }
    }

    companion object {
        /**
         * 1) Stores the access token along with the userName
         * 2) Fetch the user Id using the Deezer API
         * 3) Create a orkest Playlist then waits for the playlist ID
         * 4) Stores the playlist id back into the database
         */
        fun WelcomeOperation(
            codeValue: String,
            deezerApi: DeezerApiIntegration,
            username: String
        ): CompletableFuture<Boolean> {

            val completableFuture = CompletableFuture<Boolean>()
            val db = FireStoreDatabaseAPI()
            val future = db.storeDeezerInformationsInDatabase(
                username,
                codeValue
            ) //store the token in the database
            future.thenAccept {
                if (!it) {
                    Log.d("DB_OPERATION", "Fail to store token in the database")
                } else { // waits for the access token to be successfully stored in the database
                    Log.d("CHECKPOINT DEEZER", "12")
                    db.getUserDeezerInformations(username).thenAccept { data ->
                        if (data.access_token != "" && data.access_token != null) {
                            Log.d("CHECKPOINT DEEZER", "1")
                            val userIdFuture =
                                deezerApi.fetchTheUserIdInTheDeezerDatabase(data.access_token!!)
                            userIdFuture.thenAccept { // waits for the user Id fetch in the database
                                    user ->
                                val playlistIdFuture = deezerApi.createANewPlaylistOnTheUserProfile(
                                    user.id,
                                    data.access_token!!
                                )
                                playlistIdFuture.thenAccept { // waits for the playlist to be created on the user's profile and then return the playlist ID
                                        playlistId ->
                                    if (playlistId != "" && playlistId != null) {
                                        db.storeDeezerInformationsInDatabase(
                                            username,
                                            data.access_token!!,
                                            user.id,
                                            playlistId
                                        ) // Finaly store all the user's deezer credentials in the database
                                        completableFuture.complete(true)
                                    } else {
                                        Log.d(
                                            "DEEZER_OAUTH_FAIL",
                                            "Failed to store deezer informations"
                                        )
                                    }
                                }
                            }

                        }
                    }
                }

            }
            return completableFuture
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
        context.startActivity(intent)
    }




}