package com.github.orkest.ViewModel.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.orkest.Constants
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

open class ProfileViewModel(private val user: String) : ViewModel() {

    var db : FirebaseFirestore = Firebase.firestore
    private var userProfile = User()

    open var username = MutableLiveData<String>()
    open var bio = MutableLiveData<String>()
    open var nbFollowers = MutableLiveData<Int>()
    open var nbFollowings = MutableLiveData<Int>()
    open var profilePictureId = MutableLiveData<Int>()
    open val isUserFollowed = MutableLiveData<Boolean>()

    lateinit var future : CompletableFuture<Profile>

    init{ setupListener() }

    /**
     * To call everytime an instance of ProfileViewModel() is created
     */
    fun setupListener(){

        future= loadUserData()
        future.thenAccept{
            username.value= it.username
            bio.value= it.bio
            nbFollowers.value = it.nbFollowers
            nbFollowings.value = it.nbFollowings
            //profilePictureId.value = it.profilePictureId
        }

        listenToUserData()

        isUserFollowed().thenAccept {
            isUserFollowed.value = it
        }
    }

    fun userDocument(username: String) : DocumentReference {
        val firstLetter = username[0].uppercase()
        val path = "user/user-$firstLetter/users"
        return db.collection(path).document(username)
    }

    /**
     * Fetches data from the Firestore document and sets the profile values
     */
    private fun loadUserData() : CompletableFuture<Profile>{
        val futureProfile =CompletableFuture<Profile>()
        userDocument(user).get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val user = document.toObject(User::class.java)
                if (user != null) {
                    userProfile.profile = user.profile
                    futureProfile.complete(user.profile)
                }
            }
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error getting user data", e)
        }
        return futureProfile
    }


    /**
     * Listens to the changes in the Firestore document profile_data
     * and updates the view-model's values
     */
     private fun listenToUserData(){
        userDocument(user).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if(user != null){
                    userProfile.profile = user.profile
                }
            } else { Log.d(TAG, "Current data: null") }
        }
    }

    /**
     * Function only called when visiting the profile of another user
     * Checks whether the current logged in user follows this account
     */
    open fun isUserFollowed(): CompletableFuture<Boolean>{
        if(user == Constants.currentLoggedUser){
            Log.e(TAG, "Cannot call this function when visiting the current logged-in user's profile", IllegalArgumentException())
        }
        val future = CompletableFuture<Boolean>()
        userDocument(user).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null && user.followers.contains(Constants.currentLoggedUser)) {
                        future.complete(true)
                    } else{
                        future.complete(false)
                    }
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error getting user data", e) }
        return future
    }

    open fun follow(): CompletableFuture<Boolean>{
        val futureFollow =CompletableFuture<Boolean>()

        val userUpdated = updateUser(toFollow = true)
        val currentUserUpdated = updateCurrentUser(toFollow = true)

        //Both updates must be successful
        futureFollow.complete(userUpdated && currentUserUpdated)
        return futureFollow
    }

    open fun unfollow(): CompletableFuture<Boolean>{
        val futureFollow = CompletableFuture<Boolean>()

        val userUpdated = updateUser(toFollow = false)
        val currentUserUpdated = updateCurrentUser(toFollow = false)

        //Both updates must be successful
        futureFollow.complete(userUpdated && currentUserUpdated)
        return futureFollow
    }

    /**
     * Updates the user's followers' list
     * toFollow: Boolean = represents whether or not the current logged in user wants to follow this account
     */
    private fun updateUser(toFollow: Boolean): Boolean{
        if(user == Constants.currentLoggedUser){
            Log.e(TAG, "Cannot call this function when visiting the current logged-in user's profile", IllegalArgumentException())
        }
        var userUpdated = false
        userDocument(user).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        if(toFollow){
                            user.profile.nbFollowers += 1
                            user.followers.add(Constants.currentLoggedUser)
                            userUpdated = true
                        } else {
                            if (user.profile.nbFollowers > 0) user.profile.nbFollowers -= 1
                            user.followers.remove(Constants.currentLoggedUser)
                            userUpdated = true
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
            }
        return userUpdated
    }

    /**
     * Updates the current logged-in user's followings' list
     * toFollow: Boolean = represents whether or not the current logged in user wants to follow this account
     */
    private fun updateCurrentUser(toFollow: Boolean): Boolean{
        if(user == Constants.currentLoggedUser){
            Log.e(TAG, "Cannot call this function when visiting the current logged-in user's profile", IllegalArgumentException())
        }
        var currentUserUpdated = false
        userDocument(Constants.currentLoggedUser).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        if(toFollow){
                            user.profile.nbFollowings += 1
                            user.followings.add(username.toString())
                            currentUserUpdated = true
                        } else {
                            if (user.profile.nbFollowings > 0) user.profile.nbFollowings -= 1
                            user.followings.remove(username.toString())
                            currentUserUpdated = true
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting current user data", e)
            }
        return currentUserUpdated
    }

}
