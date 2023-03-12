package com.github.orkest.View.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
import androidx.compose.material.*
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
import com.github.orkest.ViewModel.profile.ProfileViewModel

class ProfileTopInterface(viewModel: ProfileViewModel) {

    private val topInterfaceHeight = 150.dp
    private val separator = 10.dp
    private val fontSize = 16.sp
    private val smallFontSize = 12.sp

    @Composable
    fun UserName(viewModel: ProfileViewModel){
        TextField(
            value = viewModel.getUsername(),
            onValueChange = { viewModel.updateUsername(it)},
            label = { Text(
                text = "name",
                fontWeight = FontWeight.Bold,
                fontSize = fontSize ) }
        )
    }

    @Composable
    fun Description(viewModel: ProfileViewModel){
        TextField(
            value =  viewModel.getBio(),
            onValueChange = { viewModel.updateBio(it) },
            label = { Text(
                text = "bio",
                fontSize = fontSize) }
        )
    }

    @Composable
    fun NbFollowers(viewModel: ProfileViewModel){
        TextField(
            value = viewModel.getNbFollowers(),
            onValueChange = { viewModel.updateNbFollowers(it) },
            label = { ClickableText(
                text = AnnotatedString(/*if (nb > 1) "$nb\nfollowers" else "$nb\nfollower"*/ "3"),
                onClick = {},
                style = TextStyle( fontSize = fontSize )
            )}
        )
    }

    @Composable
    fun NbFollowings(viewModel: ProfileViewModel){
        TextField(
            value = viewModel.getNbFollowings(),
            onValueChange = { viewModel.updateNbFollowings(it) },
            label = { ClickableText(
                text = AnnotatedString(/*if (nb > 1) "$nb\nfollowers" else "$nb\nfollower"*/ "4"),
                onClick = {},
                style = TextStyle( fontSize = fontSize )
            )}
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
    fun ProfilePicture(viewModel: ProfileViewModel){
        Image(
            painter = painterResource(id = viewModel.getProfilePictureId().text.toInt()),
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
                    ProfilePicture(viewModel)
                }

                //Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(separator))

                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row{ UserName(viewModel) }
                    Row{
                        //Separate followers/followings in an even way
                        Column(modifier = Modifier.weight(1f)) { NbFollowers(viewModel) }
                        Column(modifier = Modifier.weight(1f)) { NbFollowings(viewModel) }
                    }
                    Row{ Description(viewModel) }
                }
            }

            Spacer(modifier = Modifier.height(separator))

            Row(){
                EditButton() {}
            }

        }

    }

}