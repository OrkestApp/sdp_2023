package com.github.orkest.ui.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.orkest.data.Constants
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.data.User
import com.github.orkest.domain.FirebaseStorageAPI
import com.google.android.gms.tasks.Task
import java.io.Serializable
import java.util.concurrent.CompletableFuture

open class ProfileViewModel(val user: String) : ViewModel(), Serializable {

    private val dbAPI = FireStoreDatabaseAPI()
    private val firebaseStorage = FirebaseStorageAPI
    private var userProfile = User()

    val storageAPI = FirebaseStorageAPI()

    open var username = MutableLiveData(user)
    open var bio = MutableLiveData<String>()
    open var nbFollowers = MutableLiveData<Int>()
    open var nbFollowings = MutableLiveData<Int>()
    open val isUserFollowed = MutableLiveData<Boolean>()
    open var profilePictureId = MutableLiveData<Int>()
    open var profilePic = MutableLiveData<ByteArray?>()

    private lateinit var future: CompletableFuture<User>


    init {
        setupListener()
    }

    /**
     * To call everytime an instance of ProfileViewModel() is created
     */
    fun setupListener() {

        //future= loadUserData()
        future = dbAPI.searchUserInDatabase(user)
        future.thenAccept {
            username.value = it.username
            bio.value = it.profile.bio
            nbFollowers.value = it.profile.nbFollowers
            nbFollowings.value = it.profile.nbFollowings
            //profilePictureId.value = it.profilePictureId
        }



        listenToUserData()

        isUserFollowed().thenAccept {
            isUserFollowed.value = it
        }
    }

    /**
     * Get profile picture of the user whose profile you're currently visiting
     */
    fun fetchProfilePic(): CompletableFuture<ByteArray> {
        return firebaseStorage.fetchProfilePic(user)
    }


    /**
     * Listens to the changes in the Firestore document profile_data
     * and updates the view-model's values
     */
    private fun listenToUserData() {
        dbAPI.getUserDocumentRef(user).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    username.value = user.username
                    bio.value = user.profile.bio
                    nbFollowers.value = user.profile.nbFollowers
                    nbFollowings.value = user.profile.nbFollowings
                    //profilePictureId.value = it.profilePictureId
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }


    /** ===== Functions only called when visiting the profile of another user ===== */
    /** ======== This condition is set on the ProfileTopInterface function ======== */

    /**
     * Function only called when visiting the profile of another user
     * Checks whether the current logged in user follows this account
     */
    open fun isUserFollowed(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        dbAPI.getUserDocumentRef(user).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null && user.followers.contains(Constants.CURRENT_LOGGED_USER)) {
                        future.complete(true)
                    } else {
                        future.complete(false)
                    }
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error getting user data", e) }
        return future
    }


    /**
     * toFollow: Boolean = states whether we should add the current user or retrieve it from the user's followers' list
     * Update the followers' list of the visited profile
     * The multiple future.complete allow to reduce the complexity of the function and make sure to handle all the errors
     */
    open fun updateUserFollowers(toFollow: Boolean): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        val userRef = dbAPI.getUserDocumentRef(user)
        userRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val user = document.toObject(User::class.java)
                if (user != null) {

                    // Chooses which operation to apply on the user
                    if (toFollow) {
                        user.profile.nbFollowers += 1
                        user.followers.add(Constants.CURRENT_LOGGED_USER)
                    } else {
                        if (user.profile.nbFollowers > 0) user.profile.nbFollowers -= 1
                        user.followers.remove(Constants.CURRENT_LOGGED_USER)
                    }
                    future.complete(true)

                    //Updates the database
                    userRef.update(
                        "profile.nbFollowers",
                        user.profile.nbFollowers,
                        "followers",
                        user.followers
                    )
                        .addOnSuccessListener { future.complete(true) }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating user data", e)
                            future.complete(false)
                        }
                } else { future.complete(false) }
            } else { future.complete(false) }
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
                future.complete(false)
            }
        return future
    }

    /**
     * toFollow: Boolean = states whether we should add the visited user or retrieve it from the current user's followings' list
     * Update the followings' list of the current logged in user
     */
    open fun updateCurrentUserFollowings(toFollow: Boolean): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        val currentUserRef = dbAPI.getUserDocumentRef(Constants.CURRENT_LOGGED_USER)
        currentUserRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val user = document.toObject(User::class.java)
                if (user != null) {

                    // Chooses which operation to apply on the user
                    if (toFollow) {
                        user.profile.nbFollowings += 1
                        user.followings.add(this.user)
                    } else {
                        if (user.profile.nbFollowings > 0) user.profile.nbFollowings -= 1
                        user.followings.remove(this.user)
                    }

                    // Updates the database
                    currentUserRef.update(
                        "profile.nbFollowings",
                        user.profile.nbFollowings,
                        "followings",
                        user.followings
                    )
                        .addOnSuccessListener { future.complete(true) }
                        .addOnFailureListener { e -> Log.w(TAG, "Error updating user data", e)
                            future.complete(false)
                        }
                } else { future.complete(false) }
            } else { future.complete(false) }
        }
            .addOnFailureListener { e -> Log.w(TAG, "Error getting user data", e)
                future.complete(false)
            }
        return future
    }
}

