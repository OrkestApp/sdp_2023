package com.github.orkest.ui.profile

import androidx.lifecycle.MutableLiveData
import com.github.orkest.data.User
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.domain.FirebaseStorageAPI

class EditProfileViewModel {

    private val dbAPI = FireStoreDatabaseAPI()
    private val storageAPI = FirebaseStorageAPI()

    open var profilePicture = MutableLiveData<ByteArray?>()
    open var bio = MutableLiveData<String>()

    /**
     * Called when user saves changes made in the EditProfileActivity.
     * Updates the firestore DB and the firebase storage with the new data
     */
    fun updateStorage() {
        profilePicture.value?.let {FirebaseStorageAPI.uploadProfilePic(it) }
        // TODO save new bio in db
    }

    fun setProfilePic(pic: ByteArray) {
        profilePicture.value = pic
    }

    fun setBio(newBio: String) {
        bio.value = newBio
    }

    fun getProfilePic(): ByteArray? {
        return profilePicture.value
    }

    fun getBio(): String? {
        return bio.value
    }

}