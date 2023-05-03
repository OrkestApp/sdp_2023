package com.github.orkest

import android.util.Log
import androidx.test.core.app.launchActivity
import com.github.orkest.data.SpotifyMultimedia
import com.github.orkest.ui.sharing.SharingComposeActivity
import org.junit.Before
import org.junit.Test

class SpotifyMultimediaTest {

    private var accessToken : String = String()        // access token for Spotify API
    private var authorizationCode : String = String()  // authorization code for token request

    private val scenario = launchActivity<SharingComposeActivity>()

    @Before
    fun setup(){
        // sleep for 5 seconds
        Thread.sleep(5000)
        scenario.onActivity {
            this.accessToken = it.accessToken
            Log.d("SPOTIFY TOKEN", this.accessToken)
        }
    }

    @Test
    fun getAlbumUriTest() {
        val spotifyMultimedia = SpotifyMultimedia()
        val albumUri = spotifyMultimedia.getAlbumCoverImageUrl("6QPkyl04rXwTGlGlcYaRoW" , this.accessToken)
        assert(albumUri.get() == "https://i.scdn.co/image/ab67616d0000b273346a5742374ab4cf9ed32dee")
    }

    @Test
    fun getArtistImageTest() {
        val spotifyMultimedia = SpotifyMultimedia()
        val artistImage = spotifyMultimedia.getArtistImage("0TnOYISbd1XYRBk9myaseg" , this.accessToken)
        assert(artistImage.get() == "https://i.scdn.co/image/ab6761610000e5ebfc9d2abc85b6f4bef77f80ea")
    }


}