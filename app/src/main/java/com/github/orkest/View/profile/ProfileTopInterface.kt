package com.github.orkest.View.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
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
import com.github.orkest.ViewModel.profile.ProfileViewModel

class ProfileTopInterface() {

    private val topInterfaceHeight = 150.dp
    private val separator = 10.dp
    private val fontSize = 16.sp
    private val smallFontSize = 12.sp

    @Composable
    fun UserName(username: String){
        Text(
            text = username,
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
                style = TextStyle( fontSize = fontSize )
        )
    }

    @Composable
    fun NbFollowings(nb: Int){
        ClickableText(
                text = AnnotatedString(if (nb > 1) "$nb\nfollowers" else "$nb\nfollower"),
                onClick = {},
                style = TextStyle( fontSize = fontSize )
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
    fun ProfilePicture(profilePictureId: Int){
        Image(
            painter = painterResource(id = profilePictureId),
            contentDescription = "Profile picture",
            modifier = Modifier
                .width((3 * topInterfaceHeight) / 4)
                .clip(CircleShape)
        )
    }


    @Composable
    fun TopInterfaceStructure(viewModel: ProfileViewModel) {
        Column{
            Row(Modifier.height(IntrinsicSize.Min)){//allows to make fillMaxHeight relatively
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    viewModel.getProfilePictureId().value?.let { ProfilePicture(it) }
                }

                //Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(separator))

                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row{ viewModel.getUsername().value?.let { UserName(it) } }
                    Row{
                        //Separate followers/followings in an even way
                        Column(modifier = Modifier.weight(1f)) { viewModel.getNbFollowers().value?.let { NbFollowers(it) } }
                        Column(modifier = Modifier.weight(1f)) { viewModel.getNbFollowings().value?.let { NbFollowings(it) } }
                    }
                    Row{ viewModel.getBio().value?.let { Description(it) } }
                }
            }

            Spacer(modifier = Modifier.height(separator))

            Row(){
                EditButton() {}
            }

        }



    }

}