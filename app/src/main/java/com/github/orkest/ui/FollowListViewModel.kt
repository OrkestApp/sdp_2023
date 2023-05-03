package com.github.orkest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.orkest.data.User
import com.github.orkest.domain.FireStoreDatabaseAPI
import java.util.concurrent.CompletableFuture

open class FollowListViewModel(val username: String, val isFollowersList: Boolean) {

    private val dbAPI = FireStoreDatabaseAPI()

    /**
     * Retrieves the list of users in the followers or followings list
     */
    open fun retrieveFollowList(): LiveData<MutableList<User>> {
        val userListLiveData = MutableLiveData<MutableList<User>>()

        dbAPI.fetchFollowList(username, isFollowersList).thenApplyAsync { followList ->
            val userList = mutableListOf<User>()
            val userSearchFutures = mutableListOf<CompletableFuture<User>>()

            followList.forEach { username ->
                val userSearchFuture = dbAPI.searchUserInDatabase(username)
                userSearchFutures.add(userSearchFuture)
                userSearchFuture.thenAccept { user ->
                    userList.add(user)
                }
            }

            //Updates the returned list when all users have been added
            CompletableFuture.allOf(*userSearchFutures.toTypedArray()).thenRun {
                userListLiveData.postValue(userList)
            }
        }

        return userListLiveData
    }

}