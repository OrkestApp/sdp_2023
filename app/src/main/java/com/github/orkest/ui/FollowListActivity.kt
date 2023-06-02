package com.github.orkest.ui

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.data.User
import com.github.orkest.ui.profile.ProfileActivity
import com.github.orkest.ui.theme.OrkestTheme
import com.github.orkest.R
import com.github.orkest.domain.FireStoreDatabaseAPI


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
                FollowList(this, viewModel = FollowListViewModel(username, isFollowers))
            }
        }
    }
}


@Composable
fun FollowList(activity: ComponentActivity, viewModel: FollowListViewModel){
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.Yellow)
        ) {
            //Get back button
            IconButton(onClick = {
                // Gets back to the profile page of the user
                activity.finish()
            })
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

            //Title of the section: either "Followers" or "Followings"
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

        //Display the list of followers or followings
        val userListLiveData = viewModel.retrieveFollowList()
        val userListState by userListLiveData.observeAsState(emptyList())
        
        LazyColumn{
            items(userListState){user -> 
                CreateProfilePreview(user = user.username)
            }
        }
    }
}

@Composable
fun CreateProfilePreview(user: String, intent: Intent? = null){
    val context = LocalContext.current
    Row(modifier = Modifier
        .padding(all = paddingValue)
        .clickable {
            if (intent != null) {
                context.startActivity(intent)
            } else {
                if(FireStoreDatabaseAPI.isOnline(context)) {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("username", user)
                    context.startActivity(intent)
                }
            }
        }
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(roundedCornerValue))
        .background(Color.DarkGray)
        .testTag("Profile Row")
    ) {
        Image(
            painter = painterResource(R.drawable.blank_profile_pic), //TODO: to change when we will be able to fetch the profile pictures from the database
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
                text = user,
                style = TextStyle(fontSize = textFontSize, fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier.testTag("Username")
            )
        }
    }
}
