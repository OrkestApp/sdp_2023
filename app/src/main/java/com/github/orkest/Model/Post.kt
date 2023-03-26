package com.github.orkest.Model

data class Post(var username: User,
                var song: Song,
                var date: String,
                var likes: Int,
                var comments: List<String>)
