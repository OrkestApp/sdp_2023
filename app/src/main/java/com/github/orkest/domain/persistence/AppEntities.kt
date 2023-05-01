package com.github.orkest.domain.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.orkest.data.OrkestDate
import com.github.orkest.data.Song

class AppEntities {

    companion object{

        // Add entities here

        @Entity
        data class UserEntity(
            @PrimaryKey val id: Int,
            @ColumnInfo(name = "name") val name: String,
            val email: String,
            val password: String
        )

        @Entity
        data class SongEntity(
            @PrimaryKey val id: Int,
            @ColumnInfo(name = "Title") val name: String,
            val Artist: String,
            val Album: String,
            val URL: Int
        )

        @Entity
        data class PostEntitity(
            @PrimaryKey val date: OrkestDate,
            @PrimaryKey val user: String,
            var postDescription: String = "Post Description",
            var song: Song = Song(),
            var likes: Int = 0,
            var nbComments : Int = 0
        )
    }
}