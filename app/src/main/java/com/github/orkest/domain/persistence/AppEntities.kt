package com.github.orkest.domain.persistence

import androidx.room.*
import com.github.orkest.data.OrkestDate
import com.github.orkest.data.Song
import com.google.gson.Gson

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
            val URL: String
        )

        @Entity(tableName = "posts")
        @TypeConverters(OrkestDateConverter::class, SongConverter::class, StringListConverter::class)
        data class PostEntity(
            @PrimaryKey(autoGenerate = true)
            val id: Int,
            val username: String,
            val date: OrkestDate,
            val profilePicId: Int,
            val postDescription: String,
            val song: Song,
            val nbLikes: Int,
            val likeList: MutableList<String>,
            val nbComments: Int,
            val media: String,
            val isMediaVideo: Boolean
        )
    }
}

// Define a type converter for MutableList<String>
class StringListConverter {
    @TypeConverter
    fun fromStringList(list: MutableList<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(string: String?): MutableList<String>? {
        return string?.split(",")?.toMutableList()
    }
}


// Define a type converter for the Song class
class SongConverter {
    @TypeConverter
    fun fromSong(song: Song?): String? {
        val gson = Gson()
        return gson.toJson(song)
    }

    @TypeConverter
    fun toSong(songJson: String?): Song? {
        val gson = Gson()
        return gson.fromJson(songJson, Song::class.java)
    }
}


// Define a type converter for the OrkestDate class
class OrkestDateConverter {
    @TypeConverter
    fun fromOrkestDate(date: OrkestDate?): String? {
        val gson = Gson()
        return gson.toJson(date)
    }

    @TypeConverter
    fun toOrkestDate(dateJson: String?): OrkestDate? {
        val gson = Gson()
        return gson.fromJson(dateJson, OrkestDate::class.java)
    }
}

