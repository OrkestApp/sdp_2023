package com.github.orkest.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

class FireStoreDatabaseAPI {
    private val db = Firebase.firestore

    /**
     * @param prefix the prefix of the username we want to search in the database
     * @return completable future of a list of User that match the prefix in their usernames
     *
     */
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

    /**
     * @param username search a user with username
     * @return CompletableFuture of user, the User that match the username
     *
     * also assure that their is only one user that matches this username
     */
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

    /**
     * @param user the User we want to add in the database
     * @return a completable fututure that completes to true, only if user is correctly add
     *
     * Assure that their is not a user with the same username already in the database
     */

    fun addUserInDatabase(user: User): CompletableFuture<Boolean>{
        val completableFuture = CompletableFuture<Boolean>()
        val document = getUserDocumentRef(user.username)

        document.get().addOnSuccessListener {
            if (it.data != null) {
                println(it)
                completableFuture.complete(false)
            } else {
                //If no user with the same username was found, add the user to the database
                document.set(user).addOnSuccessListener { completableFuture.complete(true) }
            }
        }
            //Propagates the exception in case of another exception
            .addOnFailureListener{
                completableFuture.completeExceptionally(it)
            }

        return completableFuture
    }

    /**
     * Check if user and mail are already in the database
     */
    fun userMailInDatabase(user: User): CompletableFuture<Boolean>{

        val auth = FirebaseAuth.getInstance()
        val future = CompletableFuture<Boolean>()

        //Checks if the database already contains a user with the same username and email
        getUserDocumentRef(user.username).get()
            .addOnSuccessListener {
                if (it.data != null && it.get("mail").toString() == auth.currentUser?.email.toString()) {
                    println(it)
                    future.complete(true)
                } else {
                    future.complete(false)
                }
            }
            //Propagates the exception in case of another exception
            .addOnFailureListener{
                future.completeExceptionally(it)
            }

        return future
    }

    /**
     * @param username
     * @return the DocumentReference of the user that match the username
     */
    fun getUserDocumentRef(username :String):DocumentReference{
        val firstLetter = username[0].uppercase()
        val path = "user/user-$firstLetter/users"
        return db.collection(path).document(username)
    }


    fun storeTokenInDatabase(username:String,token:String?): CompletableFuture<Boolean>{
        val completableFuture = CompletableFuture<Boolean>()
        val path = "deezerToken"
        db.collection(path).document(username).set(hashMapOf ("token" to token)).addOnSuccessListener {
            completableFuture.complete(true)
        }.addOnFailureListener{
            completableFuture.complete(false)
        }
        return completableFuture
    }


























    }
