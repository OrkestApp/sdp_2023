package com.github.orkest.View.profile

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.github.orkest.Model.Profile
import com.github.orkest.R
import com.github.orkest.View.EditProfileActivity
import com.github.orkest.View.MainActivity
import com.github.orkest.ViewModel.profile.ProfileViewModel

private val topInterfaceHeight = 150.dp
private val separator = 10.dp
private val fontSize = 16.sp
private val smallFontSize = 12.sp

/**
 * The top interface of the user's profile displaying the user's information
 * username, bio, number of followers, number of followings, profile picture
 */
@Composable
fun ProfileTopInterface(viewModel: ProfileViewModel) {

    val context = LocalContext.current
    viewModel.setupListener()

    Column{
        Row(Modifier.height(IntrinsicSize.Min)){//allows to make fillMaxHeight relatively
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                 ProfilePicture(viewModel.profilePictureId.observeAsState().value)
            }

            //Add a horizontal space between the image and the user's info
            Spacer(modifier = Modifier.width(separator))

            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row{ UserName(viewModel.username.observeAsState().value) }
                Row{
                    //Separate followers/followings in an even way
                    Column(modifier = Modifier.weight(1f)) { NbFollowers(number(viewModel.nbFollowers.observeAsState().value)) }
                    Column(modifier = Modifier.weight(1f)) { NbFollowings(number(viewModel.nbFollowings.observeAsState().value)) }
                }
                Row{ Description(viewModel.bio.observeAsState().value) }
            }
        }

        Spacer(modifier = Modifier.height(separator))

        Row(){
            EditButton {
                val intent = Intent(context, EditProfileActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun UserName(username: String?){
    Text(
        text = username ?: "",
        fontWeight = FontWeight.Bold,
        fontSize = fontSize
    )
}

@Composable
fun Description(bio: String?){
    Text(
        text = bio ?: "Description",
        fontSize = fontSize)
}

//Returns default int if the observer is null
//Here because of warning regarding the workload of the composable functions
fun number(nb: Int?): Int{
    return nb ?: 0
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
        text = AnnotatedString(if (nb > 1) "$nb\nfollowings" else "$nb\nfollowing"),
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
fun ProfilePicture(profilePictureId: Int?){
    val picture = profilePictureId ?: R.drawable.profile_picture
    Image(
        painter = painterResource(id = picture),
        contentDescription = "$picture",
        modifier = Modifier
            .width((3 * topInterfaceHeight) / 4)
            .clip(CircleShape)
    )
}


