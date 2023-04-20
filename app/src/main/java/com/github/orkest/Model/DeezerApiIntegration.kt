package com.github.orkest.Model

import android.content.Intent
import android.net.Uri
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
        private  val REDIRECT_URI = "http://10.0.17.226:5000/deezer"

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



    fun launchDeezerToPlayAPlaylist(playlistId : String) : Intent{
        val intent = CompletableFuture<Intent>()
        val uri = Uri.parse("http://www.deezer.com/track/$playlistId")
        return Intent(Intent.ACTION_VIEW, uri)




    }





    fun launchDeezerToPlaySong(songName: String?, artistName: String): CompletableFuture<Intent> {

        val intent = CompletableFuture<Intent>()
        searchSongInDeezerDatabse(songName,artistName).thenAccept {
            val trackId = it.data[0].id
            val uri = Uri.parse("http://www.deezer.com/track/$trackId")
            intent.complete(Intent(Intent.ACTION_VIEW, uri))

        }
        return intent


    }


}