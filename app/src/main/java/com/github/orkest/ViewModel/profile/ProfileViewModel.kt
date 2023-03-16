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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

open class ProfileViewModel(private val user: String) : ViewModel() {

    var db : FirebaseFirestore = Firebase.firestore
    private lateinit var profileData : DocumentReference
    lateinit var userData : DocumentReference
    private var userProfile = User()

     open var username = MutableLiveData<String>()
     open var bio = MutableLiveData<String>()
     open var nbFollowers = MutableLiveData<Int>()
     open var nbFollowings = MutableLiveData<Int>()
     open var profilePictureId = MutableLiveData<Int>()

    /**
     * To execute everytime an instance of ProfileViewModel() is created
     */
    fun setupListener(){
        loadUserData()
        listenToUserData()
    }

    fun profileData() : DocumentReference {
        val firstLetter = user[0].uppercase()
        val path = "user/user-$firstLetter/users"
        userData = db.collection(path).document(user)
        /**profileData = db.collection(path).document(user)
            .collection("profile").document("profile_data")**/
        return userData
    }

    /**
     * Fetches data from the Firestore document and sets the profile values
     */
    private fun loadUserData() {
        profileData().get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profile = document.toObject(Profile::class.java)
                    if(profile != null){
                        userProfile.profile = profile
                    }
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
        profileData().addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val profile = snapshot.toObject(Profile::class.java)
                if(profile != null){
                    userProfile.profile = profile
                }
            } else { Log.d(TAG, "Current data: null") }
        }
    }

    open fun getUsername(): LiveData<String> = username
    open fun getBio(): LiveData<String> = bio
    open fun getNbFollowers(): LiveData<Int> = nbFollowers
    open fun getNbFollowings(): LiveData<Int> = nbFollowings
    open fun getProfilePictureId(): LiveData<Int> = profilePictureId

}
