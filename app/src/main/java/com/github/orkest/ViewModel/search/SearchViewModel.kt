package com.github.orkest.ViewModel.search


import androidx.lifecycle.ViewModel
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.User
import java.util.concurrent.CompletableFuture

/**
 * Part of the MVVM pattern,
 * the main method of this class is called each time the value of the search bar is changed through a remember mutable State
 * Communicate with the View using futures to deal with asynchronous fetch in the database
 *   SearchUserView <=> SearchViewModel
 */
class SearchViewModel() : ViewModel() {
     private val dbAPI = FireStoreDatabaseAPI()


    /**
     * @param user is the content of the Ui search bar
     * @return the future that complete with the filtered list of user who have a username that starts with User
     */

    fun searchUserInDatabase(user :String) : CompletableFuture<MutableList<User>> {
        return dbAPI.fetchUserInDatabaseWithPrefix(user)
    }




}





