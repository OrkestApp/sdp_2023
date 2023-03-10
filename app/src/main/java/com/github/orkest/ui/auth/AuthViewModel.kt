package com.github.orkest.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.github.orkest.DataModel.Providers
import com.github.orkest.DataModel.User


class AuthViewModel: ViewModel() {

    private var user = User()

    private val username = mutableStateOf(TextFieldValue())
    private val bio = mutableStateOf(TextFieldValue())
    private val selectedProvider = mutableStateOf(Providers.SPOTIFY)

    fun getUsername(): TextFieldValue{
        return username.value
    }

    fun getBio():TextFieldValue{
        return bio.value
    }

    fun getProvider():Providers{
        return selectedProvider.value
    }

    fun updateUsername(value:TextFieldValue){
        username.value = value
        //TODO: Check that the username is unique and add it to the database,
        // Publish error
    }

    fun updateBio(value:TextFieldValue){
        bio.value = value
    }

    fun updateProvider(provider:Providers){
        selectedProvider.value = provider
    }







}