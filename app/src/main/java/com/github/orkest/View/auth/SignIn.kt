package com.github.orkest.View.auth

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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.github.orkest.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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
fun SignIn (navController: NavController) {

    val context = LocalContext.current
    val googleSignInClient = remember { getGoogleSignInClient(context) }
    val auth = Firebase.auth

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
                        updateUI(user, navController)
                    } else {
                        updateUI(null, navController)
                    }
                }
            } catch (e: ApiException) {
                updateUI(null, navController)
            }
        }
    }

    /**
     * The two composable objects that are displayed : the logo and the sign in button
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
            Text("Sign in with Google")
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
private fun updateUI(user: FirebaseUser?, navController: NavController) {
    if (user != null) {
        Log.d(TAG, "User is not null")
//        navController.navigate("signup")
    } else {
        Log.d(TAG, "User is null")
    }
}