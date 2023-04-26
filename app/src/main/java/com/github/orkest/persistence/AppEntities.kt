package com.github.orkest.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    }
}