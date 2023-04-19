package com.github.orkest.ViewModel

import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.User
import java.util.concurrent.CompletableFuture

class FollowListViewModel(val username: String, val isFollowersList: Boolean) {

    private val dbAPI = FireStoreDatabaseAPI()
    /**

    /**
     * Retrieves the list of users in the followers or followings list
     */
    fun retrieveFollowList(): CompletableFuture<MutableList<User>>{
        val future = CompletableFuture<MutableList<User>>()
        val userList = mutableListOf<User>()

        dbAPI.fetchFollowList(username, isFollowersList).thenApply { followList ->
            followList.forEach { username ->
                dbAPI.searchUserInDatabase(username).thenApply { user ->
                    userList.add(user)
                }
            }
            future.complete(userList)
        }
        return future
    }
    **/

}
