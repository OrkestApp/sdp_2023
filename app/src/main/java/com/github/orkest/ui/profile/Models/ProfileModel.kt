package com.github.orkest.ui.profile.Models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.core.snap
import androidx.lifecycle.ViewModel
import com.github.orkest.DataModel.Profile
import com.github.orkest.DataModel.User
import com.github.orkest.ui.profile.Interfaces.ProfileContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

/**
 * Handles data operation
 * Extract user information from database
 */

class ProfileModel: ProfileContract.Model {

    //Get a reference to the database
    private val database = Firebase.database

    //Get a reference to the currently authenticated user
    private val currentUser = FirebaseAuth.getInstance().currentUser

    //Get a reference to the current user's data in the database
    private val userRef = database.getReference("users/${currentUser?.uid}")
    private lateinit var updatedProfile: Profile


    override fun getProfileData(callback: (Profile) -> Unit) {

        //Read the current user's data from the database whenever the data changes
        userRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val profileSnapshot = snapshot.child("profile")

                val username = profileSnapshot.child("username").getValue(String::class.java)
                val profilePictureId = profileSnapshot.child("profilePictureId").getValue(Int::class.java)
                val bio = profileSnapshot.child("bio").getValue(String::class.java)
                val nbFollowers = profileSnapshot.child("nbFollowers").getValue(Int::class.java)
                val nbFollowings = profileSnapshot.child("nbFollowings").getValue(Int::class.java)

                updatedProfile = Profile(
                    username!!,
                    profilePictureId!!,
                    bio!!,
                    nbFollowers!!,
                    nbFollowings!!,
                    listOf(), //TODO: to change when merging
                    listOf()
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error when updating user data", error.toException())
            }

        })

        callback(updatedProfile)
    }


    fun follow(user: User){

        //Get a reference to the location where the user data is stored
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")

        //Query the database to find the user with a specific username
        val query = databaseReference.orderByChild("username").equalTo(user.username)

        query.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val userKey = snapshot.children.first().key
                    if (userKey != null) {
                        //databaseReference.child(userKey).child("followers").push().setValue(currentUser.) put the currentUserName
                    } else { Log.e(TAG, "User key is null") }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        if(currentUser != null){
            val countReference = userRef.child("Profile").child("nbFollowings")
            countReference.setValue(ServerValue.increment(1))


        }
    }

    fun unfollow(user: User){

    }


}