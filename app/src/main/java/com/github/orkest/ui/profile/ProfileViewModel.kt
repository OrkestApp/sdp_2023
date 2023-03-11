package com.github.orkest.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.orkest.DataModel.Profile
import com.github.orkest.DataModel.User
import com.github.orkest.ui.profile.Interfaces.ProfileContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


/**
 * Handles data operation
 * Extract user information from database
 */

class ProfileViewModel: ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _userProfile = MutableLiveData<Profile>()
    val userProfile: LiveData<Profile> = _userProfile

    fun loadUserProfile(userId: String){
        firestore.collection("users")
            .document()
    }


}