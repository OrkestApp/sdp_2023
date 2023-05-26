package com.github.orkest.ui.sharing

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import com.github.orkest.data.Constants
import com.github.orkest.data.Providers
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.ui.CreateProfilePreview
import com.github.orkest.ui.feed.CreatePost
import com.github.orkest.ui.search.SearchUserView
import com.github.orkest.ui.search.SearchViewModel
import com.github.orkest.ui.theme.OrkestTheme

class Share : ComponentActivity() {

    private var spotifySongID: String = String()      // spotify song ID

    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // get the spotify song ID
        spotifySongID = intent.getStringExtra("songID").toString()


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


    @Composable
    fun UserSelection() {
        val viewModel = SearchViewModel()
        var text by remember { mutableStateOf("search User") }
        var list by remember { mutableStateOf(mutableListOf("")) }
        /*
    viewModel.searchUserInDatabase(text).thenAccept {
        list = it.map { user -> user.username }.toMutableList()
    }

     */
        //Show all the followers of the CURRENT_LOGGED_USER
        FireStoreDatabaseAPI().fetchFollowList(Constants.CURRENT_LOGGED_USER, false).thenAccept {
            list = it
        }



        Column(modifier = Modifier.fillMaxSize())
        {
            Text("Select a user to share with")
            // search bar
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                })
            // list of users
            LazyColumn {
                items(list) { username ->
                    // create an intent
                    val context = LocalContext.current

                    // send username and name of the song
                    Log.d("STORING", "Song name: ${SharingComposeActivity.songName}")
                    val intent = Intent(context, PlaylistActivity::class.java)
                    intent.putExtras(
                        bundleOf(
                            "songName" to SharingComposeActivity.songName,
                            "songArtist" to SharingComposeActivity.songArtist,
                            "songID" to spotifySongID,
                            "senderUsername" to Constants.CURRENT_LOGGED_USER,
                            "receiverUsername" to username
                        )
                    )
                    CreateProfilePreview(username, intent)

                }
            }
        }
    }
}

