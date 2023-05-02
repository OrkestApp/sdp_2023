package com.github.orkest.domain

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.github.orkest.data.*
import com.github.orkest.data.Constants.Companion.DEFAULT_MAX_RECENT_DAYS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

open class FireStoreDatabaseAPI {

    companion object{
        val db = Firebase.firestore
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    //============================USER OPERATIONS==========================================

    /**
     * @param prefix the prefix of the username we want to search in the database
     * @return completable future of a list of User that match the prefix in their usernames
     *
     */
    fun fetchUserInDatabaseWithPrefix(prefix: String) : CompletableFuture<MutableList<User>>{
            val future = CompletableFuture<MutableList<User>>()

            if(prefix == ""){
                future.complete(mutableListOf())
                return future
            }
            db.collection("user/user-${prefix[0].uppercase()}/users")
                .get()
                .addOnSuccessListener { result ->

                    val list: MutableList<User> = mutableListOf()
                    for (document in result) {
                        val user = document.toObject(User::class.java)
                        if((user.username).startsWith(prefix)){
                            list.add(user)
                        }
                    }
                    //future that will be used by the view
                    future.complete(list)

                }
                .addOnFailureListener { exception ->
                    Log.d("HELLO", "Error getting documents: ", exception)
                    //failure does not append when the user is not find in the database, the returned collection will just be
                }
            return future

        }

    /**
     * @param username of the user's follow lists to display
     * @param isFollowersList is a boolean to indicate whether we want to fetch the followers list if true or the followings list if false
     *
     * @return the follow list indicated by the boolean isFollowersList of the user.
     */
    fun fetchFollowList(username: String, isFollowersList: Boolean): CompletableFuture<MutableList<String>>{
        val future = CompletableFuture<MutableList<String>>()

        getUserDocumentRef(username).get().addOnSuccessListener { document ->
            if(document != null && document.exists()){
                val user = document.toObject(User::class.java)
                if (user != null) { if (isFollowersList) future.complete(user.followers) else future.complete(user.followings) }
            }

        }.addOnFailureListener { e -> Log.d(TAG, "Error fetching follow lists", e) }
        return future
    }

    /**
     * @param username search a user with username
     * @return CompletableFuture of user, the User that match the username
     *
     * also assure that their is only one user that matches this username
     */
    fun searchUserInDatabase(username : String): CompletableFuture<User>{
        return fetchUserInDatabaseWithPrefix(username).thenCompose {
            val future = CompletableFuture<User>()
            if(it.size ==1) {
                future.complete(it[0])
            }
            else {
                future.completeExceptionally(java.lang.IllegalStateException("2 user with the same name in the database"))
            }
            future
        }


    }

    /**
     * @param user the User we want to add in the database
     * @return a completable fututure that completes to true, only if user is correctly add
     *
     * Assure that their is not a user with the same username already in the database
     */

    fun addUserInDatabase(user: User): CompletableFuture<Boolean>{
        val completableFuture = CompletableFuture<Boolean>()
        val document = getUserDocumentRef(user.username)

        document.get().addOnSuccessListener {
            if (it.data != null) {
                println(it)
                completableFuture.complete(false)
            } else {
                //If no user with the same username was found, add the user to the database
                document.set(user).addOnSuccessListener { completableFuture.complete(true) }
            }
        }
            //Propagates the exception in case of another exception
            .addOnFailureListener{
                completableFuture.completeExceptionally(it)
            }

        return completableFuture
    }

    /**
     * Check if user and mail are already in the database
     */
    fun userMailInDatabase(user: User): CompletableFuture<Boolean>{

        val auth = FirebaseAuth.getInstance()
        val future = CompletableFuture<Boolean>()

        //Checks if the database already contains a user with the same username and email
        getUserDocumentRef(user.username).get()
            .addOnSuccessListener {
                if (it.data != null && it.get("mail").toString() == auth.currentUser?.email.toString()) {
                    println(it)
                    future.complete(true)
                } else {
                    future.complete(false)
                }
            }
            //Propagates the exception in case of another exception
            .addOnFailureListener{
                future.completeExceptionally(it)
            }

        return future
    }

    /**
     * @param username
     * @return the DocumentReference of the user that match the username
     */
    fun getUserDocumentRef(username :String):DocumentReference{
        val firstLetter = username[0].uppercase()
        val path = "user/user-$firstLetter/users"
        return db.collection(path).document(username)
    }

    //===========================SONG POST OPERATIONS======================

    fun storeTokenInDatabase(username:String,token:String?): CompletableFuture<Boolean>{
        val completableFuture = CompletableFuture<Boolean>()
        val path = "deezerToken"
        db.collection(path).document(username).set(hashMapOf ("token" to token)).addOnSuccessListener {
            completableFuture.complete(true)
        }.addOnFailureListener{
            completableFuture.complete(false)
        }
        return completableFuture
    }

    fun getPostCollectionRef(username: String): CollectionReference{
        val firstLetter = username[0].uppercase()
        //TODO: Discuss other option: "posts/user-$firstLetter/$username"
        //Chose this for now because easier for group queries
        // But question: what happens when we get the docRef of one user
        val path = "user/user-$firstLetter/users/$username/posts"
        return db.collection(path)
    }


    /**
     * @param post the post we want to add in the database
     * @return a completable future that completes to true only if post is correctly added
     */
    fun addPostInDataBase(post: Post): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val postDocument = getPostCollectionRef(post.username).document(post.date.toString())

        postDocument.set(post).addOnSuccessListener { future.complete(true) }
            .addOnFailureListener { future.completeExceptionally(it) }

        return future
    }

