package com.github.orkest.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query


class AppDao {

    companion object{

        // Add DAOs here
        @Dao
        interface UserDao{
            @Query("SELECT * FROM user")
            fun getAllStories(): LiveData<List<AppEntities.Companion.UserEntity?>?>?
        }
    }
}