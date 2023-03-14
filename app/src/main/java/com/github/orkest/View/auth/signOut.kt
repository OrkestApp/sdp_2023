//package com.github.orkest.View.auth
//
//import android.content.Intent
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import com.google.firebase.auth.FirebaseAuth
//
//@Composable
//private fun signOut() {
//    val auth = FirebaseAuth.getInstance()
//    val context = LocalContext.current
//    val intent = Intent(context, SignIn::class.java)
//    auth.signOut()
//    //uncomment if un-caching is needed
//    //GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
//    context.startActivity(intent)
//
//}
//
///* for later use
//@Composable
//fun SignOutButton() {
//    Button(
//        onClick = { signOut() },
//        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow))
//    {
//        Text("Sign Out")
//    }
//}*/