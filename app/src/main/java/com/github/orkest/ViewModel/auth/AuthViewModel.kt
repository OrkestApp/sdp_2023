package com.github.orkest.ViewModel.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.github.orkest.Model.Providers
import com.github.orkest.Model.User


class AuthViewModel: ViewModel() {

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
        //TODO: Check that the username is unique and add it to the database,
        // Publish error
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








}