    /**
     * @param post the post we want to add in the database
     * @param
     * @return a completable future that completes to true only if post is correctly added
     */
    fun addCommentInDataBase(username: String, post_date: String, comment: Comment): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val postDocument = getPostCollectionRef(username).document("${post_date}/comments/${comment.date}")

        postDocument.set(comment).addOnSuccessListener { future.complete(true) }
            .addOnFailureListener { future.completeExceptionally(it) }

        return future
    }


    /**
     * @param post the post we want to delete in the database
     * @return a completable future that completes to true only if post is correctly deleted
     */
    fun deletePostInDataBase(post: Post): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val postDocument = getPostCollectionRef(post.username).document(post.date.toString())

        postDocument.delete().addOnSuccessListener { future.complete(true) }
            .addOnFailureListener { future.completeExceptionally(it) }

        return future
    }


    /**
     * @param username the username of the user we want to get the posts from
     * @return a completable future of a list of post that match the username
     */
    fun getUserPostsFromDataBase(username: String): CompletableFuture<List<Post>>{
        val future = CompletableFuture<List<Post>>()
        val usersPosts = getPostCollectionRef(username)

        usersPosts.get().addOnSuccessListener{
            // Get all posts documents as a list of posts objects
            val list: MutableList<Post> =  it.toObjects(Post::class.java)

            future.complete(list)
        } //Propagates the exception in case of exception
        .addOnFailureListener{
            future.completeExceptionally(it)
        }

        return future
    }


    /**
     * @param us
     */
    fun getPostCommentsFromDataBase(post_username: String, post_date: String): CompletableFuture<List<Comment>>{
        val future = CompletableFuture<List<Comment>>()
        val usersPosts = getPostCollectionRef(post_username).document(post_date).collection("comments")

        usersPosts.get().addOnSuccessListener{
            // Get all posts documents as a list of posts objects
            val list: MutableList<Comment> =  it.toObjects(Comment::class.java)

            future.complete(list)
        } //Propagates the exception in case of exception
            .addOnFailureListener{
                future.completeExceptionally(it)
            }

        return future
    }


    private fun recentPostsQuery(month: Int, year: Int, day: Int): CompletableFuture<List<Post>>{
        val future = CompletableFuture<List<Post>>()
        val posts = db.collectionGroup("posts")
        posts.whereEqualTo(FieldPath.of("date", "year"), year)
            .whereEqualTo(FieldPath.of("date", "month"), month)
            .whereGreaterThanOrEqualTo(FieldPath.of("date", "dayOfMonth"), day)
            .get().addOnSuccessListener {
                // Get all posts documents as a list of posts objects
                val list: MutableList<Post> = it.toObjects(Post::class.java)
                //orders the list by date (most recent first)
                list.sortByDescending { post -> post.date }
                future.complete(list)
            } //Propagates the exception in case of exception
            .addOnFailureListener {
                future.completeExceptionally(it)
            }

        return future
    }


    //TODO: Add the condition on posts belonging to the user's friends
    /**
     * Returns the list of posts that were published after the last connection of the user
     * and from the last [maxDaysNb] days
     * @param lastConnection the last time the user was connected
     * @param maxDaysNb the max number of days we want to get the posts from, default and max value is 30
     * @return a completable future of a list of post that were recently published
     */
    fun getRecentPostsFromDataBase(lastConnection: LocalDateTime, maxDaysNb: Long = DEFAULT_MAX_RECENT_DAYS): CompletableFuture<List<Post>> {
        //Preconditions to check
        require(maxDaysNb <= DEFAULT_MAX_RECENT_DAYS) { "maxDaysNb must be less or equal to $DEFAULT_MAX_RECENT_DAYS" }

        val futures = mutableListOf<CompletableFuture<List<Post>>>()
        val maxDaysAgo = LocalDateTime.now().minusDays(maxDaysNb)
        val recentPostsTime = if (lastConnection.isAfter(maxDaysAgo)) lastConnection else maxDaysAgo

        val listMonths  = mutableListOf<Int>(recentPostsTime.monthValue)
        //If the recentPostsTime is not in the current month, we need to add the next month
        if (recentPostsTime.monthValue != LocalDateTime.now().monthValue)
            if (recentPostsTime.monthValue == 12)
                listMonths.add(1)
            else
                listMonths.add(recentPostsTime.monthValue+1)

        //Get all posts that were published after the recentPostsTime - One query per month
        listMonths.forEach { month ->
            run {
                futures.add(recentPostsQuery(month,
                    recentPostsTime.year,
                    if (month == recentPostsTime.monthValue) recentPostsTime.dayOfMonth else 1))
            }
        }
        //Transforms the list of futures into a future of list of posts
        return CompletableFuture.allOf(*futures.toTypedArray()).thenApply {
            val list = mutableListOf<Post>()
            futures.forEach { list.addAll(it.get()) }
            list
        }
    }

    /**
     * This method stores a shared song to the database of the receiver
     *
     * @param song the song we want to add in the database
     * @param receiver the receiver of the shared song
     *
     * @return a completable future that indicates that the song was correctly added
     */
    fun storeSharedSongToDataBase(song: Song, sender: String, receiver: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val sharedSongDocument1 =
            db.collection("shared songs")
                .document(receiver)
                .collection(sender)
                .document()

        sharedSongDocument1.set(song).addOnSuccessListener {
            future.complete(true)
        }.addOnFailureListener {
                future.completeExceptionally(it)
            }
        return future
    }

    /**
     * Fetch songs shared with current user from a sender
     * @param senderUsername the sender of the shared song
     *
     * @return a completable future of a list of post that were recently published
     */
    fun fetchSharedSongsFromDataBase(senderUsername: String, receiverUsername: String): CompletableFuture<List<Song>> {
        val future = CompletableFuture<List<Song>>()

        val sharedSongs = db.collection("shared songs").document(receiverUsername).collection(senderUsername)
        Log.d("DEBUG FETCH", "FETCHING")
        sharedSongs.get().addOnSuccessListener {

            // Get all posts documents as a list of posts objects
            val list: MutableList<Song> = it.toObjects(Song::class.java)
            Log.d("fetchSharedSongs", "list: $list")
            future.complete(list)
        } //Propagates the exception in case of exception
            .addOnFailureListener {
                future.completeExceptionally(it)
            }

        return future
    }

    //===========================LIKE POST OPERATIONS======================

    /**
     * Returns the set of users that liked this post
     */
    fun getPostLikeListFromDatabase(post_username: String, post_date: String): CompletableFuture<List<String>>{
        val future = CompletableFuture<List<String>>()
        val usersPosts = getPostCollectionRef(post_username).document(post_date).collection("likes")

        usersPosts.get().addOnSuccessListener{
            // Get all the usernames of the users that liked the post
            val list: MutableList<String> =  it.toObjects(String::class.java)
            future.complete(list)
        }
            .addOnFailureListener{
                future.completeExceptionally(it)
            }
        return future
    }


    /**
     * Get the number of likes of a post from the database
     * */
    fun getNbLikesForPostFromDatabase(post: Post): CompletableFuture<Int>{
        val future = CompletableFuture<Int>()
        getPostCollectionRef(post.username).document(post.date.toString()).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val post = document.toObject(Post::class.java)
                    if (post != null) {
                        future.complete(post.nbLikes)
                    }
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error getting post data", e)
                future.complete(0) }
        return future
    }


    /**
     * Returns whether or not the given user has already liked the post
     * */
    fun isUserInTheLikeList(post: Post, username: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        getPostCollectionRef(post.username).document(post.date.toString()).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val post = document.toObject(Post::class.java)
                    if (post != null) {
                        val list: MutableList<String> = post.likeList
                        if (list.contains(username)) { future.complete(true) }
                        else { future.complete(false) }
                    }
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error getting post data", e)
                future.complete(false) }
        return future
    }

    fun updatePostLikesInDatabase(post: Post, like: Boolean): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        getPostCollectionRef(post.username).document(post.date.toString()).get()
            .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val post = document.toObject(Post::class.java)
                if (post != null) {

                    if (like) {
                        post.nbLikes += 1
                        post.likeList.add(Constants.CURRENT_LOGGED_USER)
                    } else {
                        if (post.nbLikes > 0) post.nbLikes -= 1
                        post.likeList.remove(Constants.CURRENT_LOGGED_USER)
                    }

                    //Update database
                    getPostCollectionRef(post.username).document(post.date.toString()).update(
                        "nbLikes",
                        post.nbLikes,
                        "likeList",
                        post.likeList
                    )
                        .addOnSuccessListener { future.complete(true) }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error getting post likes data", e)
                            future.complete(false) }
                }
            }

        } .addOnFailureListener { e ->
            Log.w(TAG, "Error getting user data", e)
            future.complete(false)
        }

        return future
    }

}
