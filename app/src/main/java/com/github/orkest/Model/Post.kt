package com.github.orkest.Model

data class Post(var username: String,
                var profilePicId: Int,
                var postDescription: String,
                var song: Song,
                var likes: Int,
                var comments: List<String>)

/* alternative:

data class Post(var username: String,
                var postId: Int,      // thanks to this we can search for comments in database
                var profilePicId: Int,
                var postDescription: String,
                var song: Song,
                var likes: Int,
                var numComments: Int,
                var comments: LazyList<String> // if possible
                )

 */