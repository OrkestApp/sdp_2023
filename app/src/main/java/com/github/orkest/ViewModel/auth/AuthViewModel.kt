package com.github.orkest.ViewModel.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.github.orkest.Model.Providers
import com.github.orkest.Model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture


open class AuthViewModel: ViewModel() {

    var db = Firebase.firestore

    private var user = User()

    private val username = mutableStateOf(TextFieldValue())
    private val bio = mutableStateOf(TextFieldValue())
    private val selectedProvider = mutableStateOf(Providers.SPOTIFY)

    /**
     * Returns the current value of the username to be displayed on the view
     */
    fun getUsername(): TextFieldValue{
        return username.value
    }

    /**
     * Return the current value of the profile description of the user
     * to be displayed on the view
     */
    fun getBio():TextFieldValue{
        return bio.value
    }

    /**
     * Returns the Provider of the user to display on the view (of type Enum Providers)
     */
    fun getProvider():Providers{
        return selectedProvider.value
    }

    /**
     * Updates the value of the username after the user set it on the view
     */
    fun updateUsername(value:TextFieldValue){
        username.value = value
    }

    /**
     * Updates the value of the bio after the user set it on the view
     */
    fun updateBio(value:TextFieldValue){
        bio.value = value
    }
    
    /**
     * Updates the value of the provider of the user after the user
     * set it on the view
     */
    fun updateProvider(provider:Providers){
        selectedProvider.value = provider
    }


    /**
     * Called once the user finished inputting its credentials
     * Returns a Future that completes with :
     * True if the user has been successfully added to the database,
     * False if the username already exists in the database
     */
    open fun createUser(): CompletableFuture<Boolean> {

        //Updates the user's credentials
        updateUser()

        //Computes the path to store the user in : users-firstLetter of its username
        val firstLetter = username.value.text[0].uppercase()
        val path = "users-$firstLetter"

        val future = CompletableFuture<Boolean>()

        //Checks if the database already contains a user with the same username
        db.collection(path)
            .document(user.username).get()
            .addOnSuccessListener { //TODO: Handle failure too
                if (it.data != null) {
                    println(it)
                    future.complete(false)
                } else {
                    //If no user with the same username was found, add the user to the database
                    pushUser(path)
                        .addOnSuccessListener { future.complete(true) }
                }
            }
            .addOnFailureListener{
                future.completeExceptionally(it)
            }

        return future
    }

    /**
     * Updates the user's credentials after validation
     */
    private fun updateUser(){
        user.username = username.value.text
        user.profile.username = user.username
        user.profile.bio = bio.value.text
        user.serviceProvider = selectedProvider.value.value
    }

    //TODO: handle concurrency
    /**
     * Adds the newly created user to the database
     */
    private fun pushUser(path : String): Task<Void> {
        return db.collection(path).document(user.username)
            .set(user)
    }
}