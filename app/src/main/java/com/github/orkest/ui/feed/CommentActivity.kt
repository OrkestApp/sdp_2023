package com.github.orkest.ui.feed


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.github.orkest.data.Comment
import com.github.orkest.R
import com.github.orkest.ui.theme.OrkestTheme


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
    Column {
        /* The given button allows the user to return to feed */
        ReturnButton(activity = activity)

        Column(modifier = Modifier.fillMaxSize()) {
            // The Modifier.weight(1f, false) is to have a sticky footer (which is the TextField)
            Surface(modifier = Modifier.fillMaxWidth().weight(1f)) {
                // display the post's comments
                Comments(viewModel)
            }
            Divider()

            var comment by rememberSaveable { mutableStateOf("") }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("comment_field"),
                value = comment,
                onValueChange = { comment = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                ),
                leadingIcon = { displayProfilePic() },
                /* publish button */
                trailingIcon = {
                    Icon(Icons.Default.Send,
                        contentDescription = "Send Button",
                        modifier = Modifier
                            .testTag("publish_comment_button")
                            .clickable {
                                // adds the written text in the database
                                viewModel.updateComments(Comment(text = comment))
                                comment = ""
                            })
                },
                placeholder = { Text(text = "Write your thoughts...") }
            )
        }
    }

}


/**
 * Button allowing user to return to feed
 */
@Composable
fun ReturnButton(activity: ComponentActivity) {
    IconButton(
        modifier = Modifier.testTag("return_button"),
        onClick = {activity.finish()}
    ) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = "Return Button"
        )
    }
}



/**
 * Displays the comments under a post
 * @param comments: the list of comments to be displayed
 */
@Composable
fun Comments(viewModel: PostViewModel) {

    /* fetch comments of the given post from database */
    var listComments by remember {
        mutableStateOf( ArrayList<Comment>().toList())
    }
    viewModel.getComments()
        .whenComplete { t, _ ->
            if (t != null) {
                listComments = t
            }
        }

    /* Display the comments in a LazyColumn */
    LazyColumn {
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
                    Text(text = comment.username, fontWeight = FontWeight.Bold, modifier = Modifier.testTag("comment_username"))
                    Text(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .testTag("comment_date"), fontSize = 10.sp, text = comment.date.toString())
                }
                Text(text = comment.text, modifier = Modifier.testTag("comment_text"))
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
            .testTag("display_pic")
    ) {
        val imageUri = rememberSaveable { mutableStateOf("") }
        val painter = rememberImagePainter(
            imageUri.value.ifEmpty { R.drawable.blank_profile_pic }
        )
        Image(painter = painter, contentDescription = "Profile Picture", modifier = Modifier.testTag("display_pic"))
    }
}


/*@Preview(showBackground = true)
@Composable
fun DefaultCommentPreview() {
    CommentSetting {
        CommentScreen(CommentActivity(), PostViewModel())
    }
}*/