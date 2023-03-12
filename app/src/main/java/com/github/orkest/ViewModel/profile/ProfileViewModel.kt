package com.github.orkest.ViewModel.profile

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import org.w3c.dom.Text

class ProfileViewModel : ViewModel() {

    private val username = mutableStateOf(TextFieldValue())
    private val bio = mutableStateOf(TextFieldValue())
    private val nbFollowers = mutableStateOf(TextFieldValue())
    private val nbFollowings = mutableStateOf(TextFieldValue())
    private val profilePictureId = mutableStateOf(TextFieldValue())

    fun getUsername(): TextFieldValue{
        return username.value
    }

    fun getBio(): TextFieldValue{
        return bio.value
    }

    fun getNbFollowers(): TextFieldValue{
        return nbFollowers.value
    }

    fun getNbFollowings(): TextFieldValue{
        return nbFollowings.value
    }

    fun getProfilePictureId(): TextFieldValue{
        return profilePictureId.value
    }

    fun updateUsername(value: TextFieldValue){
        username.value = value
    }

    fun updateBio(value: TextFieldValue){
        bio.value = value
    }

    fun updateNbFollowers(value: TextFieldValue){
        nbFollowers.value = value
    }

    fun updateNbFollowings(value: TextFieldValue){
        nbFollowings.value = value
    }

    fun updateProfilePictureId(value: TextFieldValue){
        profilePictureId.value = value
    }
}