package com.github.orkest.ViewModel.post

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.github.orkest.Constants
import com.github.orkest.Constants.Companion.DEFAULT_MAX_RECENT_DAYS
import com.github.orkest.Model.*
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

open class PostViewModel {
    private val dbAPI = FireStoreDatabaseAPI()

    private val current_username = Constants.CURRENT_LOGGED_USER
    private var post: Post = Post()

    //======Mutable States=====
    private val postDescription = mutableStateOf(TextFieldValue())
    private val song = mutableStateOf(Song())
    //private val comments = mutableStateOf()

    private var post_username = ""
    private var post_date = ""

    open fun setPostUsername(usr: String) {
        post_username = usr
    }

    open fun setPostDate(date: String) {
        post_date = date
    }


    //TODO: Add a function to get the current user's profile picture when available

    /**
     * Returns the value of the post description
     */
    open fun getPostDescription(): TextFieldValue {
        return postDescription.value
    }

    /**
     * Returns the value of the song
     */
    open fun getSong(): Song {
        return song.value
    }


    /**
     * Updates the value of the post description after the user set it on the view
     */
    open fun updatePostDescription(description: TextFieldValue) {
        postDescription.value = description
    }

   /**
     * Updates the value of the song after the user set it on the view
     */
    fun updateSong(song: Song) {
        this.song.value = song
    }

    open fun getComments(): CompletableFuture<List<Comment>> {
        return dbAPI.getPostCommentsFromDataBase(post_username, post_date)
    }

    open fun updateComments(comment: Comment): CompletableFuture<Boolean> {
        return dbAPI.addCommentInDataBase(post_username, post_date, comment)
    }

    /*fun removeComment(comm: String): Boolean {
        return post.comments.removeIf { x: Comment -> x.date == id }
    }*/

    /**
     * Adds a post to the database
     */
    open fun addPost(): CompletableFuture<Boolean>{
        return dbAPI.addPostInDataBase(createPost())
    }

    /**
     * Returns a list of posts of the user @param username from the database
     */
    open fun getUserPosts(username: String = Constants.CURRENT_LOGGED_USER): CompletableFuture<List<Post>>{
        return dbAPI.getUserPostsFromDataBase(username)
    }

    /**
     * Returns a list of the most recent posts since the last connection of the user from the database
     */
    open fun getRecentPosts(lastConnected: LocalDateTime,
                            maxDaysNb: Long = DEFAULT_MAX_RECENT_DAYS ): CompletableFuture<List<Post>>{
        return dbAPI.getRecentPostsFromDataBase(lastConnected, maxDaysNb)
    }

    /**
     * Deletes a @param post from the database
     * Returns a future that completes to true if the post was deleted successfully
     */
    open fun deletePost(post: Post): CompletableFuture<Boolean>{
        return dbAPI.deletePostInDataBase(post)
    }

    /**
     * Returns the post object
     */
    fun getPost(): Post {
        return post
    }

    /**
     * Updates the post object to be added to the database
     */
    private fun createPost(): Post {
        post.username = current_username
        post.postDescription = postDescription.value.text
        post.date = OrkestDate(LocalDateTime.now())
        post.song = song.value

        return post
    }

}