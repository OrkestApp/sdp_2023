package com.github.orkest.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.orkest.Model.User

@Database(
    entities = [AppEntities.Companion.UserEntity::class], // add more entities here
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    

}