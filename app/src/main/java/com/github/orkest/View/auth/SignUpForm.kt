package com.github.orkest.View.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.github.orkest.Model.Providers
import com.github.orkest.R
import com.github.orkest.View.MainActivity
import com.github.orkest.View.theme.OrkestTheme
import com.github.orkest.View.theme.White
import com.github.orkest.ViewModel.auth.AuthViewModel


/**
 * The form used to create a new user
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpForm(viewModel: AuthViewModel) {

    // Value storing the context, used later to launch the Main Activity
    val context = LocalContext.current

    // Value used for the dropDown menu selecting the providers
    val expanded = remember { mutableStateOf(false) }

    // Value used to display an error message if the username already exists
    val userExists = remember { mutableStateOf(false) }

    // Font used for the title
    val marker = FontFamily(
        Font(R.font.permanentmarker_regular, FontWeight.Normal)
    )

    val userDoesntExist = remember {
        mutableStateOf(false)
    }

    Scaffold(
        //Creates the top bar
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Your Profile",
                                color = Color.White) },

                backgroundColor = Color.Black
            )},

        content = {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Title of the page
                Text(
                    text = "Create Your Profile",
                    style = TextStyle(fontSize = 35.sp, fontFamily = marker),
                    color = Color.Black
                )

                AddSpace()

                // Field to enter the username
                TextField(
                    label = { Text(text = "Username") },
                    value = viewModel.getUsername(),
                    onValueChange = { viewModel.updateUsername(it) }
                )


                //Displays an error when the username already exists
                AnimatedVisibility(visible = userExists.value){
                    Text(text = "This username already exists!",
                        color = Color.Red,
                        modifier = Modifier.width(280.dp))
                }

                AddSpace()

                // Field to enter the bio description
                TextField(
                    modifier = Modifier.width(280.dp),
                    label = { Text(text = "Profile Description") },
                    value = viewModel.getBio(),
                    onValueChange = { viewModel.updateBio(it) },
                )

                AddSpace()

                // Box with button to choose the service provider from a dropDownMenu
                ChooseServiceProvider(viewModel, expanded)

                //Button to confirm choices
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = { viewModel.createUser()
                            .whenComplete { result, _ ->
                                if(result) {
                                    //Launches intent to the main Activity
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.putExtra("username",viewModel.getUsername().text)
                                    context.startActivity(intent)
                                } else {
                                    //Displays error
                                    userExists.value = true
                                }
                            }
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Create Profile",
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))


                TextField(
                    label = { Text(text = "Current Username") },
                    value = viewModel.getCurrentUsername(),
                    onValueChange = { viewModel.updateCurrentUsername(it) }
                )

                AnimatedVisibility(visible = userDoesntExist.value){
                    Text(text = "No permissions for this user!",
                        color = Color.Red,
                        modifier = Modifier.width(280.dp))
                }

                Spacer(modifier = Modifier.height(15.dp))

                //sign in button
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = { viewModel.signInUser()
                                    .whenComplete { result, _ ->
                                    if(result) {
                                        //Launches intent to the main Activity
                                        val intent = Intent(context, MainActivity::class.java)
                                        intent.putExtra("username",viewModel.getCurrentUsername().text)
                                        context.startActivity(intent)
                                    } else {
                                        //Displays error
                                        userDoesntExist.value = true
                                    }
                                   }
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Sign In",
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
            }
        })
}

/**
 * Creates the button to confirm and start the creation of the profile
 */
@Composable
private fun confirmButton(viewModel: AuthViewModel, context: Context, userExists: MutableState<Boolean>){
    val error = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }


    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        val waiting = remember { mutableStateOf(false) }

        Button(
            onClick = { onConfirmListener(context,error,errorMessage,waiting, userExists, viewModel) },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)

        ) {
            Text(text = if (waiting.value) "Creating Profile" else "Create Profile",
                style = TextStyle(fontSize = 20.sp),
                color = Color.White
            )
        }
    }

    // Display of the error
    Box{
        AnimatedVisibility(visible = error.value,
            enter= EnterTransition.None,
            exit = ExitTransition.None) {
            Text(text = errorMessage.value.
                        ifEmpty { "An error occurred while creating your profile"},
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(280.dp)
                    .align(Alignment.Center).padding(40.dp))
        }
    }

}


