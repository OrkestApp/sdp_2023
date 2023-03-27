package com.github.orkest.View.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.R


@Composable
fun FeedActivity(){
    //Add the post
    Post()

}

@Composable
fun Post(){

    Row(modifier = Modifier
        .padding(20.dp)
        .clip(shape = RoundedCornerShape(20.dp))
        //.background(Color.hsl(60f, 0.16f, 0.92f))
        //.background(Color.hsl(180f, 0.11f, 0.98f))
        .background(Color.DarkGray)){


        Column {

            // Display the user profile pic
            UserProfilePic()


            //Display the reaction buttons
            Reaction()
        }



        Column(modifier = Modifier.padding(10.dp, top = 10.dp, end = 10.dp)) {
            //Add the user's username
            UserUsername()

            // Display the post's content
            PostDescription()
            Spacer(modifier = Modifier.height(10.dp))

            // Display the post's song
            SongCard()
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun UserUsername(){
    //Add the user's username
    Text(text = "Username", fontSize = 14.sp, fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
          //  .padding(start = 10.dp)
            .clickable {  })
}

@Composable
fun UserProfilePic(){
        //Add the user's profile pic
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
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
fun PostDescription(){
        //Add the post's content
        Text(text = "Post Content",
            fontSize = 15.sp,
            color = Color.White,
            maxLines = 2)
}

@Composable
fun SongCard(){

    //Row containing the song's album pic, info and play button
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.hsl(54f, 1f, 0.5f))
                .padding(end = 10.dp)){

            //Add the song's info at the left of the card
            SongInfo()

            Spacer(modifier = Modifier.width(5.dp))

            //Add a play button at the right of the card
            PlayButton()
        }
}

@Composable
fun SongInfo(){

    Row(Modifier.padding(10.dp)) {
        //Add the song's picture at the left of the card
        Image(
            painter = painterResource(id = R.drawable.album_cover),
            contentDescription = "Cover of the album of the song Rude Boy by Rihanna",
            modifier = Modifier
                .height(80.dp)
                .clip(shape = RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(verticalArrangement = Arrangement.SpaceEvenly) {

            //Add the song's title at the right of the card
            Text(text = "Rude Boy", fontSize = 25.sp)
            //Add the song's artist at the right of the card
            Text(text = "Rihanna", color = Color.Gray, fontSize = 18.sp)
            //Add the song's album at the right of the card
            Text(text = "Rated R", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun PlayButton(){
    Icon(painter = painterResource(id = R.drawable.play_button),
        contentDescription = "Play button",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { })
}

@Composable
fun Reaction(){
    Column(modifier = Modifier.padding(20.dp)) {
        // Create the like button
        Icon(painter = painterResource(id = R.drawable.black_like_icon),
            contentDescription = "Like button",
            tint = Color.White,
            modifier = Modifier
                .testTag("like_button")
                .height(20.dp)
                .width(20.dp)
                .clickable { })
       //Text(text = "20", color = Color.White, fontSize = 9.sp)

        Spacer(modifier = Modifier.height(10.dp))

        //Create the comment button
        Icon(painter = painterResource(id = R.drawable.comment_icon),
            contentDescription = "Comment button",
            tint = Color.White,
            modifier = Modifier
                .testTag("comment_button")
                .height(20.dp)
                .width(20.dp)
                .clickable { })
//       Text(text = "10", color = Color.White, fontSize = 9.sp,
//       textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(10.dp))

        //Create the share button
        Icon(painter = painterResource(id = R.drawable.share_icon),
            contentDescription = "Share button",
            tint = Color.White,
            modifier = Modifier
                .testTag("share_button")
                .height(20.dp)
                .width(20.dp)
                .clickable { })
       // Text(text = "5", color = Color.White, fontSize = 9.sp)

    }

}


@Preview
@Composable
fun PreviewSongCard(){
Post()

}