package com.github.orkest.ViewModel.feed

import androidx.compose.ui.text.input.TextFieldValue
import com.github.orkest.Constants
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.ViewModel.post.PostViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.time.LocalDateTime

class PostViewModelTest {


    companion object {

        private lateinit var postViewModel : PostViewModel
//        @BeforeClass
//        @JvmStatic
//        fun setupEmulator() {
//
//            val db = FireStoreDatabaseAPI.db
//            db.useEmulator("10.0.2.2", 8080)
//            db.firestoreSettings = firestoreSettings {
//                isPersistenceEnabled = false
//            }
//
//        }

        @BeforeClass
        @JvmStatic
        fun setup(){
            Constants.CURRENT_LOGGED_USER = "DummyUser"
            postViewModel = PostViewModel()
        }
    }




    @Test
    fun getPostsFromUserReturnsCorrectPosts() {

        postViewModel.updateSong(Constants.DUMMY_RUDE_BOY_SONG)
        postViewModel.updatePostDescription(TextFieldValue("Description"))
        postViewModel.addPost().get()

        val posts = postViewModel.getUserPosts("DummyUser").get()
        postViewModel.deletePost(posts[0]).get()
        assert(posts.size == 1)
        assert(posts[0].postDescription == "Description")
    }

    @Test
    fun getPostsFromUserReturnsEmptyList() {
        val posts = postViewModel.getUserPosts("DummyUser2").get()
        assert(posts.isEmpty())
    }

    @Test
    fun getRecentPostsReturnsCorrectPosts() {
        Constants.CURRENT_LOGGED_USER = "DummyUser"
        postViewModel.updateSong(Constants.DUMMY_RUDE_BOY_SONG)
        postViewModel.updatePostDescription(TextFieldValue("Recent1"))
        postViewModel.addPost().get()


        val posts = postViewModel.getRecentPosts(Constants.DUMMY_LAST_CONNECTED_TIME).get()

        postViewModel.deletePost(posts[0]).get()

        assert(posts.size == 1)
        assert(posts[0].postDescription == "Recent1")
    }

    @Test
    fun getAfterDeleteReturnsEmptyList() {
        Constants.CURRENT_LOGGED_USER = "DummyUser"
        postViewModel.updatePostDescription(TextFieldValue("ToDelete"))
        postViewModel.deletePost(postViewModel.getPost()).get()
        val posts = postViewModel.getRecentPosts(Constants.DUMMY_LAST_CONNECTED_TIME).get()
        assert(posts.isEmpty())
    }

    @Test
    fun getRecentPostsUpToDateUserReturnsEmptyList() {
        val posts = postViewModel.getRecentPosts(LocalDateTime.now()).get()
        assert(posts.isEmpty())
    }

    @Test
    fun addPostReturnsTrue() {
        Constants.CURRENT_LOGGED_USER = "DummyUser"
        postViewModel.updatePostDescription(TextFieldValue("Description3"))
        assert(postViewModel.addPost().get())
        postViewModel.deletePost(postViewModel.getPost()).get()
    }


}