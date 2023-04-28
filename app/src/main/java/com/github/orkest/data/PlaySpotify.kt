package com.github.orkest.data

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

open class PlaySpotify {

    companion object {
        private const val CLIENT_ID = "e7ac920406d54975bc79962dec94f4ab"
        private const val REDIRECT_URI = "https://com.github.orkest/callback/"
        private var mSpotifyAppRemote: SpotifyAppRemote? = null

        /**
         * This method is used to convert a song to a Spotify uri
         * @param song the song to convert
         */
        private fun songToUri(song: Song): String {
            //TODO: Once the export is complete, should get the song's uri from the database
            return "spotify:track:6rqhFgbbKwnb9MLmUQDhG6"
        }

        /**
         * This method is used to convert a playlist to a list of Spotify uris
         * @param playlist the playlist to convert
         */
        private fun playlistToUri(playlist: List<Song>): List<String> {
            return playlist.map { songToUri(it) }
        }

        /**
         * This method is used to connect to the Spotify API App Remote
         * @param context the context of the activity
         */
        fun setupSpotifyAppRemote(context: Context) {
            val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()

            SpotifyAppRemote.connect(context, connectionParams,
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote
                        Log.d("PlaySpotify", "Connected! Yay!")
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.e("MyActivity", throwable.message, throwable)
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                })
        }

        /**
         * This method is used to play a song
         */
        fun play(song: Song) {
            // Play a playlist
            val uri = songToUri(song)
            mSpotifyAppRemote?.playerApi?.play(uri)
        }
    }
}