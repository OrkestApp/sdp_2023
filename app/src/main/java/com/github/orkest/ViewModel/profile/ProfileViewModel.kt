package com.github.orkest.ViewModel.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.orkest.Constants
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

open class ProfileViewModel(private val user: String) : ViewModel() {

    private val dbAPI = FireStoreDatabaseAPI()
    private var userProfile = User()

    open var username = MutableLiveData<String>()
    open var bio = MutableLiveData<String>()
    open var nbFollowers = MutableLiveData<Int>()
    open var nbFollowings = MutableLiveData<Int>()
    open var profilePictureId = MutableLiveData<Int>()
    open val isUserFollowed = MutableLiveData<Boolean>()

     private lateinit var future : CompletableFuture<User>



    init{
        setupListener()
    }

    /**
     * To call everytime an instance of ProfileViewModel() is created
     */
    fun setupListener(){

        //future= loadUserData()
        future = dbAPI.searchUserInDatabase(user)
        future.thenAccept{
            username.value= it.username
            bio.value= it.profile.bio
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
     * Listens to the changes in the Firestore document profile_data
     * and updates the view-model's values
     */
     private fun listenToUserData(){

        dbAPI.getUserDocumentRef(user).addSnapshotListener { snapshot, e ->
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
        if(user == Constants.currentLoggedUser){ Log.e(TAG, "Cannot call this function when visiting the current logged-in user's profile")  }
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

        val userUpdated = accessUserData(toFollow = true)
        val currentUserUpdated = accessCurrentUserData(toFollow = true)

        //Both updates must be successful
        futureFollow.complete(userUpdated && currentUserUpdated)
        return futureFollow
    }

    open fun unfollow(): CompletableFuture<Boolean>{
        val futureFollow = CompletableFuture<Boolean>()

        val userUpdated = accessUserData(toFollow = false)
        val currentUserUpdated = accessCurrentUserData(toFollow = false)

        //Both updates must be successful
        futureFollow.complete(userUpdated && currentUserUpdated)
        return futureFollow
    }

    /**
     * Updates the user's followers' list
     * toFollow: Boolean = represents whether or not the current logged in user wants to follow this account
     */
    private fun accessUserData(toFollow: Boolean): Boolean{
        if(user == Constants.currentLoggedUser){ Log.e(TAG, "Cannot call this function when visiting the current logged-in user's profile") }
        var userUpdated = false
        userDocument(user).get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        userUpdated = updateUserFollowers(user, toFollow)
                    }
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error getting user data", e) }
        return userUpdated
    }

    private fun updateUserFollowers(user: User, toFollow: Boolean): Boolean {
        if(toFollow){
            user.profile.nbFollowers += 1
            user.followers.add(Constants.currentLoggedUser)
        } else {
            if (user.profile.nbFollowers > 0) user.profile.nbFollowers -= 1
            user.followers.remove(Constants.currentLoggedUser)
        }
        return true //means that the values have been updated
    }

    /**
     * Updates the current logged-in user's followings' list
     * toFollow: Boolean = represents whether or not the current logged in user wants to follow this account
     */
    private fun accessCurrentUserData(toFollow: Boolean): Boolean{
        if(user == Constants.currentLoggedUser){ Log.e(TAG, "Cannot call this function when visiting the current logged-in user's profile") }
        var currentUserUpdated = false
        userDocument(Constants.currentLoggedUser).get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        currentUserUpdated = updateCurrentUserFollowings(user, toFollow)
                    }
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error getting current user data", e) }
        return currentUserUpdated
    }

    private fun updateCurrentUserFollowings(user: User, toFollow: Boolean): Boolean{
        if(toFollow){
            user.profile.nbFollowings += 1
            user.followings.add(username.toString())
        } else {
            if (user.profile.nbFollowings > 0) user.profile.nbFollowings -= 1
            user.followings.remove(username.toString())
        }
        return true //means that the values have been updated
    }
}
