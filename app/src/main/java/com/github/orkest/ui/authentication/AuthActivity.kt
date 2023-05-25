package com.github.orkest.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.orkest.ui.theme.OrkestTheme
import androidx.navigation.compose.composable
import com.github.orkest.data.Constants
import com.github.orkest.ui.MainActivity


/**
 * Activity for the authentication screen
 * Necessary to implement the MVVM pattern
 */

class AuthActivity : ComponentActivity() {

    /**
     * set the content of the OrkestTheme
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = AuthViewModel()
        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // Check if user is already cached in
                    if (isSignedInOffline(this)) {
                        val (username, _) = loadUserCredentials(this)
                        val intent = Intent(this, MainActivity::class.java)
                        Constants.CURRENT_LOGGED_USER = username.toString()
                        this.startActivity(intent)
                    }else{
                        val navController = rememberNavController()
                        AuthMain(viewModel, navController)
                    }
                }
            }
        }
    }
}

/**
 * Launch of the AuthMain composable
 */
@Composable
fun AuthMain(viewModel: AuthViewModel, navController: NavHostController) {

    NavHost(navController = navController, startDestination = "signIn") {
        composable("signup") { SignUpForm(viewModel) }
        composable("signIn") { SignIn(navController,viewModel) }
    }
}
