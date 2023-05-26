package com.github.orkest

import android.content.Intent
import android.util.Log
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.data.Constants
import com.github.orkest.data.Providers
import com.github.orkest.data.Song
import com.github.orkest.domain.persistence.AppDao
import com.github.orkest.domain.persistence.AppEntities
import com.github.orkest.ui.DeezerWelcomeActivity
import com.github.orkest.ui.sharing.Playlist
import com.github.orkest.ui.sharing.PlaylistViewModel
import com.github.orkest.ui.sharing.SharingComposeActivity
import com.github.orkest.ui.theme.OrkestTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaylistActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var testPlaylistViewModel: TestPlaylistViewModel
    private lateinit var testSong: Song

    // This is your test double
    class TestPlaylistViewModel: PlaylistViewModel(TestSongDao())

    @Before
    fun setUp() {
        testPlaylistViewModel = TestPlaylistViewModel()
        testSong = Song()

        // Set the content to the activity's layout
        composeTestRule.setContent {
            OrkestTheme {
                Playlist(
                    playlistViewModel = testPlaylistViewModel,
                    senderUsername = "Test Sender",
                    receiverUsername = "Test Receiver",
                    spotifySongId = "Test Spotify Song ID"
                )
            }
        }
    }

    /*@Test
    fun testSongListDisplay() {
        // Check that the song list is displayed
        composeTestRule.onNodeWithText("Test Song").assertIsDisplayed()
    }*/

    /**
     * Test addPostButton display
     */
    @Test
    fun testHomeButtonDisplay() {
        // Check that the home button is displayed
        composeTestRule.onNodeWithTag("addPostButton").assertIsDisplayed()
    }

    @Test
    fun testOnCreate(){
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SharingComposeActivity::class.java).apply {
            putExtra("code","dummy token")
            this.putExtra(Intent.EXTRA_TEXT,"I've found a song for you... Il nous faut by Elisa Tovati 🔥 Listen now on #Deezer https://deezer.page.link/qXgZb8DkHHuHZBet5")

            Log.d("intent test", this.getStringExtra(Intent.EXTRA_TEXT).toString())
        }
        val sauvegarde = Constants.CURRENT_USER_PROVIDER
        Constants.CURRENT_USER_PROVIDER = Providers.DEEZER
        val scenario = ActivityScenario.launch<SharingComposeActivity>(intent)
        scenario.close()
        Constants.CURRENT_USER_PROVIDER =sauvegarde
    }

    /**
     *  Test addPostButton click
     */
    @Test
    fun testHomeButtonClick() {
        // Perform a click on the home button
        composeTestRule.onNodeWithTag("addPostButton").performClick()

    }
}

/**
 * Mock SongDao for testing
 */
class TestSongDao : AppDao.Companion.SongDao {
    override fun getAllSongs(): List<AppEntities.Companion.SongEntity> {
        TODO("Not yet implemented")
    }

    override fun insertAll(vararg songs: AppEntities.Companion.SongEntity) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}