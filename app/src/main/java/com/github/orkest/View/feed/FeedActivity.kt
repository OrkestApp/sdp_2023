package com.github.orkest.View.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.R


@Composable
fun FeedActivity(){



}

@Composable
fun SongCard(){

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(Color.White)
        .padding(10.dp)
        .clip(shape = RoundedCornerShape(10.dp))) {

        Row {
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

            //Add a play button at the right of the card
            Image(
                painter = painterResource(id = R.drawable.play_button),
                contentDescription = "Play button",
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .clickable {  }
            )

        }
    }


}


@Preview
@Composable
fun PreviewSongCard(){
    SongCard()
}