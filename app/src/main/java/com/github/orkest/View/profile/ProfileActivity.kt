package com.github.orkest.View.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.orkest.ViewModel.profile.ProfileViewModel
import com.github.orkest.ui.theme.OrkestTheme

class ProfileActivity() : ComponentActivity() {

    //TODO create the currentUser's username when signing up
    private lateinit var  currentUser : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = intent.getStringExtra("username").toString()
        val viewModel = ProfileViewModel(currentUser)
        setContent {
            ProfileActivitySetting {
                ProfileActivityScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ProfileActivitySetting(content: @Composable () -> Unit) {
    OrkestTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }

    }
}

@Composable
fun ProfileActivityScreen(viewModel: ProfileViewModel) {
    Column() {
        topProfile(viewModel = viewModel)
        Divider()
        favoriteSongs()
        Text(text="AHHHHHHHHHHHH")
        Divider()
        favoriteArtists()
    }
}

@Composable
fun topProfile(viewModel: ProfileViewModel) {
    ProfileTopInterface(viewModel = viewModel)
}

@Composable
fun favoriteSongs() {
    
}

@Composable
fun favoriteArtists() {
    
}



@Preview(showBackground = true)
@Composable
fun DefaultProfilePreview() {
    ProfileActivitySetting {
        ProfileActivityScreen(viewModel = ProfileViewModel("JohnDoe"))
    }
}