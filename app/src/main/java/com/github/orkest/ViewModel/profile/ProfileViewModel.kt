package com.github.orkest.ViewModel.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.orkest.Model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

open class ProfileViewModel(private val user: String) : ViewModel() {

    var db : FirebaseFirestore = Firebase.firestore
    private var userProfile = User()

    open var username = MutableLiveData<String>()
    open var bio = MutableLiveData<String>()
    open var nbFollowers = MutableLiveData<Int>()
    open var nbFollowings = MutableLiveData<Int>()
    open var profilePictureId = MutableLiveData<Int>()

    init{

        setupListener()
    }

    /**
     * To call everytime an instance of ProfileViewModel() is created
     */
    fun setupListener(){

        loadUserData()
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
    private fun loadUserData() {
        userDocument(user).get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val user = document.toObject(User::class.java)
                if (user != null) {
                    userProfile.profile = user.profile
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


}
