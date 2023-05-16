package com.github.orkest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.github.orkest.data.*
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PlaySpotifyTest {

    /**
     * Test that the Spotify App Remote is setup correctly
     */
    @Test
    fun testSetupSpotifyAppRemote() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        PlaySpotify.setupSpotifyAppRemote(context)
    }

    /**
     * Test that the Deezer Informations are returned correctly
     */
    @Test
    fun testDeezerInformations() {
        val deezerInformations = DeezerInformations("access_token_1", "user_id_1", "playlist_id_1")

        assertEquals("access_token_1", deezerInformations.access_token)
        assertEquals("user_id_1", deezerInformations.userId)
        assertEquals("playlist_id_1", deezerInformations.playlistId)
    }
}
