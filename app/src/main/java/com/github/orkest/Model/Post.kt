package com.github.orkest.Model

data class Post(var user: User,
                var postDescription: String,
                var song: Song,
                var likes: Int,
                var comments: List<String>)
