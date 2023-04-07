package com.github.orkest.Model

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

data class Post(var username: String = "",
                var date: LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris")), 
                var profilePicId: Int = -1,
                var postDescription: String = "",
                var song: Song = Song(),
                var likes: Int = 0,
                var comments: List<String> = ArrayList())
