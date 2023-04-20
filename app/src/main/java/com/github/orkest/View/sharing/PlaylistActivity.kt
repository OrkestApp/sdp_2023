package com.github.orkest.View.sharing

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.github.orkest.Model.DeezerApiIntegration
import com.github.orkest.Model.Song
import com.github.orkest.View.sharing.ui.theme.OrkestTheme
import com.github.orkest.ViewModel.playlist.PlaylistViewModel
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class PlaylistActivity() : ComponentActivity() {

    companion object {
        private const val CLIENT_ID = "e7ac920406d54975bc79962dec94f4ab"
        private const val REDIRECT_URI = "https://com.github.orkest/callback/"
    }

    private val playlistViewModel = PlaylistViewModel()
    private var spotifySongId = ""
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // -------Connect to Spotify AppRemote-------
        this.spotifySongId = intent.getStringExtra("songId") ?: "songId"

        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MyActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })


        //--------Update Database---------

        // check if extras are not null
        Log.d("PlaylistActivity", "songId: ${intent.getStringExtra("songId")}")
        intent.getStringExtra("songName")?.let {
            // update database
            val song = Song(
                Title = it,
                Artist = "Unknown Artist",
                Album = "Unknown Album",
                URL = "Unknown ID")

            playlistViewModel.storeSong(
                song,
                intent.getStringExtra("senderUsername") ?: "sender1",
                intent.getStringExtra("receiverUsername") ?: "sender1"
            )
            playlistViewModel.storeSong(
                song,
                intent.getStringExtra("receiverUsername") ?: "xx",
                intent.getStringExtra("senderUsername") ?: "xx"
            )

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
            mSpotifyAppRemote?.let {
                Playlist(
                    this.playlistViewModel,
                    intent
                        .getStringExtra("senderUsername") ?: "sender1",
                    intent
                        .getStringExtra("receiverUsername") ?: "receiver1",
                    it,
                    spotifySongId
                )
            }
        }
    }
}


@Composable
fun Playlist(playlistViewModel: PlaylistViewModel,
             senderUsername: String,
             receiverUsername: String,
             appRemote: SpotifyAppRemote,
             spotifySongId: String
){
    val context = LocalContext.current

    var songList by remember { mutableStateOf(listOf<Song>()) }
    playlistViewModel.fetchSongs(senderUsername, receiverUsername)
        .whenComplete { songs, _ ->
            songs?.let {
                songList = it
            }
        }

    Column {
        for (song in songList) {
            // TODO enhance UI
            Row(modifier =
            Modifier.clickable {
                // play song
//                appRemote.playerApi.play("spotify:playlist:$spotifySongId")
                val player = DeezerApiIntegration()

                startActivity(context, player.launchDeezerToPlaySong(song.Title, song.Artist).get(), null)

            }.fillMaxSize())
            {
                Text(text = song.Title)
                Text(text = song.Artist)
            }
        }
    }


}

