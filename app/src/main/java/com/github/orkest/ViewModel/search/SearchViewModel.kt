package com.github.orkest.ViewModel.search


import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

/**
 * Part of the MVVM pattern,
 * the main method of this class is called each time the value of the search bar is changed through a remember mutable State
 * Communicate with the View using futures to deal with asynchronous fetch in the database
 *   SearchUserView <=> SearchViewModel
 */
class SearchViewModel() : ViewModel() {
     val db = Firebase.firestore


    /**
     * @param user is the content of the Ui search bar
     * @return the future that complete with the filtered list of user who have a username that starts with User
     */

    fun searchUserInDatabase(user :String) : CompletableFuture<MutableList<String>>{
        val future = CompletableFuture<MutableList<String>>()

        if(user == ""){
            future.complete(mutableListOf())
            return future
        }



        //TODO change the database collection to the actual letter when it will be setup


        db.collection("user/user-${user[0].uppercase()}/users")
            .get()
            .addOnSuccessListener { result ->

                val list: MutableList<String> = mutableListOf()
                for (document in result) {
                    val username = document.data["username"] as String
                    if((username).startsWith(user)){
                        list.add(username)
                    }
                }
                //future that will be used by the view
                future.complete(list)

            }
            .addOnFailureListener { exception ->
                Log.d("HELLO", "Error getting documents: ", exception)
                //failure does not append when the user is not find in the database, the returned collection will just be
            }
        return future
    }




}





