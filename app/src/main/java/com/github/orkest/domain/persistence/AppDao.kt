package com.github.orkest.domain.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.nio.charset.CodingErrorAction.REPLACE





class AppDao {

    companion object{

        // Add DAOs here
        @Dao
        interface UserDao{
            @Query("SELECT * FROM user")
            fun getAllStories(): LiveData<List<AppEntities.Companion.UserEntity?>?>?
        }

        @Dao
        interface SongDao{
            @Query("SELECT * FROM song")
            fun getAllSongs(): LiveData<List<AppEntities.Companion.SongEntity?>?>?

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            fun insertAll(vararg songs: AppEntities.Companion.SongEntity)

            @Query("DELETE FROM SongEntity")
            fun clear()
        }

        @Dao
        interface PostDao{

            @Query("SELECT * FROM Posts P WHERE P.date > 30days AND P.date > lastConnection")
            fun getRecentPosts()

            @Insert(AppEntities.Companion.PostEntitity::class)
            fun insertPosts(vararg posts: AppEntities.Companion.PostEntitity)
        }
    }
}