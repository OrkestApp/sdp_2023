package com.github.orkest.View.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.Model.Post
import com.github.orkest.Model.Song
import com.github.orkest.R
import java.time.LocalDateTime


/**
 * Composable function for the feed screen
 * Represents the view of the MVVM pattern
 */
@Composable
fun FeedActivity(){
    //Add a list of posts
    // Once the backend will be implemented, this list will be filled with the posts from the database
    val rudeBoySong = Song("Rude Boy", "Rihanna", "Rated R",
                "link", R.drawable.album_cover)
    val post = Post("Username",LocalDateTime.now(), R.drawable.profile_picture, "Post Description", rudeBoySong, 0, ArrayList())

    var listPosts by remember { mutableStateOf(mutableListOf(post,post,post,post)) }
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)) {
        items(listPosts) { post ->
            DisplayPost(post = post)
        }
    }
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
            Reaction()
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
            PlayButton()
        }
}

@Composable
private fun SongInfo(song: Song){

    Row(Modifier.padding(10.dp)) {
        //Add the song's picture at the left of the card
        Image(
            painter = painterResource(id = song.pictureId),
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
private fun PlayButton(){
    Icon(painter = painterResource(id = R.drawable.play_button),
        contentDescription = "Play button",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { })
}

@Composable
private fun Reaction(){
    Column(modifier = Modifier.padding(20.dp)) {
        // Create the like button
        ReactionIcon(R.drawable.black_like_icon,"Like button", "like_button" )
        Spacer(modifier = Modifier.height(10.dp))

        //Create the comment button
        ReactionIcon(R.drawable.comment_icon,"Comment button", "comment_button" )
        Spacer(modifier = Modifier.height(10.dp))

        //Create the share button
        ReactionIcon(R.drawable.share_icon,"Share button", "share_button" )
    }
}

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
    FeedActivity()
}