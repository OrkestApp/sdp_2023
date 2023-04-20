package com.github.orkest.Model

import android.content.Intent
import android.net.Uri
<<<<<<< HEAD
import android.util.Log
=======
>>>>>>> main
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
<<<<<<< HEAD
        private  val REDIRECT_URI = "http://10.0.17.226:5000/deezer"
=======
        private  val REDIRECT_URI = "http://172.20.10.3:5000/deezer"
>>>>>>> main

        val url = Uri.parse(
            DEEZER_AUTH_URL +
                    "?app_id=$CLIENT_ID" +
                    "&redirect_uri=$REDIRECT_URI" +
                    "&response_type=token"
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
                val trackList = json.fromJson(response,ListTrack::class.java)
                completableFuture.complete(trackList)
            } else {
                // TODO  Handle the error case here...
            }
        }.start()

        return completableFuture

    }

<<<<<<< HEAD
=======
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

>>>>>>> main
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
                val playlist = json.fromJson(response,ListPlaylist::class.java)
                playlistFuture.complete(playlist)
            } else {
                // TODO  Handle the error case here...
            }
        }.start()

        return playlistFuture
    }


<<<<<<< HEAD

    fun launchDeezerToPlayAPlaylist(playlistId : String) : Intent{
        val intent = CompletableFuture<Intent>()
        val uri = Uri.parse("http://www.deezer.com/track/$playlistId")
=======
    /**
     * We will need this method when we will want to play the entire playlist of shared songs
     */

    fun launchDeezerToPlayAPlaylist(playlistId : String) : Intent{
        val intent = CompletableFuture<Intent>()
        val uri = Uri.parse("http://www.deezer.com/playlist/$playlistId")
>>>>>>> main
        return Intent(Intent.ACTION_VIEW, uri)




    }





<<<<<<< HEAD
    fun launchDeezerToPlaySong(songName: String?, artistName : String = ""): CompletableFuture<Intent> {
        Log.d("DEBUG FETCH API",songName + artistName)
        val intent = CompletableFuture<Intent>()
        searchSongInDeezerDatabse(songName,artistName).thenAccept {//TODO FIX ARTIST NAME
=======
    fun launchDeezerToPlaySong(songName: String?, artistName: String=""): CompletableFuture<Intent> {

        val intent = CompletableFuture<Intent>()
        searchSongInDeezerDatabse(songName,artistName).thenAccept {
>>>>>>> main
            val trackId = it.data[0].id
            val uri = Uri.parse("http://www.deezer.com/track/$trackId")
            intent.complete(Intent(Intent.ACTION_VIEW, uri))

        }
        return intent


<<<<<<< HEAD
    }
=======
        }
>>>>>>> main


}