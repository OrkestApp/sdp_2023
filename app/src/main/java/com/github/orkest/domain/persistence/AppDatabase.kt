package com.github.orkest.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AppEntities.Companion.UserEntity::class,
                AppEntities.Companion.SongEntity::class,
                AppEntities.Companion.PostEntity::class,
                AppEntities.Companion.ProfileEntity::class], // add more entities here
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songsDao(): AppDao.Companion.SongDao

    abstract fun postsDao(): AppDao.Companion.PostDao

    abstract fun profileDao(): AppDao.Companion.ProfileDao
}