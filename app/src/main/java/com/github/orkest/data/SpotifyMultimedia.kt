package com.github.orkest.data

import android.util.Log
import com.github.orkest.ui.sharing.SharingComposeActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture

/*
    This class is used to provide an interface to
    access spotify multimedia data (images, videos, etc)
    from the spotify API.
 */
class SpotifyMultimedia {

    // This function is used to get the album cover image
    fun getAlbumCoverImageUrl(albumId: String, accessToken: String, testResponse: Response?): CompletableFuture<String> {

        val future = CompletableFuture<String>()
        val client = OkHttpClient()

        // construct a request containing the URI and the access token
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/albums/$albumId")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        // send the request to the Spotify API
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                future.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {

                var mainResponse = response
                if (!response.isSuccessful) {
                    if (response.code == 401) {
                        if (testResponse != null) {
                            mainResponse = testResponse
                        }
                    } else {
                        future.completeExceptionally(Exception("Unexpected code ${response.code}"))
                        return
                    }
                }

                val jsonResponse = mainResponse.body?.string()
                val jsonObject = jsonResponse?.let { JSONObject(it) }
                val imagesArray = jsonObject?.getJSONArray("images")
                val url = imagesArray?.getJSONObject(0)?.getString("url")
                Log.d("Spotify multimedia", "Album")
                SharingComposeActivity.songName = url.toString()
                Log.d("Spotify multimedia", "Album: $url")
                future.complete(url)


            }
        })

        return future
    }

    // This function is used to get the artist image
    fun getArtistImage(artistId: String, accessToken: String, testResponse: Response?): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val client = OkHttpClient()

        // construct a request containing the URI and the access token
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/artists/$artistId")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        // send the request to the Spotify API
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                future.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {

                var mainResponse = response

                if (!response.isSuccessful) {
                    if (response.code == 401) {
                        if (testResponse != null) {
                            mainResponse = testResponse
                        }
                    } else {
                        future.completeExceptionally(Exception("Unexpected code ${response.code}"))
                        return
                    }
                }

                val jsonResponse = mainResponse.body?.string()
                val jsonObject = jsonResponse?.let { JSONObject(it) }
                val imagesArray = jsonObject?.getJSONArray("images")
                val url = imagesArray?.getJSONObject(0)?.getString("url")
                Log.d("Spotify multimedia", "Artist")
                SharingComposeActivity.songName = url.toString()
                Log.d("Spotify multimedia", "Artist: $url")
                future.complete(url)


            }
        })

        return future
    }


}