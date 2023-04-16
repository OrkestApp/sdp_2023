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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.Constants
import com.github.orkest.Model.Song
import com.github.orkest.R
import com.github.orkest.View.feed.*


private val separator = 10.dp

@Preview
@Composable
fun sharedMusicPost() {
    val song = Song("Shape of you", "Ed Sheeran", "Love yourself")
    Row(modifier = Modifier
        .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                //.padding(start = 25.dp, top = 20.dp)
                //.clip(shape = RoundedCornerShape(20.dp))
                .background(Color.DarkGray)
        ) {
            Row(modifier = Modifier.padding(start = 15.dp, top = 10.dp)) {
                Column(Modifier.weight(1f)) {
                    UserInfo(username = "JohnDoe", userPicture = -1)
                    Spacer(modifier = Modifier.height(separator))
                    SongInfo(author = song.name, album = song.album)
                }
                Spacer(modifier = Modifier.width(separator))
                Column() {
                    AlbumCover(pictureId = -1)
                }
            }
            Row(modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)) {
                PlayButton()
                Spacer(modifier = Modifier.width(separator))
                Text(song.name)
            }
        }
    }
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
        Text(text = "$username ", fontWeight = FontWeight.Bold)
        Text(text = "has shared this music with you")
    }
}

@Composable
fun SongInfo(author: String, album: String){
    Row {
        Text(text ="$author, $album",
        fontWeight = FontWeight.Thin
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
