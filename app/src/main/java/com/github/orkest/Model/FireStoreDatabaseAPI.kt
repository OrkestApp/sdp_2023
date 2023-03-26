package com.github.orkest.Model

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

class FireStoreDatabaseAPI {
    private val db = Firebase.firestore


    fun fetchUserInDatabaseWithPrefix(prefix: String) : CompletableFuture<MutableList<User>>{
            val future = CompletableFuture<MutableList<User>>()

            if(prefix == ""){
                future.complete(mutableListOf())
                return future
            }
            db.collection("user/user-${prefix[0].uppercase()}/users")
                .get()
                .addOnSuccessListener { result ->

                    val list: MutableList<User> = mutableListOf()
                    for (document in result) {
                        val user = document.toObject(User::class.java)
                        if((user.username).startsWith(prefix)){
                            list.add(user)
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

    fun searchUserInDatabase(username : String): CompletableFuture<User>{
        return fetchUserInDatabaseWithPrefix(username).thenCompose {
            val future = CompletableFuture<User>()
            if(it.size ==1) {
                future.complete(it[0])
            }
            else {
                future.completeExceptionally(java.lang.IllegalStateException("2 user with the same name in the database"))
            }

            future
        }


    }


    fun addUserInDatabase(user: User): CompletableFuture<Boolean>{
        val completableFuture = CompletableFuture<Boolean>()
        val firstLetter = user.username[0].uppercase()
        val path = "user/user-$firstLetter/users"
         db.collection(path).document(user.username)
            .set(user).addOnSuccessListener {
                completableFuture.complete(true)
            }
        return completableFuture
        }

    }
