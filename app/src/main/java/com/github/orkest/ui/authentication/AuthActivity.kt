package com.github.orkest.ui.authentication

import android.os.Bundle
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
                    val navController = rememberNavController()
                    AuthMain(viewModel, navController)
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

/**
 * Composable for the default preview of the OrkestTheme
 */
/**
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OrkestTheme {
       // AuthMain(AuthViewModel())
    }
}**/