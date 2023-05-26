package com.github.orkest.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.github.orkest.data.Constants
import com.github.orkest.data.Song
import com.github.orkest.R
import com.github.orkest.View.*
import com.github.orkest.domain.persistence.AppDatabase
import com.github.orkest.ui.theme.OrkestTheme
import kotlinx.coroutines.CoroutineScope

class ProfileActivity(private val appDatabase: AppDatabase) : ComponentActivity() {

    //TODO create the currentUser's username when signing up
    private lateinit var  currentUser : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = intent.getStringExtra("username").toString()
        val viewModel = ProfileViewModel(appDatabase ,currentUser)
        setContent {
            ProfileActivitySetting {
                ProfileActivityScreen(appDatabase, this, viewModel = viewModel)
            }
        }
    }
}

/**
 * Function generating the screen, takes as function the Composable modeling the screen
 */
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
fun ProfileActivityScreen(appDatabase: AppDatabase, activity: ComponentActivity, viewModel: ProfileViewModel) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    androidx.compose.material.Scaffold(
        // keep track of the state of the scaffold (whether it is opened or closed)
        scaffoldState = scaffoldState,
        // main screen content
        content = { padding ->
            Modifier
                .fillMaxHeight()
                .padding(padding)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TopProfile(appDatabase, viewModel = viewModel, scaffoldState, coroutineScope)
                }
                MainBody(viewModel)
            }
        },
        drawerGesturesEnabled = true
    )

}

@Composable
fun TopProfile(appDatabase: AppDatabase, viewModel: ProfileViewModel, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
    ProfileTopInterface(appDatabase, viewModel, scaffoldState, coroutineScope)
}

@Composable
fun MainBody(viewModel: ProfileViewModel) {
    Divider(modifier = Modifier.padding(vertical = 10.dp))
    favoriteSongs(viewModel)
    Divider(modifier = Modifier.padding(vertical = 10.dp))
    favoriteArtists(viewModel)
}

// creates the row displaying the user's favorite songs
@Composable
fun favoriteSongs(viewModel: ProfileViewModel) {
    placeholders("Favorite Songs", items = ArrayList<Song>(), viewModel = viewModel)
}

// creates the row displaying the user's favorite artists
@Composable
fun favoriteArtists(viewModel: ProfileViewModel) {
    placeholders(title = "Favorite Artists", items = ArrayList<String>(), viewModel = viewModel)
}

@Composable
fun <T> placeholders (title: String, items: List<T>, select: () -> Unit = { }, viewModel: ProfileViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        if(viewModel.user == Constants.CURRENT_LOGGED_USER) {
            IconButton(
                onClick = { /* TODO ADD ABILITY TO ADD SONGS + ARTISTS */ }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add Button")
            }
        }
    }
    // replace with your items...
    val items = (1..10).map { "Item $it" }
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        imageUri.value.ifEmpty { R.drawable.blank_profile_pic }
    )
    // This constitutes the scrollable row with the elements to display
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        // LazyRow to display your items horizontally
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = rememberLazyListState()
        ) {
            itemsIndexed(items) { _, item ->
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .width(maxWidth) // allows for the horizontal scroll
                        .padding(10.dp)
                        .background(Color.Gray)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.wrapContentSize(),
                        contentScale = ContentScale.Crop) // card's content
                }
            }
        }
    }
}
