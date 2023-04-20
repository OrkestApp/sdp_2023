package com.github.orkest.ViewModel.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.orkest.Constants
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val fireStoreDatabaseAPI = FireStoreDatabaseAPI()

    var imageUri by mutableStateOf("")
        private set

    private var _isUploading by mutableStateOf(false)
    val isUploading: Boolean
        get() = _isUploading

    val updatedData = mutableMapOf<String, Any>()

    /**
     * Updates the imageUri with the uri of the image picked by the user
     */
    fun onImagePicked(uri: Uri) {
        imageUri = uri.toString()
        uploadImage(uri)
    }

    /**
     * Uploads the image to the Firebase Storage
     */
    private fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            _isUploading = true
            val imageRef = storageRef.child("profil_images/users/${Constants.CURRENT_LOGGED_USER}.jpg")
            imageRef.putFile(uri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    updatedData["profile.picture"] = downloadUri.toString()
                    _isUploading = false
                }
            }.addOnFailureListener {
                _isUploading = false
            }
        }
    }

    /**
     * Updates the user profile with the new data
     */
    fun updateUserProfile(username: String) {
        fireStoreDatabaseAPI.updateUserProfile(username, updatedData)
    }
}
