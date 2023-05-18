package com.github.orkest.domain

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.ui.unit.Constraints
import com.github.orkest.data.Constants
import com.github.orkest.data.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class DeezerApiIntegration(deezerAPi : DeezerApi)  {
    val x = deezerAPi

    companion object {
        private const val DEEZER_AUTH_URL = "https://connect.deezer.com/oauth/auth.php"
        private  val DEEZER_TOKEN_URL = "https://connect.deezer.com/oauth/access_token.php"
        private const val CLIENT_ID = "592224"
        private  val CLIENT_SECRET = "7951a1e4171f70af65cae5c55fdd0e51"
        private const val REDIRECT_URI = "http://172.20.10.3:5000/deezer"


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
            var response = ""
            if (songName != null ) {
                response = x.searchAsong(songName,artistName)
            }
            if(response !="" && response!= Constants.HTTP_FAIL_CODE){
                val trackList =  deserialise<ListTrack>(response)
                completableFuture.complete(trackList)
            }
            else{
                completableFuture.completeExceptionally(IOException())
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
            var response = ""
            if (access_token != "" ){
                response = x.fetchTheUserId(access_token)
            }
            if (response != Constants.HTTP_FAIL_CODE && response != "") {
                val user = deserialise<DeezerModelClasses.UserExtended>(response)
                completableFuture.complete(user)
            } else {
            completableFuture.completeExceptionally(IllegalArgumentException())
            }
        }.start()

        return completableFuture

    }


    /**
     * @param userId the userId of the user, needs to be fetch from deezer DB
     * @param access_token the private token of the user
     * @param playlistTitle default is Orkest, else we can choose the name of the playlist
     *
     * @return a completable future that completes with the playlist id of the newly created playlist
     * or complete exeptionaly with an IO exeption
     */
    fun createANewPlaylistOnTheUserProfile(userId:String,access_token: String,playlistTitle: String="Orkest"):CompletableFuture<String>{
        val completableFuture = CompletableFuture<String>()
        Thread{
            var responseCode = ""
            if (userId!= "" && access_token != "" ){
                responseCode = x.createANewPlaylistOnTheUserProfile(userId,access_token,playlistTitle)
            }
            if (responseCode != Constants.HTTP_FAIL_CODE && responseCode != "") {
                val regex = Regex("\\D+") // Define a regular expression to match non-digit characters
                val cleared = regex.replace(responseCode, "") // clear the string to a non -json format
                completableFuture.complete(cleared)
            } else {
                Log.d("createANewPlaylistOnTheUserProfile","FAILED")
                completableFuture.completeExceptionally(IOException())
            }
        }.start()

        return completableFuture


    }

    /**
     * add a song the Orkest playlist of the User
     */
    fun addANewSongToOrkestPlayList(access_token: String,playlistId: String,trackId:String):CompletableFuture<Boolean>{
        val completableFuture = CompletableFuture<Boolean>()
        Thread{

            var response = ""
            if(access_token!= "" && playlistId != "" && trackId != "") {
                response = x.addANewSongToOrkestPlayList(access_token,playlistId,trackId)
            }
            if (response != Constants.HTTP_FAIL_CODE && response != "") {
                completableFuture.complete(true)
            } else {
                completableFuture.completeExceptionally(IOException())
            }
        }.start()

        return completableFuture


    }

    /**
     * Generic Function to deserialize JSON
     */
    inline fun <reified T> deserialise(string: String): T {
        val gson = GsonBuilder().create()
        return gson.fromJson(string, T::class.java)
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