package com.github.orkest.View.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.View.*
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
                ProfileActivityScreen(this, viewModel = viewModel)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActivityScreen(activity: ComponentActivity, viewModel: ProfileViewModel) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    androidx.compose.material.Scaffold(
        // keep track of the state of the scaffold (whether it is opened or closed)
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    NavDrawerButton(coroutineScope, scaffoldState)
                }
            )
        },
        // The content displayed inside the drawer when you click on the hamburger menu button
        drawerContent = { CreateMenuDrawer() },

        content = { padding ->
            Modifier
                .fillMaxHeight()
                .padding(padding)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
            ) {
                topProfile(viewModel = viewModel)
                Divider()
                favoriteSongs()
                Divider()
                favoriteArtists()

            }
        },
        drawerGesturesEnabled = true
    )

}

@Composable
fun topProfile(viewModel: ProfileViewModel) {
    ProfileTopInterface(viewModel = viewModel)
}

@Composable
fun favoriteSongs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Favorite Songs")
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add Button")
        }
    }
}

@Composable
fun favoriteArtists() {
    
}



@Preview(showBackground = true)
@Composable
fun DefaultProfilePreview() {
    ProfileActivitySetting {
        ProfileActivityScreen(ProfileActivity(), viewModel = ProfileViewModel("JohnDoe"))
    }
}