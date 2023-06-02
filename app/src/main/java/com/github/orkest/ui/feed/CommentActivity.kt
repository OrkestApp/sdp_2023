package com.github.orkest.ui.feed


import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberImagePainter
import com.github.orkest.data.Comment
import com.github.orkest.R
import com.github.orkest.data.Constants
import com.github.orkest.domain.FirebaseStorageAPI
import com.github.orkest.ui.theme.OrkestTheme
import okio.utf8Size
import java.io.ByteArrayOutputStream


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
    companion object{
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
                displayProfilePic(comment.username)
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
    }}
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
                leadingIcon = { displayProfilePic(Constants.CURRENT_LOGGED_USER) },
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
            CommentActivity.CommentBox(comment = comment)
        }
    }
}



/**
 * standardized 'displayer' of profile pics on comments
 * TODO: need to decide on how to reference people's profile pictures
 */
@Composable
fun displayProfilePic(username: String) {
    val context = LocalContext.current
    val d = context.getDrawable(R.drawable.blank_profile_pic)
    val stream = ByteArrayOutputStream()
    d?.toBitmap()?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val bytearray = stream.toByteArray()
    val bitmapdata = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.size);

    val bitmap: MutableState<ImageBitmap?> = remember {
        mutableStateOf(bitmapdata.asImageBitmap())
    }

    /*viewModel.fetchProfilePic().addOnSuccessListener {
        bitmap.value = BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
    }*/
    FirebaseStorageAPI.fetchProfilePic(username).whenComplete { bytes, _ ->
        bitmap.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
    }

    bitmap.value?.let {
        Image(
            bitmap = bitmap.value!!,
            contentDescription = "Profile Picture of the user",
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
                .height(40.dp)
                .width(40.dp)
                .clip(shape = CircleShape)
                .clickable { }
        )
    }
    /*Card(
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(40.dp)
            .testTag("display_pic")
    ) {
        val context = LocalContext.current
        val d = context.getDrawable(R.drawable.blank_profile_pic)
        val stream = ByteArrayOutputStream()
        d?.toBitmap()?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytearray = stream.toByteArray()
        val bitmapdata = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.size);

        val bitmap: MutableState<ImageBitmap?> = remember {
            mutableStateOf(bitmapdata.asImageBitmap())
        }

        /*viewModel.fetchProfilePic().addOnSuccessListener {
            bitmap.value = BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
        }*/
        FirebaseStorageAPI.fetchProfilePic(username).whenComplete { bytes, _ ->
            bitmap.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
        }
        /*{ bytes, _ ->
            bitmap.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
        }*/
        //-------------------------------------------

        //Add the user's profile pic
        bitmap.value?.let {
            Image(
                bitmap = bitmap.value!!,
                contentDescription = "Profile Picture of the user"
            )
        }
        //Image(painter = painter, contentDescription = "Profile Picture", modifier = Modifier.testTag("display_pic"))
    }*/
}
