package com.github.orkest.ui.sharing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import com.github.orkest.data.Constants
import com.github.orkest.domain.Authorization.Companion.getLoginActivityTokenIntent
import com.github.orkest.domain.Authorization.Companion.requestUserAuthorization
import com.github.orkest.ui.search.SearchUserView
import com.github.orkest.ui.theme.OrkestTheme
import com.github.orkest.ui.search.SearchViewModel
import com.github.orkest.ui.sharing.PlaylistActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture


class SharingComposeActivity : ComponentActivity() {

    var accessToken : String = String()        // access token for Spotify API
    private var authorizationCode : String = String()  // authorization code for token request
    private var spotifySongID : String = String()      // spotify song ID

    // spotify song name
    companion object {
        var spotifySongName : String = String()
        var spotifySongArtist : String = String()
    }



    /**
     * This override of the onCreate method is used to get the text from the intent
     * The text corresponds to a spotify song URL that is used to get the song ID and
     * song name.
     *
     * It consists of 3 parts:
     * 1. Intent handling
     * 2. Spotify API: It performs the PKCE Authorization Code Flow to get the access token
     * 3. Compose UI
     *
     * @param savedInstanceState the saved instance state
     * @see <a href="https://developer.android.com/guide/components/activities/intents-filters"></a>
     *
     */
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Get the song name from the intent ----------------- Intent from Orkest -----------------
        if (intent.hasExtra(Constants.SONG_NAME)) {
            spotifySongName = intent.getStringExtra(Constants.SONG_NAME).toString()
            if (intent.hasExtra(Constants.SONG_ARTIST)) {
                spotifySongArtist = intent.getStringExtra(Constants.SONG_ARTIST).toString()
            }
        } else {


            // ----------------- Intent handling from spotify for songID -----------------
            when (intent?.action) {
                Intent.ACTION_SEND -> {
                    if ("text/plain" == intent.type) {
                        Log.d("Debug", "got text")
                        handleSendText(intent) // Handle text being sent
                    }
                }
            }

            // ----------------- Spotify API -----------------
            spotifyAuthorization()
        }

        // ----------------- Compose UI -----------------
        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UserSelection()
                }
            }
        }
    }

    /**
     * This function is used to connect to the Spotify API and get the song name
     *
     */
    private fun spotifyAuthorization(){
        // This is a callback for the authorization function that requests the access token
        val showLoginActivityToken = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->

            val authorizationResponse = AuthorizationClient.getResponse(result.resultCode, result.data)

            // Check the response type
            when (authorizationResponse.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    // Retrieve the access token
                    this.accessToken = authorizationResponse.accessToken
                    Log.d("SPOTIFY TOKEN", this.accessToken)
                    CompletableFuture.runAsync {
                        getTrackName(this.spotifySongID)
                    }
                }
                AuthorizationResponse.Type.ERROR ->
                    Log.d("SPOTIFY ERROR", authorizationResponse.error)
                else ->
                    Log.d("SPOTIFY FATAL ERROR", "Abort")
            }
        }

        // This is a callback for the authorization function that requests the authorization code
        val showLoginActivityCode = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->

            val authorizationResponse = AuthorizationClient.getResponse(result.resultCode, result.data)
            when (authorizationResponse.type) {
                AuthorizationResponse.Type.CODE -> {
                    // retrieve the authorization code
                    this.authorizationCode = authorizationResponse.code
                    Log.d("SPOTIFY", "Code: ${this.authorizationCode}")
                    CompletableFuture.runAsync {
                        showLoginActivityToken.launch(getLoginActivityTokenIntent(authorizationCode, this))
                    }
                }
                AuthorizationResponse.Type.ERROR ->
                    // log the error
                    Log.d("Debug", "Error: ${authorizationResponse.error}")
                else ->
                    // interrupt
                    Log.d("Debug", "Error: Fatal")
            }
        }

        val completableFutureSong = CompletableFuture.supplyAsync {
            showLoginActivityCode.launch(requestUserAuthorization(this))
        }

        // retrieve the song name from the future
        completableFutureSong.get()


    }

    /**
     * This function extracts the spotify song ID shared
     * from Spotify.
     *
     * @param intent The intent that is sent to the app
     * @see <a href="https://developer.android.com/guide/components/activities/intents-filters"></a>
     */
    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            // extract Spotify ID from the link
            this.spotifySongID = it.substringAfterLast("/").substringBefore("?")
        }
    }

    /**
     * This function is used to get the track name from the Spotify API
     *
     * @param spotifyUri The Spotify song ID
     * @return A CompletableFuture that contains the track name
     * @see <a href="https://developer.android.com/reference/java/util/concurrent/CompletableFuture"></a>
     */
    private fun getTrackName(spotifyUri: String) : CompletableFuture<String>{
        val future = CompletableFuture<String>()
        val client = OkHttpClient()

        // construct a request containing the URI and the access token
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/tracks/$spotifyUri")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        // send the request to the Spotify API
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                future.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    future.completeExceptionally(Exception("Unexpected code ${response.code}"))
                    return
                }

                val responseData = response.body?.string()
                val jsonObject = responseData?.let { JSONObject(it) }
                val trackName = jsonObject?.getString("name")

                Log.d("ShareActivity", "Track name: $trackName")
                spotifySongName = trackName.toString()
                future.complete(trackName)

            }
        })

        return future
}


@Composable
fun UserSelection(){
    val viewModel = SearchViewModel()
    var text by remember { mutableStateOf("search User") }
    var list by remember { mutableStateOf(mutableListOf("")) }

    viewModel.searchUserInDatabase(text).thenAccept {
        list = it.map { user -> user.username }.toMutableList()
    }
    Column(modifier = Modifier.fillMaxSize())
    {
        Text("Select a user to share with")
        // search bar
        OutlinedTextField(
            value = text ,
            onValueChange = {
                text = it
            } )
        // list of users
        LazyColumn{
            items(list){ username ->
                // create an intent
                val context = LocalContext.current

                // send username and name of the song
                Log.d("STORING", "Song name: $spotifySongName")
                val intent = Intent(context, PlaylistActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "songName" to spotifySongName,
                        "songID" to spotifySongID,
                        "senderUsername" to Constants.CURRENT_LOGGED_USER,
                        "receiverUsername" to username
                    )
                )

                SearchUserView.CreateUser(name = username, intent)

                }
            }
        }
    }
}

