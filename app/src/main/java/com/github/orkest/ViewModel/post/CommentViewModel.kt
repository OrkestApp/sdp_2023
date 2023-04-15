package com.github.orkest.ViewModel.post

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.github.orkest.Constants
import com.github.orkest.Model.*
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

open class CommentViewModel(postId: OrkestDate) {
    private val dbAPI = FireStoreDatabaseAPI()

    private val username = Constants.CURRENT_LOGGED_USER

    //======Mutable States=====
    private val comment = mutableStateOf(String())


    /*open fun getComments(): List<Comment> {
        return post.comments
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

    fun updateComments(comment: Comment) {
        post.comments.add(comment)
    }

    fun removeComment(id: OrkestDate): Boolean {
        return post.comments.removeIf { x: Comment -> x.date == id }
    }

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
    open fun getRecentPosts(lastConnected: LocalDateTime): CompletableFuture<List<Post>>{
        return dbAPI.getRecentPostsFromDataBase(lastConnected)
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
        post.username = username
        post.postDescription = postDescription.value.text
        post.song = song.value

        return post
    }

    /*open fun addComment(): CompletableFuture<Comment> {
        return dbAPI.addPostInDataBase(createComment())
    }
    public fun createComment(): Comment {
        return Comment(text="a")
    }*/
*/






}