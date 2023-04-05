package com.github.orkest.ViewModel.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.github.orkest.Constants
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Providers
import com.github.orkest.Model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.CompletableFuture


open class AuthViewModel: ViewModel() {


    private val dbAPI = FireStoreDatabaseAPI()

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
     * Returns a Future that completes with True if the user has been successfully added to the database,
     * False if it already exists, and an exception if an error occurred
     */
    open fun createUser(): CompletableFuture<Boolean> {

        val future = CompletableFuture<Boolean>()

        //Updates the user's credentials and transmits any exception through the future
        try { updateUser() } catch (e: Exception) {
             future.completeExceptionally(e)
             return future
        }

        // Computes the path to store the user in : user/user-firstLetter/users
        // user-firstletter is a document containing a subcollection which contains the users's documents

        return dbAPI.addUserInDatabase(user)
    }

    /**
     * Updates the user's credentials after validation
     */
    private fun updateUser(){
        val auth = FirebaseAuth.getInstance()

        if (username.value.text.isEmpty()) throw Exception("Username cannot be empty")
        user.username = username.value.text
        user.profile.username = user.username
        user.profile.bio = bio.value.text
        user.serviceProvider = selectedProvider.value.value
        //updated with the email of the user
        user.mail = auth.currentUser?.email.toString()
    }

    /**
     * If the username is empty throws an exception
     */
    private fun checkUsername(){
        if (username.value.text.isEmpty()) throw Exception("Username cannot be empty")
    }

    /**
     * In a logic similar to createUser,
     * this method checks if the user and the corresponding email already exist in the database
     */
    open fun signInUser(): CompletableFuture<Boolean> {

        val future = CompletableFuture<Boolean>()

        user.username = username.value.text
        Constants.currentLoggedUser = username.value.text

        try { checkUsername() } catch (e: Exception) {
            future.completeExceptionally(e)
            return future
        }

        return dbAPI.userMailInDatabase(user)
    }
}