/**
 * Function called when the confirm button is pressed
 */
private fun onConfirmListener(context: Context, error: MutableState<Boolean>, errorMessage: MutableState<String>,
                        waiting: MutableState<Boolean>,userExists: MutableState<Boolean>, viewModel: AuthViewModel){

    // Resets the values used for error handling
    error.value = false
    waiting.value = true
    userExists.value = false

    // Calls the function to create the user
    viewModel.createUser().whenComplete { result, e ->
        waiting.value = false
        if (e != null){
            // Print the Exception Message on the UI
            error.value = true
            errorMessage.value = e.message?:""

        }else{
            if(result) {
                //Launches intent to the main Activity
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("username",viewModel.getUsername().text)
                context.startActivity(intent)
            } else {
                //Displays error
                userExists.value = true
            }
        }
    }
}

/**
 * Adds a space between the different elements of the page
 */
@Composable
private fun AddSpace(){
    Spacer(modifier = Modifier.height(15.dp))
}

/**
 * Creates the box component for the choice of the service provider
 */
@Composable
private fun ChooseServiceProvider(viewModel: AuthViewModel, expanded: MutableState<Boolean>){

    val logos = mapOf(
        Providers.SPOTIFY to R.drawable.spotify_logo,
        Providers.DEEZER to R.drawable.deezer_logo,
        Providers.APPLE_MUSIC to R.drawable.apple_music_logo,
    )

    Box() {
        // The button that will be displayed for the choice of the provider
       providerButtonContent(viewModel = viewModel, expanded = expanded , logos = logos)

        dropDownMenu(viewModel = viewModel, expanded = expanded, logos = logos)


    }
}

/**
 * Creates the button of the dropDownMenu for the choice of the service provider
 */
@Composable
@OptIn(ExperimentalCoilApi::class)
private fun providerButtonContent(viewModel: AuthViewModel, expanded: MutableState<Boolean>, logos: Map<Providers, Int>){

    Button(
        colors= ButtonDefaults.buttonColors(backgroundColor = Color(217,217,217)),
        onClick = {expanded.value = true},
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .width(280.dp)
            .height(50.dp)
            .background(Color.Gray)
            .testTag("providerButton")
    ) {
        // Logo of the provider
        Image(
            painter = rememberImagePainter(logos[viewModel.getProvider()]!!),
            contentDescription = "Logo of the provider",
            modifier = Modifier
                .size(40.dp)
                .padding(start = 0.dp)
                // Add alignment at the end of the row
                .alignByBaseline()
        )

        // Name of the provider
        Text(
            text = viewModel.getProvider().value,
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .padding(start = 20.dp)

                .align(Alignment.CenterVertically)
        )

        // Spacer to push the Icon to the right edge of the Button
        Spacer(modifier = Modifier.weight(1f))

        // Arrow to indicate that the button is clickable
        Icon(
            painter = painterResource(R.drawable.down_arrow),
            contentDescription = "Arrow to indicate that the button is clickable",
            modifier = Modifier
                .size(30.dp)
                .padding(end = 10.dp)
                // Add alignment at the end of the row
                .align(Alignment.CenterVertically)
        )
    }

}

/**
 * Creates the dropDownMenu for the choice of the service provider
 */
@Composable
private fun dropDownMenu(viewModel: AuthViewModel, expanded: MutableState<Boolean>, logos: Map<Providers, Int>){
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
            .background(White)
            .width(280.dp)
    ) {
        Providers.values().forEachIndexed { _, option ->
            DropdownMenuItem(
                onClick = {
                    viewModel.updateProvider(option)
                    expanded.value = false
                },
                modifier = Modifier
                    .testTag("option ${option.value}")
            ) {
                // Logo of the provider
                Image(
                    painter = painterResource(logos[option]!!),
                    contentDescription = "Logo of the provider",
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(40.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))


                Text(text = option.value,
                    modifier = Modifier
                        .padding(start = 20.dp))

            }
        }
    }
}


@Preview
@Composable
fun Preview (){
    OrkestTheme {
        // A surface container using the 'background' color from the theme
        androidx.compose.material3.Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SignUpForm(AuthViewModel())
        }
    }
}






