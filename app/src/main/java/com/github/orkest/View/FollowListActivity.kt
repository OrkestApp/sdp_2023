package com.github.orkest.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.User
import com.github.orkest.View.sharing.ui.theme.OrkestTheme

class FollowListActivity(private val username: String ,private val isFollowersList: Boolean): ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OrkestTheme {
                FollowListScreen(username, isFollowersList)
            }
        }
    }
}


@Composable
fun FollowListScreen(username: String, isFollowersList: Boolean) {
    val db = FireStoreDatabaseAPI()
    val userList by remember { mutableStateOf(mutableListOf(User())) }

    db.fetchFollowList(username, isFollowersList).thenAccept { followList ->
        followList.forEach { username ->
            db.searchUserInDatabase(username).thenApply{ user ->
                userList.add(user)
            }
        }
    }

    UserList(users = userList)
}

@Composable
fun UserList(users: MutableList<User>) {
    LazyColumn {
        items(users) { user ->
            UserListItem(user)
        }
    }
}

@Composable
fun UserListItem(user: User){
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(text = user.username, fontWeight = FontWeight.Bold)
        Text(text = user.mail)
    }
}