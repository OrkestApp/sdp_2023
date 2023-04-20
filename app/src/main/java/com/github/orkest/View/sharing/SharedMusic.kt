package com.github.orkest.View.sharing

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.github.orkest.Constants
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.github.orkest.View.search.SearchUserView
import com.github.orkest.ViewModel.playlist.PlaylistViewModel

/**
 * This function is used to display the list of users
 * that shared a song with the current user.
 *
 * @param viewModel the view model
 */
@Composable
fun UsersList(viewModel: PlaylistViewModel) {
    val messages = List(
        // create a dummy User
        10
    ) {
        // Temporary static list of users for demo, will be replaced by a list of users from cache and Database
        User("ehreich2", "", "", Profile("ehreich2", 1))
    }
    LazyColumn {
        items(messages) { user ->
            val intent : Intent = Intent(LocalContext.current, PlaylistActivity::class.java)
            intent.putExtra("senderUsername", Constants.CURRENT_LOGGED_USER)
            intent.putExtra("receiverUsername", user.username)
            SearchUserView.CreateUser(user.username, intent)
        }
    }
}
