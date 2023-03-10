package com.github.orkest.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.orkest.ui.theme.OrkestTheme
import androidx.navigation.compose.composable

class AuthActivity : ComponentActivity() {
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
                    authMain(viewModel)
                }
            }
        }
    }
}

@Composable
fun authMain(viewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") { SignUpForm(navController = navController,viewModel)}
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OrkestTheme {
        authMain(AuthViewModel())
    }
}