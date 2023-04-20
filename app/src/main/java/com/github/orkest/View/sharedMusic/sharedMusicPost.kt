package com.github.orkest.View.sharedMusic


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.github.orkest.Model.Profile
import com.github.orkest.Model.Song
import com.github.orkest.R


private val separator = 10.dp
private val fontSize = 14.sp
private val paddingValue = 10.dp
private val roundedCornerValue = 10.dp

/**
 * This function creates the UI for post when a user shared a music with the current logged in user
 * profile: Profile = the profile of the user that shared the song. Used the class Profile to get access to the username and the profile picture ID.
 * song: Song = song that was shared from the service provider.
 * message: String = the user has the possibility to add a customizable message.
 *
 * This only represents the UI and the parameters can be adapted to what will be implemented in the backend
 */

@Composable
fun sharedMusicPost(profile: Profile, song: Song, message: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = paddingValue, top = paddingValue, end = paddingValue)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.DarkGray)
        ) {
            Row(modifier = Modifier.padding(start = paddingValue, top = paddingValue, end = paddingValue)) {
                Column(Modifier.weight(1f)) {
                    UserInfo(username = profile.username, userPicture = profile.profilePictureId)
                    Spacer(modifier = Modifier.height(separator))
                    Message(message = message)
                }
                Spacer(modifier = Modifier.width(separator))
                //SongCover(pictureId = song.pictureId)
            }
            Spacer(modifier = Modifier.height(separator))
            Row(modifier = Modifier
                .padding(paddingValue)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(roundedCornerValue))
                .background(Color.hsl(54f, 1f, 0.5f))
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
        .clip(shape = RoundedCornerShape(roundedCornerValue))) {
            PlayButton()
            Spacer(modifier = Modifier.width(separator))
            Text(text = song.Title, fontWeight = FontWeight.Bold, fontSize = fontSize)
            Text(" - ", fontSize = fontSize)
            SongInfo(author = song.Artist, album = song.Album)
    }
}

//Displays the customized message sent from the user
@Composable
fun Message(message: String){
    Text(
        text = message,
        fontWeight = FontWeight.Normal
    )
}
@Composable
fun AddToPlaylistButton(){
    Icon(painter = painterResource(id = R.drawable.add_to_playlist),
        contentDescription = "Add to playlist",
        modifier = Modifier
            .padding(5.dp)
            .height(20.dp)
            .width(20.dp)
            .clip(shape = RoundedCornerShape(roundedCornerValue))
            .clickable { })
}

@Composable
fun SongCover(pictureId: Int){
    Image(
        painter = painterResource(id = if(pictureId == -1) R.drawable.album_cover else pictureId ),
        contentDescription = "Cover of the album",
        modifier = Modifier
            .height(80.dp)
            .clip(shape = RoundedCornerShape(roundedCornerValue))
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
                .height(20.dp)
                .width(20.dp)
                .clip(shape = CircleShape)
                .clickable { }
        )
        Spacer(modifier = Modifier.width(separator))

        //Displays the "user has shared this music with you" message
        val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)
        val lightStyle = SpanStyle(fontWeight = FontWeight.Light)
        val text = buildAnnotatedString {
            withStyle(boldStyle) { append(username) }
            withStyle(lightStyle) { append(" shared this music with you") }
        }
        Text(text = text)
    }
}

//Displays the song's author and album
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
            .clip(shape = RoundedCornerShape(roundedCornerValue))
            .clickable { })
}
