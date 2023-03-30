package com.github.orkest.View.sharing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.View.search.SearchUserView
import com.github.orkest.View.sharing.ui.theme.OrkestTheme
import com.github.orkest.ViewModel.search.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class SharingComposeActivity : ComponentActivity() {

    private var accessToken : String = ""

    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    Log.d("Debug", "got text")
                    handleSendText(intent) // Handle text being sent
                }
            }
        }
        // ----------------- Spotify API -----------------

        // TODO: get access token from Spotify API
//        val token = Authorization.getAccessToken()
//
//        Log.d("token", token.toString())


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

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            // use spotify API to get the name of the song from the link

            // extract Spotify ID from the link
            val spotifyID = it.substringAfterLast("/").substringBefore("?")
            //TODO: debug
            //getTrackName(spotifyID)

        }
    }

    fun getTrackName(spotifyUri: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/tracks/$spotifyUri")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val responseData = response.body?.string()
            val jsonObject = JSONObject(responseData)
            val trackName = jsonObject.getString("name")

            Log.d("MainActivity", "Track name: $trackName")
        }
    }
}


@Composable
fun UserSelection(){
    val viewModel = SearchViewModel()
    var text by remember { mutableStateOf("search User") }
    var list by remember { mutableStateOf(mutableListOf("")) }

    viewModel.searchUserInDatabase(text).thenAccept {
        list = it
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
//               // Temporary solution-to be replaced by an intent to launch the chat with the user
                val intent : Intent = Intent(context, ProfileActivity::class.java)

                SearchUserView.CreateUser(name = username, intent)

            }
        }
    }
}

