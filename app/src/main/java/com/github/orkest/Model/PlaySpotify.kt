package com.github.orkest.Model

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Uri

class PlaySpotify {




    companion object {
        private const val CLIENT_ID = "e7ac920406d54975bc79962dec94f4ab"
        private const val REDIRECT_URI = "https://com.github.orkest/callback/"
        private var mSpotifyAppRemote: SpotifyAppRemote? = null

        fun songToUri(song: Song): String {
            return "spotify:track:6rqhFgbbKwnb9MLmUQDhG6"
        }

        fun playlistToUri(playlist: List<Song>): List<String> {
            return playlist.map { songToUri(it) }
        }


        fun play(context: Context, songUri: String) {
            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()

            SpotifyAppRemote.connect(context, connectionParams,
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote
                        Log.d("PlaySpotify", "Connected! Yay!")

                        // Now you can start interacting with App Remote
                        connected(songUri)
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.e("MyActivity", throwable.message, throwable)

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                })
        }

        private fun connected(uri: String) {
            // Play a playlist
           // mSpotifyAppRemote!!.playerApi.play(uri)//"spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")

            mSpotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")

            // Subscribe to PlayerState
            mSpotifyAppRemote!!.playerApi
                .subscribeToPlayerState()
                .setEventCallback { playerState: PlayerState ->
                    val track = playerState.track
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name)
                    }
                }
        }
    }
}