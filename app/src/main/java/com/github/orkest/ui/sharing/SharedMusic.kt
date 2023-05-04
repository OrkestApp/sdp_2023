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
import com.github.orkest.ui.search.SearchUserView

/**
 * This function is used to display the list of users
 * that shared a song with the current user.
 *
 * @param viewModel the view model
 */
@Composable
fun UsersList(viewModel: PlaylistViewModel) {
    var messages by remember { mutableStateOf(mutableListOf("")) }
    FireStoreDatabaseAPI().fetchFollowList(Constants.CURRENT_LOGGED_USER,true).thenAccept {
        messages = it
    }
    LazyColumn {
        items(messages) { user ->
            val intent : Intent = Intent(LocalContext.current, PlaylistActivity::class.java)
            intent.putExtra("senderUsername", Constants.CURRENT_LOGGED_USER)
            intent.putExtra("receiverUsername", user)
            SearchUserView.CreateUser(user, intent)
        }
    }
}

