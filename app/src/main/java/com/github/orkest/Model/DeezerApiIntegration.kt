package com.github.orkest.Model

import android.net.Uri
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import java.util.concurrent.CompletableFuture
import com.google.gson.Gson

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
     * Fetch in the deezer
     */
    fun searchSongInDeezerDatabse(songName: String? , artistName:String = ""): CompletableFuture<ListTrack>{
        val completableFuture = CompletableFuture<ListTrack>()
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
                // Handle the error case here...
            }
        }.start()

        return completableFuture

    }

    fun launchDeezerToPlaySong(id:String){

    }
}