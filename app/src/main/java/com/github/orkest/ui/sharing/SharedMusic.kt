package com.github.orkest.ui.sharing

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.github.orkest.data.Constants
import com.github.orkest.data.Post
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.domain.persistence.AppDatabase
import com.github.orkest.domain.persistence.AppEntities
import com.github.orkest.ui.CreateProfilePreview
import java.util.concurrent.CompletableFuture

/**
 * This function is used to display the list of users
 * that shared a song with the current user.
 *

 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun UsersList(database: AppDatabase, context: Context) {

    var messages by remember { mutableStateOf(mutableListOf("")) }
    val listSharers =
        remember { mutableStateOf(ArrayList<AppEntities.Companion.SharedSongEntity>().toList()) }


    if (FireStoreDatabaseAPI.isOnline(context)) {
        FireStoreDatabaseAPI().fetchFollowList(Constants.CURRENT_LOGGED_USER, true).thenAccept {
            messages = it
        }

        LazyColumn {
            items(messages) { user ->
                val intent = Intent(LocalContext.current, PlaylistActivity::class.java)
                intent.putExtra("senderUsername", Constants.CURRENT_LOGGED_USER)
                intent.putExtra("receiverUsername", user)
                CreateProfilePreview(user, intent)
            }
        }

        CompletableFuture.runAsync {
            val sharedSongEntities = messages.map { user ->
                AppEntities.Companion.SharedSongEntity(
                    sharerName = user
                )
            }
            database.sharedSongsDao().insertSharedSongs(sharedSongEntities)
        }
    } else {

        CompletableFuture.runAsync {
            val cachedSharedSongs = database.sharedSongsDao().getAllSharedSongs()
            listSharers.value = cachedSharedSongs.map { user ->
                AppEntities.Companion.SharedSongEntity(
                    sharerName = user.sharerName
                )
            }
        }

        LazyColumn {
            items(listSharers.value) { user ->
                CreateProfilePreview(user.sharerName, null)
            }
        }

    }
}

