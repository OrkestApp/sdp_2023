package com.github.orkest.ViewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.platform.app.InstrumentationRegistry
import com.github.orkest.data.Song
import com.github.orkest.domain.persistence.AppDao
import com.github.orkest.domain.persistence.AppEntities
import com.github.orkest.ui.sharing.PlaylistViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SongCachingTest {

    private lateinit var viewModel: PlaylistViewModel

    @Mock
    private lateinit var dao: AppDao.Companion.SongDao

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        // Create a new instance of the ViewModel with the mock DAO injected
        viewModel = PlaylistViewModel(dao)

        // Set up the context for the tests
        context = InstrumentationRegistry.getInstrumentation().targetContext

    }

    @Test
    fun fetchDataFromCacheWhenOffline() {
        // Set up the mock DAO to return cached data
        val cachedData = listOf(AppEntities.Companion.SongEntity(0, "name", "artist", "album", "url"))
        `when`(dao.getAllSongs()).thenReturn(cachedData)

        // Set up the context to be offline
        // Create a mock ConnectivityManager object
        val connectivityManager = mock(ConnectivityManager::class.java)

        // Create a NetworkInfo object to represent a disconnected network
        val networkInfo = mock(NetworkInfo::class.java)
        `when`(networkInfo.isConnected).thenReturn(false)

        // Set up the mock ConnectivityManager to return the disconnected NetworkInfo
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        val context = mock(Context::class.java)


        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)


        // Call the ViewModel function to fetch data
        val result = viewModel.fetchSongs("sender", "receiver", context)

        // Verify that the result is the cached data
        assertEquals(Song(cachedData[0].name, cachedData[0].Artist, cachedData[0].Album, cachedData[0].URL), result.get()[0])
    }

}