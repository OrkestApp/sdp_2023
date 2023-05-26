package com.github.orkest.ui.profile

import android.content.Context
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
import com.github.orkest.data.Constants
import com.github.orkest.R
import com.github.orkest.ui.EditProfileActivity
import com.github.orkest.ui.NavDrawerButton
import kotlinx.coroutines.CoroutineScope
import androidx.compose.ui.graphics.Color
import com.github.orkest.ui.FollowListActivity
import com.github.orkest.domain.DeezerApiIntegration
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.domain.persistence.AppDatabase
import com.github.orkest.ui.authentication.AuthActivity
import com.github.orkest.ui.notification.Notification
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

import java.util.concurrent.CompletableFuture



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
                    Column(modifier = Modifier.weight(1f)) { NbFollowers(number(viewModel.nbFollowers.observeAsState().value), viewModel.user) }
                    Column(modifier = Modifier.weight(1f)) { NbFollowings(number(viewModel.nbFollowings.observeAsState().value), viewModel.user) }
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

                        //notification

                        Notification(context,null).sendNotification("Sign Out", "You have been signed out", "Sign Out", "Sign Out", 1)

                        //uncomment if un-caching is needed
                        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                        context.startActivity(intent)

                        cleanSigningCache(context)
                    }
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, DeezerApiIntegration.url)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow))
                    {
                        Text("Link Deezer Account")
                    }
                }
            } else {
                FollowButton(viewModel, viewModel.isUserFollowed.observeAsState().value)
            }
        }
    }
}


/**
 * remove caching credentials
 */
fun cleanSigningCache(context : Context ){
    val sharedPref = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        remove("username")
        remove("email")
        apply()
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
    val context = LocalContext.current
    if(isUserFollowed == null){ Text(text="") } //Empty body, here waiting for the future that fetch isUserFollowed to complete
    else {
        //Adapts the UI to the Follow or Unfollow button
        val buttonText = if (isUserFollowed) "Unfollow" else "Follow"
        val backGroundColor = if (isUserFollowed) Color.White else followColor
        val contentColor = if (isUserFollowed) followColor else Color.White
        Button(
            onClick = {
                if(FireStoreDatabaseAPI.isOnline(context)) {
                    followOrUnfollow(viewModel, isUserFollowed)
                }},
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
    CompletableFuture.allOf(viewModel.updateUserFollowers(!isUserFollowed), viewModel.updateCurrentUserFollowings(!isUserFollowed)).thenApply{
        viewModel.isUserFollowed.value = !isUserFollowed
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
fun NbFollowers(nb: Int, username: String){
    val context = LocalContext.current
    ClickableText(
        text = AnnotatedString(if (nb > 1) "$nb\nfollowers" else "$nb\nfollower"),
        onClick = {
            if(FireStoreDatabaseAPI.isOnline(context)) {
                val intent = Intent(context, FollowListActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("isFollowers", true)
                context.startActivity(intent)
            }
        },
        style = TextStyle( fontSize = fontSize )
    )
}

@Composable
fun NbFollowings(nb: Int, username: String){
    val context = LocalContext.current
    ClickableText(
        text = AnnotatedString(if (nb > 1) "$nb\nfollowings" else "$nb\nfollowing"),
        onClick = {
            if(FireStoreDatabaseAPI.isOnline(context)) {
                val intent = Intent(context, FollowListActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("isFollowers", false)
                context.startActivity(intent)
            }
        },
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
