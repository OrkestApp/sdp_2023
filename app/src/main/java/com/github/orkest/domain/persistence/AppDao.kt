package com.github.orkest.domain.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query



class AppDao {

    companion object{

        // Add DAOs here
        @Dao
        interface UserDao{
            @Query("SELECT * FROM user")
            fun getAllStories(): LiveData<List<AppEntities.Companion.UserEntity>>
        }

        @Dao
        interface SongDao{
            @Query("SELECT * FROM SongEntity")
            fun getAllSongs(): List<AppEntities.Companion.SongEntity>

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            fun insertAll(vararg songs: AppEntities.Companion.SongEntity)

            @Query("DELETE FROM SongEntity")
            fun clear()
        }

        @Dao
        interface PostDao {
            @Query("SELECT * FROM posts")
            fun getAllPosts(): List<AppEntities.Companion.PostEntity>

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            fun insertPosts(posts: List<AppEntities.Companion.PostEntity>)
        }

        @Dao
        interface ProfileDao {
            @Query("SELECT * FROM profile WHERE id = 1")
            fun getProfile(): AppEntities.Companion.ProfileEntity?

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            fun insertProfile(profile: AppEntities.Companion.ProfileEntity)
        }

    }
}