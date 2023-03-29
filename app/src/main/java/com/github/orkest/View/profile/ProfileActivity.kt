package com.github.orkest.View.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.orkest.View.theme.OrkestTheme
import com.github.orkest.ViewModel.profile.ProfileViewModel

class ProfileActivity() : ComponentActivity() {

    //TODO create the currentUser's username when signing up
    private lateinit var  currentUser : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = intent.getStringExtra("username").toString()
        val viewModel = ProfileViewModel(currentUser)
        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    topProfile(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun topProfile(viewModel: ProfileViewModel) {
    ProfileTopInterface(viewModel = viewModel)
}

@Preview(showBackground = true)
@Composable
fun DefaultProfilePreview() {
    OrkestTheme {
        topProfile(viewModel = ProfileViewModel("JohnDoe"))
    }
}