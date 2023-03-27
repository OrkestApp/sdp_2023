package com.github.orkest.ViewModel.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orkest.Constants
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture

open class ProfileViewModel(private val user: String) : ViewModel() {

    var db : FirebaseFirestore = Firebase.firestore
    private var userProfile = User()

    open var username = MutableLiveData<String>()
    open var bio = MutableLiveData<String>()
    open var nbFollowers = MutableLiveData<Int>()
    open var nbFollowings = MutableLiveData<Int>()
    open var profilePictureId = MutableLiveData<Int>()

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

    private val _isUserFollowed = MutableLiveData<Boolean>()
    var isUserFollowed: LiveData<Boolean> = _isUserFollowed
    fun isUserFollowed(): CompletableFuture<Boolean>{
        val future = CompletableFuture<Boolean>()
        userDocument(user).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        _isUserFollowed.postValue(user.followers.contains(Constants.currentLoggedUser))
                        future.complete(true)
                    }
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error getting user data", e) }
        return future
    }

    fun follow(): CompletableFuture<Boolean>{
        val futureFollow =CompletableFuture<Boolean>()
        var userUpdated = false
        var currentUserUpdated = false

        userDocument(user).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        user.profile.nbFollowers += 1
                        user.followers.add(Constants.currentLoggedUser)
                        userUpdated = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
            }

        userDocument(Constants.currentLoggedUser).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        user.profile.nbFollowings += 1
                        user.followings.add(username.toString())
                        currentUserUpdated = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
            }

        futureFollow.complete(userUpdated && currentUserUpdated)
        return futureFollow
    }

    fun unfollow(): CompletableFuture<Boolean>{
        val futureFollow =CompletableFuture<Boolean>()
        var userUpdated = false
        var currentUserUpdated = false

        userDocument(user).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        if (user.profile.nbFollowers > 0) user.profile.nbFollowers -= 1
                        user.followers.remove(Constants.currentLoggedUser)
                        userUpdated = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
            }

        userDocument(Constants.currentLoggedUser).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        if (user.profile.nbFollowings > 0 ) user.profile.nbFollowings -= 1
                        user.followings.remove(username.toString())
                        currentUserUpdated = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
            }

        futureFollow.complete(userUpdated && currentUserUpdated)
        return futureFollow
    }


}
