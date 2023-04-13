package com.github.orkest.Model

import com.github.orkest.Constants
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

data class Post(var username: String = "",
                var date: LocalDateTime = LocalDateTime.now(Constants.DB_ZONE_ID),
                var profilePicId: Int = -1,
                var postDescription: String = "Post Description",
                var song: Song = Song(),
                var likes: Int = 0,
                var comments: List<String> = ArrayList(),
                var nbComments : Int = 0)
