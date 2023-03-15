package com.github.orkest.ViewModel.search


import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.orkest.Model.mockDatabaseSearch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture


class SearchViewModel(private val mock:Boolean) : ViewModel() {
     val db = Firebase.firestore



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
                future.complete(list)

            }
            .addOnFailureListener { exception ->
                Log.d("HELLO", "Error getting documents: ", exception)
            }
        return future
    }




}





