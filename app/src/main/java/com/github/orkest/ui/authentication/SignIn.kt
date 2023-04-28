package com.github.orkest.ui.authentication

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.orkest.R
import com.github.orkest.ui.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "Google_Sign_In_Activity"

/**
 * A login screen that offers login via Google with Firebase
 * @see [GoogleSignInFirebase](https://firebase.google.com/docs/auth/android/firebaseui?hl=fr)
 */


/**
 * The function that launches the call to firebase and the google api to sign in
 */
@Composable
fun SignIn (navController: NavController, viewModel: AuthViewModel) {

    val context = LocalContext.current
    val googleSignInClient = remember { getGoogleSignInClient(context) }
    val auth = Firebase.auth

    var isInDatabase = false
    val userNotInDb = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val waiting = remember { mutableStateOf(false) }


    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if(isInDatabase){
                            onConfirmListener(context,error,errorMessage,waiting, userNotInDb,
                                viewModel, auth, navController)
                        } else {
                            updateUI(user, navController, isInDatabase, context, viewModel)
                        }
                    } else {
                        updateUI(null, navController, isInDatabase, context, viewModel)
                    }
                }
            } catch (e: ApiException) {
                updateUI(null, navController, isInDatabase, context, viewModel)
            }
        }
    }

    /**
     * The four composable objects that are displayed :
     * the logo, the sign up button, the username and the sign in button
     */

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier.fillMaxWidth(0.5f).testTag("logo")

        )
        Button(
            onClick = { signIn(activityResultLauncher, googleSignInClient) },
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
        ) {
            Text("Sign up with Google")
        }

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            label = { Text(text = "Username") },
            value = viewModel.getUsername(),
            modifier = Modifier.fillMaxWidth(0.5f),
            onValueChange = { viewModel.updateUsername(it) }
        )

        //Displays an error when the username is not in the db
        AnimatedVisibility(visible = userNotInDb.value){
            Text(text = "This pair (username,email) doesn't exist!",
                color = Color.Red,
                modifier = Modifier.width(280.dp))
        }

        Button(
            onClick = {
                if(auth.currentUser != null) {
                    onConfirmListener(context,error,errorMessage,waiting, userNotInDb,
                        viewModel, auth, navController)
                } else {
                    isInDatabase = true
                    signIn(activityResultLauncher, googleSignInClient)
                }
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = if (waiting.value) "Signing in with Google ..." else "Sign in with Google")
        }

        // Display of the error
        Box{
            this@Column.AnimatedVisibility(visible = error.value,
                enter= EnterTransition.None,
                exit = ExitTransition.None) {
                Text(text = errorMessage.value.ifEmpty { "An error occurred while signing in"},
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(280.dp)
                        .align(Alignment.Center).padding(40.dp))
            }
        }
    }

}

/**
 * Listener to catch the exception from the db call
 */
private fun onConfirmListener(context: Context, error: MutableState<Boolean>, errorMessage: MutableState<String>,
                              waiting: MutableState<Boolean>, userNotInDb: MutableState<Boolean>,
                              viewModel: AuthViewModel, auth: FirebaseAuth, navController: NavController){

    // Resets the values used for error handling
    error.value = false
    waiting.value = true
    userNotInDb.value = false

    viewModel.signInUser()
        .whenComplete { result, e ->
            waiting.value = false
            if (e != null){
                // Print the Exception Message on the UI
                error.value = true
                errorMessage.value = e.message?:""
            }else {
                if(result) {
                    updateUI(auth.currentUser, navController, true, context, viewModel)
                } else {
                    userNotInDb.value = true
                }
            }
        }
}

/**
 * Set the client with the default sign in options
 */
fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.resources.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}



/**
 * the Sign In function when the the button is clicked on
 */
fun signIn(activityResultLauncher: ActivityResultLauncher<Intent>,
           googleSignInClient: GoogleSignInClient) {
    val signInIntent = googleSignInClient.signInIntent
    activityResultLauncher.launch(signInIntent)
}



/**
 * Changes the intent depending on the user's credentials
 */
private fun updateUI(user: FirebaseUser?, navController: NavController,
                     isInDatabase: Boolean, context: Context, viewModel: AuthViewModel
) {

    if (user != null && !isInDatabase) {

        Log.d(TAG, "User is not null and is not in database")
        navController.navigate("signup")

    } else if (user != null && isInDatabase) {

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("username",viewModel.getUsername().text)
        context.startActivity(intent)
        Log.d(TAG, "User is not null and is in database")

    }
    else {
        Log.d(TAG, "User is null")
    }

}