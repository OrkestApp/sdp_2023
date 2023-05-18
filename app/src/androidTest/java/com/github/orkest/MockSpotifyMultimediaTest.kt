package com.github.orkest

import com.github.orkest.data.SpotifyMultimedia
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test

/**
 * Test class for SpotifyMultimedia
 */
class MockSpotifyMultimediaTest {

    private var accessToken = "random"       // access token for Spotify API
    private lateinit var spotifyMultimedia: SpotifyMultimedia

    @Before
    fun setup() {
        spotifyMultimedia = SpotifyMultimedia()
    }

    /**
     * Test that the album cover image URL is returned correctly
     */
    @Test
    fun getAlbumUriTest() {

        val responseBody = """
            {
              "images": [
                {
                  "url": "http://example.com/image.jpg"
                }
              ]
            }
            """.trimIndent()

        val mediaType = "application/json".toMediaTypeOrNull()
        val body = responseBody.toResponseBody(mediaType)

        val testResponse = Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("http://localhost/").build())
            .protocol(Protocol.HTTP_1_1)
            .body(body)
            .build()


        val spotifyMultimedia = SpotifyMultimedia()
        val albumUri = spotifyMultimedia.getAlbumCoverImageUrl("6QPkyl04rXwTGlGlcYaRoW" , accessToken, testResponse)
        assert(albumUri.get() == "http://example.com/image.jpg")
    }

    /**
     * Test that the artist image URL is returned correctly
     */
    @Test
    fun getArtistImageTest() {

        val responseBody = """
            {
              "images": [
                {
                  "url": "http://example.com/image.jpg"
                }
              ]
            }
            """.trimIndent()

        val mediaType = "application/json".toMediaTypeOrNull()
        val body = responseBody.toResponseBody(mediaType)

        val testResponse = Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("http://localhost/").build())
            .protocol(Protocol.HTTP_1_1)
            .body(body)
            .build()

        val spotifyMultimedia = SpotifyMultimedia()
        val artistImage = spotifyMultimedia.getArtistImage("0TnOYISbd1XYRBk9myaseg" , accessToken, testResponse)
        assert(artistImage.get() == "http://example.com/image.jpg")
    }
}