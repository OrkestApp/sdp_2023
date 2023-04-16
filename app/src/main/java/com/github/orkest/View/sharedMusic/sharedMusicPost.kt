package com.github.orkest.View.sharedMusic

import android.graphics.Typeface.BOLD
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.Constants
import com.github.orkest.Model.Song
import com.github.orkest.Model.User
import com.github.orkest.R
import com.github.orkest.View.feed.*


private val separator = 10.dp
private val fontSize = 14.sp
private val smallFontSize = 12.dp

@Preview
@Composable
fun sharedMusicPost() {
    val song = Song("Bad boy", "Rihanna", "Rated R")
    val message = "Check this out bro. It's amazing. It reminds me of summer 2012."
    Row(modifier = Modifier
        .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.DarkGray)
        ) {
            Row(modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
                Column(Modifier.weight(1f)) {
                    UserInfo(username = "John", userPicture = -1)
                    Spacer(modifier = Modifier.height(separator))
                    Message(message = message)
                }
                Spacer(modifier = Modifier.width(separator))
                AlbumCover(pictureId = -1)
            }
            Spacer(modifier = Modifier.height(separator))
            Row(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color.Yellow)
                ) {
                    Column(Modifier.weight(1f)){ playMusic(song)}
                    AddToPlaylistButton()
            }
        }
    }
}

@Composable
fun playMusic(song: Song) {
    Row(modifier = Modifier
        .padding(5.dp)
        .clip(shape = RoundedCornerShape(10.dp))) {
            PlayButton()
            Spacer(modifier = Modifier.width(separator))
            Text(text = song.name, fontWeight = FontWeight.Bold, fontSize = fontSize)
            Text(" - ", fontSize = fontSize)
            SongInfo(author = song.author, album = song.album)
    }
}

@Composable
fun Message(message: String){
    Text(
        text = message,
        fontWeight = FontWeight.Normal
    )
}
@Composable
fun AddToPlaylistButton(){
    Image(painter = painterResource(id = R.drawable.add_to_playlist),
        contentDescription = "Add to playlist",
        modifier = Modifier
            .padding(5.dp)
            .height(20.dp)
            .width(20.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { })
}

@Composable
fun AlbumCover(pictureId: Int){
    Image(
        painter = painterResource(id = if(pictureId == -1) R.drawable.album_cover else pictureId ),
        contentDescription = "Cover of the album",
        modifier = Modifier
            .height(80.dp)
            .clip(shape = RoundedCornerShape(10.dp))
    )
}

@Composable
fun UserInfo(username: String, userPicture: Int){
    Row {
        //Display the user profile pic
        Image(
            painter = painterResource(id = if (userPicture == -1) R.drawable.blank_profile_pic else userPicture),
            contentDescription = "Profile Picture of the user",
            modifier = Modifier
                //.padding(start = 10.dp, top = 10.dp)
                .height(20.dp)
                .width(20.dp)
                .clip(shape = CircleShape)
                .clickable { }
        )
        Spacer(modifier = Modifier.width(separator))
        //Display the informative text
        //Text(text = "$username ", fontWeight = FontWeight.Bold)
        val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)
        val lightStyle = SpanStyle(fontWeight = FontWeight.Light)

        val text = buildAnnotatedString {
            val username = "JohnDoe"
            withStyle(boldStyle) { append(username) }
            withStyle(lightStyle) { append(" shared this music with you") }
        }

        Text(text = text)

    }
}

@Composable
fun SongInfo(author: String, album: String){
    Row {
        Text(text ="$author ($album)",
            fontWeight = FontWeight.Thin,
            fontSize = fontSize
        )
    }
}

@Composable
private fun PlayButton(){
    Icon(painter = painterResource(id = R.drawable.play_button),
        contentDescription = "Play button",
        modifier = Modifier
            .height(20.dp)
            .width(20.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { })
}
