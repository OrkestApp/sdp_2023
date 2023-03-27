package com.github.orkest.View


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.github.orkest.R
import com.github.orkest.View.theme.OrkestTheme


class CommentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CommentSetting {
                CommentScreen(this)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(post: ComponentActivity) {
    Column() {
        Surface(modifier = Modifier.height(600.dp)) {
            Comments()
        }
        var modifyName by rememberSaveable { mutableStateOf("tqt") }
        TextField(
            value = modifyName,
            onValueChange = { modifyName = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )
    }

}

@Composable
fun Comments() {
    val comments = (1..30).map { "Item $it" }
    LazyColumn {
        itemsIndexed(comments) { index, comment ->
            Divider()
            CommentBox(
                profilePic = "",
                username = "Roman ${index}",
                comment = comment
            )
        }
    }
}

@Composable
fun CommentBox(profilePic: String, username: String, comment: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            displayProfilePic()
            Column() {
                Text(text = username, fontWeight = FontWeight.Bold)
                Text(text = comment)
            }
        }
    }
}

@Composable
fun displayProfilePic() {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(50.dp)
    ) {
        val imageUri = rememberSaveable { mutableStateOf("") }
        val painter = rememberImagePainter(
            imageUri.value.ifEmpty { R.drawable.blank_profile_pic }
        )
        Image(painter = painter, contentDescription = "tqt")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCommentPreview() {
    CommentSetting {
        CommentScreen(CommentActivity())
    }
}