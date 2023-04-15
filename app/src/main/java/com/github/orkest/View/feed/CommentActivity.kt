package com.github.orkest.View.feed


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.github.orkest.Constants
import com.github.orkest.Model.Comment
import com.github.orkest.Model.Post
import com.github.orkest.R
import com.github.orkest.View.theme.OrkestTheme
import com.github.orkest.View.feed.FeedActivity
import com.github.orkest.ViewModel.post.PostViewModel


class CommentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = PostViewModel()
        intent.getStringExtra("post_username")?.let { viewModel.setPostUsername(it) }
        intent.getStringExtra("post_date")?.let { viewModel.setPostDate(it) }

        setContent {
            CommentSetting {
                CommentScreen(this, viewModel)
            }
        }
    }
}

@Composable
fun CommentSetting(content: @Composable () -> Unit) {
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

/**
 * Main screen on which this the Comment activity is built
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(activity: ComponentActivity, viewModel: PostViewModel) {
    //val comments = (1..15).map { Comment(username = "roman n° ${it}", text = "oh nah")}
    Column() {
        // Button to return to feed
        IconButton(
            onClick = {activity.finish()}
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Return Button"
            )
        }

        // The Modifier.weight(1f, false) is to have a sticky footer (which is the TextField)
        Surface(modifier = Modifier.weight(1f, false)) {
            Comments(viewModel)
        }
        Divider()

        /* text field where the user can type their comment */
        var comment by rememberSaveable { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment,
            onValueChange = { comment = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
            ),
            leadingIcon = { displayProfilePic() },//Icon(imageVector = Icons.Default.Android, contentDescription = "User Profile Pic") },
            trailingIcon = { PublishButton(viewModel, comment) },
            //label = { Text(text = "Username") },
            placeholder = { Text(text = "Write your thoughts...") }
        )
    }

}

/**
 * Displays the comments under a post
 * @param comments: the list of comments to be displayed
 */
@Composable
fun Comments(viewModel: PostViewModel) {

    var listComments by remember {
        mutableStateOf( ArrayList<Comment>().toList())
    }
    viewModel.getComments()
        .whenComplete { t, _ ->
            if (t != null) {
                listComments = t
            }
        }

    LazyColumn {
        //val list = (1..15).map {Comment(username = "$it")}
        itemsIndexed(listComments) { _, comment ->
            Divider()
            CommentBox(comment = comment)
        }
    }
}

/**
 * Comment design
 * @param comment: the comment to display
 */
@Composable
fun CommentBox(comment: Comment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            displayProfilePic()
            Column {
                Row(horizontalArrangement = Arrangement.SpaceEvenly){
                    Text(text = comment.username, fontWeight = FontWeight.Bold)
                    Text(modifier = Modifier.padding(horizontal = 8.dp), fontSize = 10.sp, text = comment.date.toString())
                }
                Text(text = comment.text)
            }
        }
    }
}

/**
 * standardized 'displayer' of profile pics on comments
 * TODO: need to decide on how to reference people's profile pictures
 */
@Composable
fun displayProfilePic(/* TODO */) {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(40.dp)
    ) {
        val imageUri = rememberSaveable { mutableStateOf("") }
        val painter = rememberImagePainter(
            imageUri.value.ifEmpty { R.drawable.blank_profile_pic }
        )
        Image(painter = painter, contentDescription = "Profile Picture")
    }
}


/**
 * "Send" button that when pressed will publish the typed comment
 * TODO: add the onClick functionality
 */
@Composable
fun PublishButton(viewModel: PostViewModel, text: String) {
    IconButton(
        onClick = {
            val comment = Comment(text = text)
            viewModel.updateComments(comment)
        }
    ) {
        Icon(
            Icons.Default.Send,
            contentDescription = "Send Button"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCommentPreview() {
    CommentSetting {
        CommentScreen(CommentActivity(), PostViewModel())
    }
}