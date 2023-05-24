package com.github.orkest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.data.OrkestDate
import com.github.orkest.data.Song
import com.github.orkest.domain.persistence.AppDao
import com.github.orkest.domain.persistence.AppDatabase
import com.github.orkest.domain.persistence.AppEntities
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersistenceTesting {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var userDao: MockUserDao
    private lateinit var songDao: MockSongDao
    private lateinit var postDao: MockPostDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()  // allowMainThreadQueries should generally be avoided in production.

        userDao = MockUserDao()
        songDao = MockSongDao()
        postDao = MockPostDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    /**
     * Test the DAOs for users
     */
    @Test
    fun testUserDao() = runBlocking {
        // Test getAllStories
        val users = listOf(
            AppEntities.Companion.UserEntity(1, "John Doe", "john@doe.com", "password"),
            AppEntities.Companion.UserEntity(2, "Jane Doe", "jane@doe.com", "password"),
            // Add more users if needed
        )
        users.forEach { userDao.addUser(it) }
        val liveData = userDao.getAllStories()
        liveData.observeForever { } // To ensure LiveData emits its data
        assertEquals(users, liveData.value)
    }

    /**
     * Test the DAOs for songs
     */
    @Test
    fun testSongDao() = runBlocking {
        // Test insertAll and getAllSongs
        val songs = listOf(
            AppEntities.Companion.SongEntity(1, "Song 1", "Artist 1", "Album 1", "URL 1"),
            AppEntities.Companion.SongEntity(2, "Song 2", "Artist 2", "Album 2", "URL 2"),
            // Add more songs if needed
        )
        songDao.insertAll(*songs.toTypedArray())
        val allSongs = songDao.getAllSongs()
        assertEquals(songs, allSongs)

        // Test clear
        songDao.clear()
        val allSongsAfterClear = songDao.getAllSongs()
        assertEquals(emptyList<AppEntities.Companion.SongEntity>(), allSongsAfterClear)
    }

    @Test
    fun testPostDao() = runBlocking {
        val posts = listOf(
            AppEntities.Companion.PostEntity(1, "user1", OrkestDate(1992,2,27), 0, "bio1", Song(), 14, mutableListOf("user1"), 2, "video", true ),
            AppEntities.Companion.PostEntity(2, "user2", OrkestDate(2003,3,28), 1, "bio2", Song("title1"), 2, mutableListOf("user2"), 0, "picture", false )
        )

        postDao.insertPosts(posts)
        val allPosts = postDao.getAllPosts()
        assertEquals(posts, allPosts)
    }
}

/**
 * Mock DAOs for testing for users
 */
class MockUserDao: AppDao.Companion.UserDao {
    private val userList = mutableListOf<AppEntities.Companion.UserEntity>()

    override fun getAllStories(): LiveData<List<AppEntities.Companion.UserEntity>> {
        return MutableLiveData(userList)
    }

    fun addUser(user: AppEntities.Companion.UserEntity) {
        userList.add(user)
    }
}

/**
 * Mock DAOs for testing for songs
 */
class MockSongDao: AppDao.Companion.SongDao {
    private val songList = mutableListOf<AppEntities.Companion.SongEntity>()

    override fun getAllSongs(): List<AppEntities.Companion.SongEntity> {
        return songList
    }

    override fun insertAll(vararg songs: AppEntities.Companion.SongEntity) {
        songList.addAll(songs)
    }

    override fun clear() {
        songList.clear()
    }
}

class MockPostDao: AppDao.Companion.PostDao{
    private val postList = mutableListOf<AppEntities.Companion.PostEntity>()

    override fun getAllPosts(): List<AppEntities.Companion.PostEntity> {
        return postList
    }

    override fun insertPosts(posts: List<AppEntities.Companion.PostEntity>) {
        postList.addAll(posts)
    }
}