package com.github.orkest.View.feed

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.Constants
import com.github.orkest.Model.*
import com.github.orkest.R
import com.github.orkest.ViewModel.post.PostViewModel


/**
 * Composable function for the feed screen
 * Represents the view of the MVVM pattern
 */
@Composable
fun FeedActivity(viewModel: PostViewModel) {

    //Add a list of posts
    var listPosts by remember {
        mutableStateOf( ArrayList<Post>().toList())
    } //Add the .toList() to always store an immutable collection to avoid unpredictable behavior

    //Fetch posts from database
    //viewModel.getUserPosts("Yas")
    viewModel.getRecentPosts(Constants.DUMMY_LAST_CONNECTED_TIME)
        .whenComplete { t, u ->
            if (t != null) {
                listPosts = t
            }
        }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        items(listPosts) { post ->
            DisplayPost(post = post)
        }
    }

    val context = LocalContext.current

    //Add a button to create a new post
    FloatingActionButton(
        modifier = Modifier
            .padding(10.dp),
        onClick = { launchCreatePostActivity(context) }) {
        Icon(painter = painterResource(id = R.drawable.add_button), contentDescription = "Add post")
    }
}

fun launchCreatePostActivity(context: Context){
    val intent = Intent(context, CreatePost::class.java)
    context.startActivity(intent)
}


/**
 * Composable function to display a post, can be reused for the profile page
 * @param post the post to display
 */
@Composable
fun DisplayPost(post: Post){

    Row(modifier = Modifier
        .padding(start = 25.dp, top = 20.dp)
        .clip(shape = RoundedCornerShape(20.dp))
        .background(Color.DarkGray)){

        Column {
            // Display the user profile pic
            ProfilePic(post.profilePicId)
            //Display the reaction buttons
            Reaction(post)
        }

        Column(modifier = Modifier.padding(10.dp, top = 10.dp, end = 10.dp)) {
            //Add the user's username
            Username(post.username)
            // Display the post's content
            PostDescription(post.postDescription)
            Spacer(modifier = Modifier.height(10.dp))
            // Display the post's song
            SongCard(post.song)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun Username(username: String){
    //Add the user's username
    Text(text = username, fontSize = 14.sp, fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .clickable {  })
}

@Composable
private fun ProfilePic(profilePicId : Int){
        //Add the user's profile pic
        Image(
            painter = painterResource(id = profilePicId),
            contentDescription = "Profile Picture of the user",
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
                .height(40.dp)
                .width(40.dp)
                .clip(shape = CircleShape)
                .clickable { }
        )
}

@Composable
private fun PostDescription(postDescription: String){
        //Add the post's content
        Text(text = postDescription,
            fontSize = 15.sp,
            color = Color.White,
            maxLines = 2)
}


/**
 * Composable function that displays the song's info and a play button
 * Can be reused for the sharedWithMe page and profile page
 * @param song the song to display
 */
@Composable
fun SongCard(song: Song){

    //Row containing the song's album pic, info and play button
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.hsl(54f, 1f, 0.5f))
                .padding(end = 10.dp)){

            //Add the song's info at the left of the card
            SongInfo(song)

            Spacer(modifier = Modifier.width(5.dp))

            //Add a play button at the right of the card
            PlayButton(song)
        }
}

@Composable
private fun SongInfo(song: Song){

    Row(Modifier.padding(10.dp)) {
        //Add the song's picture at the left of the card
        Image(
            painter = painterResource(id = if(song.pictureId == -1) R.drawable.album_cover else song.pictureId ),
            contentDescription = "Cover of the album of the song Rude Boy by Rihanna",
            modifier = Modifier
                .height(80.dp)
                .clip(shape = RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(verticalArrangement = Arrangement.SpaceEvenly) {

            //Add the song's title at the right of the album
            Text(text = song.name, fontSize = 25.sp)
            //Add the song's artist at the right of the album
            Text(text = song.author, color = Color.Gray, fontSize = 18.sp)
            //Add the song's album at the right of the album
            Text(text = song.album, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun PlayButton(song: Song){
    val isPlayed = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Icon(painter = if (!isPlayed.value) painterResource(id = R.drawable.play_button)
                    else painterResource(id = R.drawable.pause_button),
        contentDescription = if (!isPlayed.value) "Play button" else "Pause button",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable {
                Constants.playMusicButtonClicked(song, isPlayed)
            })
}

@Composable
private fun Reaction(post: Post){
    //val context = LocalContext.current
    Column(modifier = Modifier.padding(20.dp)) {
        // Create the like button
        ReactionIcon(R.drawable.black_like_icon,"Like button", "like_button" )
        Spacer(modifier = Modifier.height(10.dp))

        //Create the comment button
        val context = LocalContext.current
        IconButton(
            modifier = Modifier.testTag("comment_button").height(20.dp).width(20.dp),
            onClick = { context.startActivity(Intent(context, CommentActivity::class.java)
                .putExtra("post_date", post.date.toString())
                .putExtra("post_username", post.username))
            }
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.comment_icon),
                contentDescription = "comment_button",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        //Create the share button
        ReactionIcon(R.drawable.share_icon,"Share button", "share_button" )
    }
}

/* TODO modularize in next sprint */

@Composable
private fun ReactionIcon(iconId: Int, contentDescription:String, testTag: String) {
    Icon(painter = painterResource(id = iconId),
        contentDescription = contentDescription,
        tint = Color.White,
        modifier = Modifier
            .testTag(testTag)
            .height(20.dp)
            .width(20.dp)
            .clickable { })
}


@Preview
@Composable
fun PreviewSongCard(){
    FeedActivity(PostViewModel())
}