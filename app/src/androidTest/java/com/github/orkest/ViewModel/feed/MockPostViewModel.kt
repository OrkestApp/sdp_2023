package com.github.orkest.ViewModel.feed

import androidx.compose.ui.text.input.TextFieldValue
import com.github.orkest.data.Constants
import com.github.orkest.data.Comment
import com.github.orkest.data.OrkestDate
import com.github.orkest.data.Post
import com.github.orkest.ui.feed.PostViewModel
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

class MockPostViewModel : PostViewModel() {
    private val song = Constants.DUMMY_RUDE_BOY_SONG
    private val user = "DummyUser"
    private val description = TextFieldValue("DummyDescription")
    private val date = Constants.DUMMY_LAST_CONNECTED_TIME

    private val post = Post(username = user, date= OrkestDate(date), song= song, postDescription = description.text)
    private val comment = Comment(text="testing")
    val postComm = Post(username = user, date= OrkestDate(date), song= song, postDescription = description.text)

    override fun getSong() = song

    override fun getPost() = post

    override fun getPostDescription() = description

    fun updateSong(song: String) {}

    override fun updatePostDescription(description: TextFieldValue) {}

    override fun addPost(): CompletableFuture<Boolean>  {
        val future = CompletableFuture<Boolean>()
        future.complete(true)
        return future
    }

    override fun getRecentPosts(lastConnected : LocalDateTime, maxDaysNb: Long): CompletableFuture<List<Post>> {
        val future = CompletableFuture<List<Post>>()
        future.complete(listOf(post)) //, post, post))
        return future
    }
    override fun getUserPosts(username : String): CompletableFuture<List<Post>> {
        val future = CompletableFuture<List<Post>>()
        future.complete(listOf(post, post, post))
        return future
    }

    override fun updateComments(comment: Comment): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        future.complete(true)
        return future
    }

    override fun getComments(): CompletableFuture<List<Comment>> {
        val future = CompletableFuture<List<Comment>>()
        future.complete(listOf(comment))
        return future
    }

}