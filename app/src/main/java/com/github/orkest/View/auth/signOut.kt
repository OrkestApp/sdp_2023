/**
 * The Composable object for the sign out button is created
 * with it's respective function that implements the caching of the user
 * the functionality of the button has been tested
 * For the next sprint these two elements need to be implemented
 * in the different actions that the user will have for example,
 * that is why the code is commented
 */

/*package com.github.orkest.View.auth

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
*/

/**
    * The function that implements the sign out button
    * @param auth the firebase authentication object
    * @param context the context of the application
    * @param intent the intent that will be used to start the sign in activity
    */

/*
@Composable
private fun signOut() {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val intent = Intent(context, SignIn::class.java)
    auth.signOut()
    //uncomment if un-caching is needed
    //GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
    context.startActivity(intent)

}*/

/**
    * The Composable object for the sign out button
    */
/*
@Composable
fun SignOutButton() {
    Button(
        onClick = { signOut() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow))
    {
        Text("Sign Out")
    }
}*/