package com.github.orkest.ViewModel.post

import androidx.compose.runtime.mutableStateOf
import com.github.orkest.Constants
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Post
import com.github.orkest.Model.Song
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

class PostViewModel {
    private val dbAPI = FireStoreDatabaseAPI()

    private val username = Constants.CURRENT_LOGGED_USER

    //======Mutable States=====
    private val postDescription = mutableStateOf("")
    private val song = mutableStateOf(Song())

    //TODO: Add a function to get the current user's profile picture when available

    /**
     * Updates the value of the post description after the user set it on the view
     */
    fun updatePostDescription(description: String) {
        postDescription.value = description
    }

   /**
     * Updates the value of the song after the user set it on the view
     */
    fun updateSong(song: Song) {
        this.song.value = song
    }

    /**
     * Adds a post to the database
     */
    fun addPost(): CompletableFuture<Boolean>{
        return dbAPI.addPostInDataBase(createPost())
    }

    /**
     * Returns a list of posts of the user @param username from the database
     */
    fun getUserPosts(): CompletableFuture<List<Post>>{
        return dbAPI.getUserPostsFromDataBase(username)
    }

    /**
     * Returns a list of the most recent posts since the last connection of the user from the database
     */
    fun getRecentPosts(lastConnected: LocalDateTime): CompletableFuture<List<Post>>{
        return dbAPI.getRecentPostsFromDataBase(lastConnected)
    }

    /**
     * Updates and creates the post object to be added to the database
     */
    private fun createPost(): Post {
        val post = Post()
        post.username = username
        post.postDescription = postDescription.value
        post.song = song.value

        return post
    }







}