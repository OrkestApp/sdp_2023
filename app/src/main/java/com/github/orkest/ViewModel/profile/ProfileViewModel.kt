package com.github.orkest.ViewModel.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private var uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User is not authenticated")

    private val db = Firebase.firestore
    private val profileData = db.collection("users").document(uid)
                                .collection("profile").document("profile_data")

     var username = MutableLiveData<String>()
     var bio = MutableLiveData<String>()
     var nbFollowers = MutableLiveData<Int>()
     var nbFollowings = MutableLiveData<Int>()
     var profilePictureId = MutableLiveData<Int>()

    init {
        profileData.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Update the data in the ProfileViewModel
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

    fun getUsername(): LiveData<String> = username
    fun getBio(): LiveData<String> = bio
    fun getNbFollowers(): LiveData<Int> = nbFollowers
    fun getNbFollowings(): LiveData<Int> = nbFollowings
    fun getProfilePictureId(): LiveData<Int> = profilePictureId

    private fun loadUserData() {
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



    fun updateUsername(value: String) {
        profileData.update("username", value)
            .addOnSuccessListener {
                username.value = value
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating username", e)
            }
    }

    fun updateBio(value: String) {
        profileData.update("bio", value)
            .addOnSuccessListener {
                bio.value = value
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating bio", e)
            }
    }

    fun updateNbFollowers(value: Int) {
        profileData.update("nb_followers", value)
            .addOnSuccessListener {
                nbFollowers.value = value
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating nbFollowers", e)
            }
    }

    fun updateNbFollowings(value: Int) {
        profileData.update("nb_followings", value)
            .addOnSuccessListener {
                nbFollowings.value = value
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating nbFollowings", e)
            }
    }

    fun updateProfilePictureId(value: Int) {
        profileData.update("profile_picture_id", value)
            .addOnSuccessListener {
                profilePictureId.value = value
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating profilePictureId", e)
            }
    }
}
