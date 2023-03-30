package com.github.orkest.ViewModel.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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


}
