package com.github.orkest.ui.sharing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.github.orkest.data.Profile
import com.github.orkest.ui.sharedMusic.sharedMusicPost
import androidx.room.Room
import com.github.orkest.R
import com.github.orkest.data.Constants
import com.github.orkest.data.Song
import com.github.orkest.domain.persistence.AppDatabase
import com.github.orkest.ui.MainActivity
import com.github.orkest.ui.theme.OrkestTheme
import com.spotify.android.appremote.api.SpotifyAppRemote

/*
    * This class is used to display the list of songs that the user share with other users.
 */
class PlaylistActivity() : ComponentActivity() {



    private lateinit var playlistViewModel : PlaylistViewModel
    private var spotifySongId = ""
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // -------Connect to Spotify AppRemote-------
        // TODO include spotify in addition to Deezer

        //--------Update AppDatabase---------

        val appDao = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-orkest"
        ).build().songsDao()

        playlistViewModel = PlaylistViewModel(appDao)

        val song = Song()

        // check if extras are not null
        Log.d("PlaylistActivity", "songId: ${intent.getStringExtra("songId")}")
        intent.getStringExtra("songName")?.let { song.Title = it }
        intent.getStringExtra("songArtist")?.let { song.Artist = it }

        // store song in a way that both users can see them in their pages
        playlistViewModel.storeSong(
            song,
            intent.getStringExtra("senderUsername") ?: "sender1",
            intent.getStringExtra("receiverUsername") ?: "sender1",
            this
        )

        //--------------UI----------------
        setContent {
            OrkestTheme {
               SetContent()
            }
        }
    }

    @Composable
    private fun SetContent(){
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            Playlist(
                this.playlistViewModel,
                intent
                    .getStringExtra("senderUsername") ?: "sender1",
                intent
                    .getStringExtra("receiverUsername") ?: "receiver1",
                spotifySongId
            )

        }
    }
}

/*
    * This is a Composable that fetches the songs from the database and displays them.
 */
@Composable
fun Playlist(playlistViewModel: PlaylistViewModel,
             senderUsername: String,
             receiverUsername: String,
             spotifySongId: String
) {
    val context = LocalContext.current

    var songList by remember { mutableStateOf(listOf<Song>()) }
    playlistViewModel.fetchSongs(senderUsername, receiverUsername, context)
        .whenComplete { songs, _ ->
            songs?.let {
                songList = it
            }
        }

    Column() {
        //Add a title to the page
        androidx.compose.material3.Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 20.dp),
            text = "Songs Shared With You by $receiverUsername",

            style = TextStyle(fontSize = 20.sp, fontFamily = Constants.FONT_MARKER),
            color = Color.Black
        )

        LazyColumn {

            items(songList) { song ->
                // TODO enhance UI
                Row(modifier =
                Modifier
                    .clickable {
                        Constants.playMusicButtonClicked(song, mutableStateOf(false), context)
                    }
                    .fillMaxSize()
                )
                {
                    sharedMusicPost(
                        profile = Profile(
                            username = receiverUsername
                        ),
                        song = song,
                        message = "Dummy"
                    )
                }

            }
        }
    }

    //Add a button to go back to the main Activity
        Box(contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
                    .testTag("addPostButton"),
                backgroundColor = Color.White,
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(context, intent, null)
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_black_24dp),
                    contentDescription = "Go to Home"
                )
            }
        }

    }


