package com.github.orkest.domain

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.github.orkest.data.User

import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture

class DeezerApiIntegration {
    companion object {
        private  val DEEZER_AUTH_URL = "https://connect.deezer.com/oauth/auth.php"
        private  val DEEZER_TOKEN_URL = "https://connect.deezer.com/oauth/access_token.php"
        private  val CLIENT_ID = "592224"
        private  val CLIENT_SECRET = "7951a1e4171f70af65cae5c55fdd0e51"
        private  val REDIRECT_URI = "http://172.20.10.3:5000/deezer"


        val url = Uri.parse(
            DEEZER_AUTH_URL +
                    "?app_id=$CLIENT_ID" +
                    "&redirect_uri=$REDIRECT_URI" +
                    "&perms=basic_access,email,manage_library,offline_access"
        )
    }

    /**
     * Fetch in the deezer API all the songs that matches the title we gave
     */
    fun searchSongInDeezerDatabse(songName: String? , artistName:String = ""): CompletableFuture<ListTrack>{
        val completableFuture = CompletableFuture<ListTrack>()

        //Need to be on an other Thread because if on the main Thread would block the UI
        Thread{
            val connection = URL("https://api.deezer.com/search/track?q=$songName $artistName").openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                val json = Gson()
                val trackList = json.fromJson(response, ListTrack::class.java)
                completableFuture.complete(trackList)
            } else {
                // TODO  Handle the error case here...
            }
        }.start()

        return completableFuture

    }



    /**
     * fetch the database for the Id of the logged in user
     * We will need this Id to update the playlist
     */

    fun fetchTheUserIdInTheDeezerDatabase(access_token:String):CompletableFuture<DeezerModelClasses.UserExtended>{
        val completableFuture = CompletableFuture<DeezerModelClasses.UserExtended>()
        Thread{
            val connection = URL("https://api.deezer.com/user/me?access_token=$access_token").openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                val json = Gson()
                val user = json.fromJson(response, DeezerModelClasses.UserExtended::class.java)
                completableFuture.complete(user)
            } else {
            Log.d("DEEZER_API_FAIL", "Problems fetching the user in the deezer database")
            }
        }.start()

        return completableFuture

    }

    /**
     * Finaly,
     * Need the access token of the user and use it to add a playlist to his Deezer profile, will able Orkest to share the music
     */

    fun createANewPlaylistOnTheUserProfile(userId:String,access_token: String,playlistTitle: String="Orkest"):CompletableFuture<String>{
        val completableFuture = CompletableFuture<String>()
        Thread{
            val connection = URL("https://api.deezer.com/user/$userId/playlists?access_token=$access_token&title=$playlistTitle").openConnection() as HttpURLConnection
            connection.requestMethod = "POST"

            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                val regex = Regex("\\D+") // Define a regular expression to match non-digit characters
                val cleared = regex.replace(response, "") // clear the string to a non -json format
                Log.d("HELLO", response.toString())
                completableFuture.complete(cleared)
            } else {
                Log.d("DEEZER_API_FAIL", "Problems fetching the user in the deezer database")
            }
        }.start()

        return completableFuture


    }

    /**
     * add a song the Orkest playlist of the User
     */
    fun addANewSongToOrkestPlayList(access_token: String,playlistId: String="Orkest",trackId:String):CompletableFuture<Boolean>{
        val completableFuture = CompletableFuture<Boolean>()
        Thread{
            val connection = URL("https://api.deezer.com/playlist/$playlistId/tracks?access_token=$access_token&songs=$trackId").openConnection() as HttpURLConnection
            connection.requestMethod = "POST"

            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                completableFuture.complete(true)
            } else {
                Log.d("DEEZER_API_FAIL", "Problems adding a song to the database")
            }
        }.start()

        return completableFuture


    }

    /**
     * Generic Function to deserialize JSON
     */
    inline fun <reified T> deserialise(string: String): T {
        val jsonParser = Gson()
        return jsonParser.fromJson(string, T::class.java)
    }



    /**
     * We will need this method later when Orkest will add a playlist to the User profile
     */

    fun searchPlaylistInDatabase(playlistName:String):CompletableFuture<ListPlaylist>{
        val playlistFuture = CompletableFuture<ListPlaylist>()
        Thread{
            val connection = URL("https://api.deezer.com/search/playlist?q=$playlistName").openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                val json = Gson()
                val playlist = json.fromJson(response, ListPlaylist::class.java)
                playlistFuture.complete(playlist)
            } else {
                // TODO  Handle the error case here...
            }
        }.start()

        return playlistFuture
    }


        /**
         * We will need this method when we will want to play the entire playlist of shared songs
         */

        fun launchDeezerToPlayAPlaylist(playlistId: String): Intent {
            val intent = CompletableFuture<Intent>()
            val uri = Uri.parse("http://www.deezer.com/playlist/$playlistId")
            return Intent(Intent.ACTION_VIEW, uri)


        }


        fun launchDeezerToPlaySong(
            songName: String?,
            artistName: String = ""
        ): CompletableFuture<Intent> {

            val intent = CompletableFuture<Intent>()
            searchSongInDeezerDatabse(songName, artistName).thenAccept {

                val trackId = it.data[0].id
                val uri = Uri.parse("http://www.deezer.com/track/$trackId")
                intent.complete(Intent(Intent.ACTION_VIEW, uri))

            }
            return intent


        }

}