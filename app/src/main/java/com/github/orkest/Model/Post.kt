package com.github.orkest.Model

data class Post(var username: String,
                var profilePicId: Int,
                var postDescription: String,
                var song: Song,
                var likes: Int,
                var comments: List<String>)
