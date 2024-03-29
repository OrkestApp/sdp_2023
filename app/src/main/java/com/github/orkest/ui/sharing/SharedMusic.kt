package com.github.orkest.ui.sharing

import android.content.Intent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.github.orkest.data.Constants
import com.github.orkest.data.Profile
import com.github.orkest.data.User
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.ui.CreateProfilePreview
import com.github.orkest.ui.search.SearchUserView

/**
 * This function is used to display the list of users
 * that shared a song with the current user.
 *

 */
@Composable
fun UsersList() {
    var messages by remember { mutableStateOf(mutableListOf("")) }
    FireStoreDatabaseAPI().fetchFollowList(Constants.CURRENT_LOGGED_USER,true).thenAccept {
        messages = it
    }
    LazyColumn {
        items(messages) { user ->
            val intent : Intent = Intent(LocalContext.current, PlaylistActivity::class.java)
            intent.putExtra("senderUsername", Constants.CURRENT_LOGGED_USER)
            intent.putExtra("receiverUsername", user)
            CreateProfilePreview(user, intent)
        }
    }
}

