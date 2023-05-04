package com.github.orkest.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AppEntities.Companion.UserEntity::class, AppEntities.Companion.SongEntity::class], // add more entities here
    version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songsDao(): AppDao.Companion.SongDao
    

}