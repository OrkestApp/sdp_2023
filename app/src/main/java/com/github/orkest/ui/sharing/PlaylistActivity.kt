package com.github.orkest.ui.sharing

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.github.orkest.data.Profile
import com.github.orkest.ui.sharedMusic.sharedMusicPost
import androidx.room.Room
import com.github.orkest.domain.DeezerApiIntegration
import com.github.orkest.data.Song
import com.github.orkest.domain.persistence.AppDatabase
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

        // check if extras are not null
        Log.d("PlaylistActivity", "songId: ${intent.getStringExtra("songId")}")
        intent.getStringExtra("songName")?.let {
            // update database
            val song = Song(
                Title = it,
                Artist = "Unknown Artist",
                Album = "Unknown Album",
                URL = "Unknown ID")

            // store song in a way that both users can see them in their pages
            playlistViewModel.storeSong(
                song,
                intent.getStringExtra("senderUsername") ?: "sender1",
                intent.getStringExtra("receiverUsername") ?: "sender1",
                this
            )
            /*
            playlistViewModel.storeSong(
                song,
                intent.getStringExtra("receiverUsername") ?: "xx",
                intent.getStringExtra("senderUsername") ?: "xx",
                this
            )

             */

        }
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

    LazyColumn {

        items(songList) { song ->
            // TODO enhance UI
            Row(modifier =
            Modifier
                .clickable {
                    // play song
                    val player = DeezerApiIntegration()
                    Log.d("HELLO SONG TITLE", song.Title)
                    startActivity(context,
                        player
                            .launchDeezerToPlaySong(song.Title)
                            .get(),
                        null
                    )

                }
                .fillMaxSize()
            )
            {
                sharedMusicPost(profile = Profile(
                    username = receiverUsername ),
                    song = song,
                    message = "Dummy")
            }


        }
    }

//        for (song in songList) {
//            // TODO enhance UI
//            Row(modifier =
//            Modifier.clickable {
//                // play song
//                val player = DeezerApiIntegration()
//                startActivity(context, player.launchDeezerToPlaySong(song.Title).get(), null)
//
//            }.fillMaxSize())
//            {
//                Text(text = song.Title)
//                Text(text = song.Artist)
//            }
//        }
    }


