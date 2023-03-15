package com.github.orkest.ViewModel.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

open class ProfileViewModel(user: String) : ViewModel() {


    private val db = Firebase.firestore
    private val firstLetter = user[0].uppercase()
    val path = "user/user-$firstLetter/users"
    private val profileData = db.collection(path).document(user)
                                .collection("profile").document("profile_data")

     open var username = MutableLiveData<String>()
     open var bio = MutableLiveData<String>()
     open var nbFollowers = MutableLiveData<Int>()
     open var nbFollowings = MutableLiveData<Int>()
     open var profilePictureId = MutableLiveData<Int>()

    /**
     * Executed block everytime an instance of ProfileViewModel() is created
     */
    init {
        loadUserData()
        listenToUserData()
    }

    /**
     * Fetches data from the Firestore document and sets the profile values
     */
    fun loadUserData() {
        profileData.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    username.value = document.getString("username")
                    bio.value = document.getString("bio")
                    nbFollowers.value = document.getLong("nb_followers")?.toInt() ?: -1
                    nbFollowings.value = document.getLong("nb_followings")?.toInt() ?: -1
                    profilePictureId.value = document.getLong("profile_picture_id")?.toInt() ?: -1
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
            }
    }

    /**
     * Listens to the changes in the Firestore document profile_data
     * and updates the view-model's values
     */
    fun listenToUserData(){
        profileData.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                username.value = snapshot.getString("username")
                bio.value = snapshot.getString("bio")
                nbFollowers.value = snapshot.getLong("nb_followers")?.toInt() ?: -1
                nbFollowings.value = snapshot.getLong("nb_followings")?.toInt() ?: -1
                profilePictureId.value = snapshot.getLong("profile_picture_id")?.toInt() ?: -1
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    open fun getUsername(): LiveData<String> = username
    open fun getBio(): LiveData<String> = bio
    open fun getNbFollowers(): LiveData<Int> = nbFollowers
    open fun getNbFollowings(): LiveData<Int> = nbFollowings
    open fun getProfilePictureId(): LiveData<Int> = profilePictureId

}
