package com.github.orkest.ViewModel.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.orkest.View.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class SearchViewModel : ViewModel() {
    private val db = Firebase.firestore


    fun searchUserInDatabase(user :String, callback : (MutableList<String>)-> Unit) {

        if(user == ""){
            callback(mutableListOf())
            return
        }
        //TODO change the database collection to the actual letter when it will be setup

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val list: MutableList<String> = mutableListOf()
                for (document in result) {
                    val username = document.data["username"] as String
                    if((username).startsWith(user)){
                        list.add(username)
                    }
                }
                callback(list)

            }
            .addOnFailureListener { exception ->
                Log.d("HELLO", "Error getting documents: ", exception)
            }
    }




}





