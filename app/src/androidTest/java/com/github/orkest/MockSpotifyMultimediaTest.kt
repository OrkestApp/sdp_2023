package com.github.orkest

import com.github.orkest.data.SpotifyMultimedia
import org.junit.Before
import org.junit.Test

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
        val spotifyMultimedia = SpotifyMultimedia()
        val albumUri = spotifyMultimedia.getAlbumCoverImageUrl("6QPkyl04rXwTGlGlcYaRoW" , accessToken)
        assert(albumUri.get() == "Placeholder URL")
    }

    /**
     * Test that the artist image URL is returned correctly
     */
    @Test
    fun getArtistImageTest() {
        val spotifyMultimedia = SpotifyMultimedia()
        val artistImage = spotifyMultimedia.getArtistImage("0TnOYISbd1XYRBk9myaseg" , accessToken)
        assert(artistImage.get() == "Placeholder URL")
    }
}