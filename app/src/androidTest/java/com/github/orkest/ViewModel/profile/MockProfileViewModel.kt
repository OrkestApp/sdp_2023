package com.github.orkest.ViewModel.profile

import androidx.lifecycle.MutableLiveData
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import java.util.concurrent.CompletableFuture

class MockProfileViewModel(user: String) : ProfileViewModel(user) {

    override var username = MutableLiveData<String>()
    override var bio = MutableLiveData<String>()
    override var nbFollowers = MutableLiveData<Int>()
    override var nbFollowings = MutableLiveData<Int>()
    override var profilePictureId = MutableLiveData<Int>()


    // Using runOnUiThread for the mock to set values bc it is not permitted on background threads
    fun setUsername(value: String?){
        UiThreadStatement.runOnUiThread { username.value = value }
    }

    fun setBio(value: String?){
        UiThreadStatement.runOnUiThread { bio.value = value }
    }
    fun setNbFollowers(value: Int?){
        UiThreadStatement.runOnUiThread { nbFollowers.value = value }
    }

    fun setNbFollowings(value: Int?){
        UiThreadStatement.runOnUiThread { nbFollowings.value = value }
    }

    fun setProfilePictureId(value: Int?){
        UiThreadStatement.runOnUiThread { profilePictureId.value = value }
    }

    fun loadData(username: String?, bio: String?, nbFollowers: Int?, nbFollowings: Int?, profilePicture: Int?){
        UiThreadStatement.runOnUiThread {
            setUsername(username)
            setProfilePictureId(profilePicture)
            setBio(bio)
            setNbFollowers(nbFollowers)
            setNbFollowings(nbFollowings)
        }
    }




    fun setIsUserFollowed(value: Boolean){
        UiThreadStatement.runOnUiThread { isUserFollowed.value = value }
    }

     override fun isUserFollowed(): CompletableFuture<Boolean> {
        return CompletableFuture.completedFuture(isUserFollowed.value)
    }

    override fun follow(): CompletableFuture<Boolean> {
        isUserFollowed.value = true
        return CompletableFuture.completedFuture(true)
    }

    override fun unfollow(): CompletableFuture<Boolean> {
        isUserFollowed.value = false
        return CompletableFuture.completedFuture(true)
    }


}
