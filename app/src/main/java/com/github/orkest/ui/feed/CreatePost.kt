package com.github.orkest.ui.feed

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.github.orkest.data.Constants
import com.github.orkest.View.feed.SongCard
import com.github.orkest.data.Song
import com.github.orkest.domain.FirebaseStorageAPI
import com.github.orkest.ui.MainActivity
import com.github.orkest.ui.notification.Notification
import com.github.orkest.ui.theme.OrkestTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView


//TODO: Implement correctly, this is just a simple version for testing and demo purposes

var mediaURI: Uri = Uri.EMPTY
var isVideo : Boolean = false
class CreatePost : ComponentActivity() {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel = PostViewModel()

                    isVideo = intent.getBooleanExtra("isVideo", false)
                    val URIstring = intent.getStringExtra("URI") ?: "Unknown"
                    mediaURI = Uri.parse(URIstring)

                    if(isVideo) {
                        FirebaseStorageAPI.uploadVideo(mediaURI, viewModel.getPost())
                    } else {
                        FirebaseStorageAPI.uploadPostPic(mediaURI, viewModel.getPost())
                    }

                    viewModel.setPostMedia(URIstring, isVideo)

                    val song = Song()
                    //Get the song name, artist, and album from the intent
                    //--------------Intent handling------------------
                    if (intent.hasExtra(Constants.SONG_NAME))
                        song.Title = intent.getStringExtra(Constants.SONG_NAME) ?: "Unknown"

                    if (intent.hasExtra(Constants.SONG_ARTIST))
                        song.Artist = intent.getStringExtra(Constants.SONG_ARTIST) ?: "Unknown"

                    if (intent.hasExtra(Constants.SONG_ALBUM))
                        song.Album = intent.getStringExtra(Constants.SONG_ALBUM) ?: "Unknown"

                    viewModel.updateSong(song)

                    EditPostScreen(viewModel = viewModel, activity = this)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditPostScreen(viewModel: PostViewModel, activity: ComponentActivity) {

    //Create editable fields for the post
    // Add a button to list songs to choose from

    val context = LocalContext.current

    Scaffold(
        topBar = {
            Text(text = "Publish Post")
        }) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Title of the page
            Text(
                text = "Create Your Post",
                style = TextStyle(fontSize = 25.sp, fontFamily = Constants.FONT_MARKER),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(15.dp))

            // Field to choose post description
            TextField(
                label = { Text(text = "Post Description") },
                value = viewModel.getPostDescription(),
                onValueChange = { viewModel.updatePostDescription(it) }
            )

            Spacer(modifier = Modifier.height(15.dp))

            //TODO: Add a button to list songs to choose from
            //Field to choose the song to display [dummy, to be correctly implemented later with retrieval from database ..]
            viewModel.updateSong(Constants.DUMMY_RUDE_BOY_SONG)
            SongCard(song = viewModel.getSong())


            Spacer(modifier = Modifier.height(15.dp))

            //TODO: Add error handling and correctly implemented behavior
            //Confirm button to publish the post
            Button(
                onClick = {
                    viewModel.addPost().whenComplete{
                            bool,_ -> if(bool) activity.finish() }

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                    Notification(context,null).sendNotification("New Post", "A new Post was added ;)", "New Post", "New Post", 2)
                },
                modifier = Modifier
                    .height(50.dp)
            ) {
                Text(text = "Publish")
            }

            Spacer(modifier = Modifier.height(15.dp))

            //Display the captured media
            CapturedMedia(capturedUri = mediaURI, isVideo = isVideo)


        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CapturedMedia(
    capturedUri: Uri?,
    isVideo: Boolean
) {
    val context = LocalContext.current
    lateinit var exoPlayer: ExoPlayer

    Box(modifier = Modifier.fillMaxSize()) {
        if (isVideo){
            //Displays the taken video
            exoPlayer = remember(context) {
                ExoPlayer.Builder(context).build().apply {
                    capturedUri?.let { MediaItem.fromUri(it) }?.let { setMediaItem(it) }
                    prepare()
                }
            }
            AndroidView(
                factory = { context ->
                    StyledPlayerView(context).apply {
                        player = exoPlayer
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("Captured Video")
            )

        } else {
            //Displays the captured Image
            Image(
                painter = rememberImagePainter(data = capturedUri),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun EditPost() {
//    OrkestTheme {
//        EditPostScreen(PostViewModel(), CreatePost())
//    }
//}
