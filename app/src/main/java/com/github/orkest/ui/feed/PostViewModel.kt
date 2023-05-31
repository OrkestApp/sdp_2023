package com.github.orkest.ui.feed

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import com.github.orkest.data.*
import com.github.orkest.data.Constants.Companion.DEFAULT_MAX_RECENT_DAYS
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.shazam.domain.ShazamConstants
import com.github.orkest.ui.notification.Notification
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

    // Likes variables
    open val isPostLiked = MutableLiveData<Boolean>()


    /**==============like functions===================*/

    /**
     * Returns whether or not the current user has already liked the post
     * */
    open fun isPostLiked(post: Post): CompletableFuture<Boolean>{
        return dbAPI.isUserInTheLikeList(post, current_username)
    }

    /**
     * This function updates the nbLikes and likeList of the given post depending on whether the current user wants to like or dislike the post
     */
    open fun updatePostLikes(post: Post, like: Boolean): CompletableFuture<Boolean> {
        return dbAPI.updatePostLikesInDatabase(post, like)
    }

    /**
     * Gets the number of likes for the given post
     */
    open fun getNbLikes(post: Post): CompletableFuture<Int>{
        return dbAPI.getNbLikesForPostFromDatabase(post)
    }

    /**
     * Gets the like list for the given post
     */
    open fun getLikeList(post: Post): CompletableFuture<MutableList<String>>{
        return dbAPI.getLikeList(post)
    }

    /**===============================================*/

    open fun setPostUsername(usr: String) {
        post_username = usr
    }

    open fun setPostDate(date: String) {
        post_date = date
    }

    open fun setPostMedia(media: String, isVideo: Boolean){
        post.media = media
        post.isMediaVideo = isVideo
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
    open fun updateSong(song: Song) {
        this.song.value = song
    }

    /**==============comment functions===================*/

    open fun getComments(): CompletableFuture<List<Comment>> {
        return dbAPI.getPostCommentsFromDataBase(post_username, post_date)
    }

    open fun updateComments(comment: Comment): CompletableFuture<Boolean> {
        return dbAPI.addCommentInDataBase(post_username, post_date, comment)
    }

    /*fun removeComment(comm: String): Boolean {
        return post.comments.removeIf { x: Comment -> x.date == id }
    }*/

    /**===============================================*/

    open fun resetFoundShazamSong() {
        ShazamConstants.SONG_FOUND = ShazamConstants.SONG_NO_MATCH
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
    open fun getPost(): Post {
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