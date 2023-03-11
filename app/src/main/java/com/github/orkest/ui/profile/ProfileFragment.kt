package com.github.orkest.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.github.orkest.DataModel.Profile

class ProfileFragment {

    private val topInterfaceHeight = 150.dp
    private val separator = 10.dp
    private val fontSize = 16.sp
    private val smallFontSize = 12.sp


    @Composable
    fun UserName(name: String){
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
    }

    @Composable
    fun Description(bio: String){
        Text(
            text = bio,
            fontSize = fontSize)
    }

    @Composable
    fun NbFollowers(nb: Int){
        ClickableText(
            text = AnnotatedString(if (nb > 1) "$nb\nfollowers" else "$nb\nfollower"),
            onClick = {},
            style = TextStyle(
                fontSize = fontSize
            )
        )
    }

    @Composable
    fun NbFollowings(nb: Int){
        ClickableText(
            text = AnnotatedString(if (nb > 1) "$nb\nfollowings" else "$nb\nfollowing"),
            onClick = {},
            style = TextStyle(
                fontSize = fontSize
            )
        )
    }

    @Composable
    fun EditButton(onClick:() -> Unit){
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(topInterfaceHeight / 4)
                .width((3 * topInterfaceHeight) / 4)

        ){
            Text(
                text ="Edit Profile",
                fontSize = smallFontSize)
        }
    }

    @Composable
    fun ProfilePicture(id: Int){
        Image(
            painter = painterResource(id = id),
            contentDescription = "Profile picture",
            modifier = Modifier
                .width((3 * topInterfaceHeight) / 4)
                .clip(CircleShape)
        )
    }


    @Composable
    fun UpdateData(profile: Profile) {
        Column{
            Row(Modifier.height(IntrinsicSize.Min)){//allows to make fillMaxHeight relatively
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfilePicture(id = profile.profilePictureId)
                }

                //Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(separator))

                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row{ UserName(profile.username) }
                    Row{
                        //Separate followers/followings in an even way
                        Column(modifier = Modifier.weight(1f)) { NbFollowers(profile.nbFollowers) }
                        Column(modifier = Modifier.weight(1f)) { NbFollowings(profile.nbFollowing) }
                    }
                    Row{ Description(profile.bio) }
                }
            }

            Spacer(modifier = Modifier.height(separator))

            Row(){
                EditButton() {}
            }

        }

    }
}