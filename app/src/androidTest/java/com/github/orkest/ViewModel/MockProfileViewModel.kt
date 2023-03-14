package com.github.orkest.ViewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.orkest.R
import com.github.orkest.ViewModel.profile.ProfileViewModel

class MockProfileViewModel(user: String) : ProfileViewModel(user) {



    /**Adding users in the database**/
    /**==========================================================================================**/
    init{
        addJohn()
        addRebecca()
    }
    private fun addJohn() {

        val John = hashMapOf(
            "username" to "JohnDoe",
            "bio" to "Hello, I'm John!",
            "nbFollowers" to 100,
            "nbFollowings" to 50,
            "profilePictureId" to R.drawable.profile_picture
        )

        db.collection("users").document("user-J")
            .collection("users").document("JohnDoe")
            .collection("profile").document("profile_data")
            .set(John)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User added successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding user", e)
            }

    }
    private fun addRebecca(){
        val Rebecca = hashMapOf(
            "username" to "RebeccaSmith",
            "bio" to "What's up guys",
            "nbFollowers" to 2,
            "nbFollowings" to 102,
            "profilePictureId" to 2
        )
        db.collection("users").document("user-R")
            .collection("users").document("RebeccaSmith")
            .collection("profile").document("profile_data")
            .set(Rebecca)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User added successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding user", e)
            }
    }
    /**==========================================================================================**/

    override var username = MutableLiveData<String>()
    override var bio = MutableLiveData<String>()
    override var nbFollowers = MutableLiveData<Int>()
    override var nbFollowings = MutableLiveData<Int>()
    override var profilePictureId = MutableLiveData<Int>()

    override fun getUsername(): LiveData<String> = username
    override fun getBio(): LiveData<String> = bio
    override fun getNbFollowers(): LiveData<Int> = nbFollowers
    override fun getNbFollowings(): LiveData<Int> = nbFollowings
    override fun getProfilePictureId(): LiveData<Int> = profilePictureId





    fun updateUsername(value: String) {
        profileData.update("username", value)
            .addOnSuccessListener {
                username.value = value
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating username", e)
            }
    }

    fun updateBio(value: String) {
        profileData.update("bio", value)
            .addOnSuccessListener {
                bio.value = value
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating bio", e)
            }
    }

    fun updateNbFollowers(value: Int) {
        profileData.update("nb_followers", value)
            .addOnSuccessListener {
                nbFollowers.value = value
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating nbFollowers", e)
            }
    }

    fun updateNbFollowings(value: Int) {
        profileData.update("nb_followings", value)
            .addOnSuccessListener {
                nbFollowings.value = value
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating nbFollowings", e)
            }
    }

    fun updateProfilePictureId(value: Int) {
        profileData.update("profile_picture_id", value)
            .addOnSuccessListener {
                profilePictureId.value = value
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating profilePictureId", e)
            }
    }
}