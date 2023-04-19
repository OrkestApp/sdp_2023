package com.github.orkest.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.View.sharing.ui.theme.OrkestTheme
import com.github.orkest.ViewModel.FollowListViewModel
import com.github.orkest.R

val user = User(username = "Philippe", profile = Profile(profilePictureId = R.drawable.blank_profile_pic))
class FollowListActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val username = intent.getStringExtra("username").toString()
            val isFollowers = intent.getBooleanExtra("isFollowers", false)
            OrkestTheme {
                FollowList(viewModel = FollowListViewModel(username, isFollowers))
                //FollowListScreen(FollowListViewModel(username, isFollowers))
            }
        }
    }
}


@Composable
fun FollowList(viewModel: FollowListViewModel){
    Column {
        Row(Modifier
            .fillMaxWidth()
            .background(Color.Yellow)
        ) {
            val context = LocalContext.current
            IconButton(onClick = {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("username", viewModel.username)
                context.startActivity(intent)})
            {
                Image(
                    painterResource(id = R.drawable.back_button),
                    contentDescription = "Back button",
                    modifier = Modifier
                        .padding(5.dp)
                        .size(120.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))

            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center){
                Text(
                    text = if (viewModel.isFollowersList) "Followers" else "Followings",
                    style = TextStyle(fontSize = 28.sp),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        //Here only to show the feature. Will be deleted.
        CreateProfilePreview(user = user)
        CreateProfilePreview(user = user)
        CreateProfilePreview(user = user)
    }
}

@Composable
fun CreateProfilePreview(user: User){
    Row(modifier = Modifier
        .padding(all = 5.dp)
        .clickable {}
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(20.dp))
        .background(Color.DarkGray)
    ) {
        Image(
            painter = painterResource(user.profile.profilePictureId),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .padding(start = 10.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column (
            modifier = Modifier.height(50.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center) {
            Text(
                text = user.username,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}


/**
@Composable
fun FollowListScreen(viewModel: FollowListViewModel) {
    var userList by remember { mutableStateOf(mutableListOf<User>()) }

    viewModel.retrieveFollowList().thenAccept {
        userList = it
    }

    Column{
        val context = LocalContext.current
        IconButton(onClick = {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("username", viewModel.username)
                context.startActivity(intent)})
        {
            Image(
                painterResource(id = R.drawable.back_button),
                contentDescription = "Back button",
                modifier = Modifier
                    .padding(10.dp)
                    .height(100.dp)
                    .width(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(userList) { user ->
                val context = LocalContext.current
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("username",user.username)
                UserListItem(user = user, intent = intent)
            }
        }
    }
    
}

@Composable
fun UserList(users: MutableList<User>) {
    LazyColumn {
        items(users) { user ->
            val context = LocalContext.current
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("username",user.username)
            UserListItem(user = user, intent = intent)
        }
    }
}

@Composable
fun UserListItem(user: User, intent: Intent){
    val context = LocalContext.current
    Row(modifier = Modifier
        .padding(all = 10.dp)
        .clickable { context.startActivity(intent) }
        .fillMaxSize()){

        Image(
            painter = painterResource(R.drawable.blank_profile_pic), // TODO A CHANGER
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        // Add a horizontal space between the image and the text
        Spacer(modifier = Modifier.width(10.dp))

        Text(text = user.username)
    }
}
**/
