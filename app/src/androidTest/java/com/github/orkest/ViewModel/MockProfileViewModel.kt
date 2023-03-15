package com.github.orkest.ViewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.github.orkest.R
import com.github.orkest.ViewModel.profile.ProfileViewModel

class MockProfileViewModel(user: String) : ProfileViewModel(user) {

    /**
     * Tests whether the right values are displayed
     * Tests whether the right values are displayed
     * when the values change in the viewmodel
     */

    override var username = MutableLiveData<String>()
    override var bio = MutableLiveData<String>()
    override var nbFollowers = MutableLiveData<Int>()
    override var nbFollowings = MutableLiveData<Int>()
    override var profilePictureId = MutableLiveData<Int>()

    fun setUsername(value: String){
        username.value = value
    }

    fun setBio(value: String){
        bio.value = value
    }
    fun setNbFollowers(value: Int){
        nbFollowers.value = value
    }

    fun setNbFollowings(value: Int){
        nbFollowings.value = value
    }

    fun setProfilePictureId(value: Int){
        profilePictureId.value = value
    }

    fun loadData(username: String, bio: String, nbFollowers: Int, nbFollowings: Int, profilePicture: Int){
        UiThreadStatement.runOnUiThread {
            setUsername(username)
            setProfilePictureId(profilePicture)
            setBio(bio)
            setNbFollowers(nbFollowers)
            setNbFollowings(nbFollowings)
        }
    }


}
