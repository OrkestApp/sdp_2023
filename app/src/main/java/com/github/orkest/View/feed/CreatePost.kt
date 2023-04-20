package com.github.orkest.View.feed

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.Constants
import com.github.orkest.View.theme.OrkestTheme
import com.github.orkest.ViewModel.post.PostViewModel


//TODO: Implement correctly, this is just a simple version for testing and demo purposes
class CreatePost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel = PostViewModel()
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
                onClick = { viewModel.addPost().whenComplete{
                            bool,_ ->
                                if(bool) activity.finish()
                        }
                      },
                modifier = Modifier
                    .height(50.dp)
            ) {
                Text(text = "Publish")
            }
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
