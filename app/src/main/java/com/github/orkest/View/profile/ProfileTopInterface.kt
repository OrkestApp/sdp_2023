package com.github.orkest.View.profile

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.github.orkest.Constants
import com.github.orkest.R
import com.github.orkest.View.EditProfileActivity
import com.github.orkest.View.NavDrawerButton
import kotlinx.coroutines.CoroutineScope
import com.github.orkest.ViewModel.profile.ProfileViewModel
import androidx.compose.ui.graphics.Color
import com.github.orkest.View.auth.AuthActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


private val topInterfaceHeight = 150.dp
private val separator = 10.dp
private val fontSize = 16.sp
private val smallFontSize = 13.sp
private val paddingValue = 10.dp
private val buttonSettings = Modifier
    .height(topInterfaceHeight / 4)
    .width((3 * topInterfaceHeight) / 4)
private val followColor = Color(0xFFFEE600) // bright yellow

/**
 * The top interface of the user's profile displaying the user's information
 * username, bio, number of followers, number of followings, profile picture
 */
@Composable
fun ProfileTopInterface(viewModel: ProfileViewModel, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {

    val context = LocalContext.current
    viewModel.setupListener()

    val currentUser = remember {
        viewModel.username.value
    }


    Column(Modifier
        .padding(paddingValue)){
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
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    UserName(viewModel.username.observeAsState().value)
                    NavDrawerButton(coroutineScope, scaffoldState)
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    //Separate followers/followings in an even way
                    Column(modifier = Modifier.weight(1f)) { NbFollowers(number(viewModel.nbFollowers.observeAsState().value)) }
                    Column(modifier = Modifier.weight(1f)) { NbFollowings(number(viewModel.nbFollowings.observeAsState().value)) }
                }
                Description(viewModel.bio.observeAsState().value)
            }
        }

        Spacer(modifier = Modifier.height(separator))


        Row{

            if(viewModel.username.value == Constants.CURRENT_LOGGED_USER) {
                EditButton {
                    val intent = Intent(context, EditProfileActivity::class.java)
                    context.startActivity(intent)
                }
                Spacer(modifier = Modifier.width(separator))
                Row(){
                    SignOutButton {
                        val auth = FirebaseAuth.getInstance()
                        val intent = Intent(context, AuthActivity::class.java)
                        auth.signOut()
                        //uncomment if un-caching is needed
                        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                        context.startActivity(intent)
                    }
                }
            } else {
                FollowButton(viewModel, viewModel.isUserFollowed.observeAsState().value)
            }
        }


    }
}

/**
 * The button to sign out of the app
 */
@Composable
fun SignOutButton(onClick:() -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow))
    {
        Text("Sign Out")
    }
}

@Composable
fun FollowButton(viewModel: ProfileViewModel, isUserFollowed: Boolean?){
    if(isUserFollowed == null){ Text(text="") } //Empty body, here waiting for the future that fetch isUserFollowed to complete
    else {
        //Adapts the UI to the Follow or Unfollow button
        val buttonText = if (isUserFollowed) "Unfollow" else "Follow"
        val backGroundColor = if (isUserFollowed) Color.White else followColor
        val contentColor = if (isUserFollowed) followColor else Color.White
        Button(
            onClick = { followOrUnfollow(viewModel, isUserFollowed) },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = backGroundColor, contentColor = contentColor),
            border = BorderStroke(2.dp, followColor),
            modifier = buttonSettings
        ) {
            Text(text = buttonText, fontSize = smallFontSize)
        }
    }
}

private fun followOrUnfollow(viewModel: ProfileViewModel, isUserFollowed: Boolean) {
    if (isUserFollowed) {
        //The unfollow button is displayed at this stage, when clicked on,
        //the current user unfollows the account and isUserFollowed is updated to false
        viewModel.unfollow().whenComplete { _, _ ->
            viewModel.isUserFollowed.value = false
        }
    } else {
        //The follow button is displayed at this stage, when clicked on,
        //the current user follows the account and isUserFollowed is updated to true
        viewModel.follow().whenComplete { _, _ ->
            viewModel.isUserFollowed.value = true
        }
    }
}



@Composable
fun UserName(username: String?){
    Text(
        text = username ?: "Username",
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
        modifier = buttonSettings,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray, contentColor = Color.White)
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



