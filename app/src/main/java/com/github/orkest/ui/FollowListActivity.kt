package com.github.orkest.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.data.Profile
import com.github.orkest.data.User
import com.github.orkest.ui.profile.ProfileActivity
import com.github.orkest.ui.sharing.ui.theme.OrkestTheme
import com.github.orkest.R

//Here for preview purposes. Will be deleted.
val user = User(username = "Philippe", profile = Profile(profilePictureId = R.drawable.blank_profile_pic))

private val paddingValue = 5.dp
private val titleFontSize = 28.sp
private val textFontSize = 18.sp
private val smallSeparator = 10.dp
private val largeSeparator = 20.dp
private val itemSize = 50.dp
private val roundedCornerValue = 20.dp

/**
 * This activity is launched when a user wants to display the list of followers or followings of another user (or himself)
 * Two extras are sent with the intent to launch the activity
 * username : String = The username of the user to which we want to access the follow lists
 * isFollowers : Boolean = Whether it is the followers list we want to display or the followings list
 */
class FollowListActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val username = intent.getStringExtra("username").toString()
            val isFollowers = intent.getBooleanExtra("isFollowers", false)
            OrkestTheme {
                FollowList(viewModel = FollowListViewModel(username, isFollowers))
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
                // Gets back to the profile page of the user
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("username", viewModel.username)
                context.startActivity(intent)})
            {
                Image(
                    painterResource(id = R.drawable.back_button),
                    contentDescription = "Back button",
                    modifier = Modifier
                        .padding(paddingValue)
                        .size(120.dp)
                )
            }
            Spacer(modifier = Modifier.width(largeSeparator))

            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center){
                Text(
                    text = if (viewModel.isFollowersList) "Followers" else "Followings",
                    style = TextStyle(fontSize = titleFontSize),
                    modifier = Modifier.padding(paddingValue)
                )
            }
        }

        Spacer(modifier = Modifier.height(smallSeparator))

        //Here only to show the feature. Will be deleted.
        repeat(3){
            CreateProfilePreview(user = user)
        }

    }
}

@Composable
fun CreateProfilePreview(user: User){
    Row(modifier = Modifier
        .padding(all = paddingValue)
        .clickable {}
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(roundedCornerValue))
        .background(Color.DarkGray)
    ) {
        Image(
            painter = painterResource(user.profile.profilePictureId),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(itemSize)
                .clip(CircleShape)
                .padding(start = 10.dp)
        )

        Spacer(modifier = Modifier.width(smallSeparator))

        Column (
            modifier = Modifier.height(itemSize),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center) {
            Text(
                text = user.username,
                style = TextStyle(fontSize = textFontSize, fontWeight = FontWeight.Bold)
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
            painter = painterResource(user.profile.profilePictureId),
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
