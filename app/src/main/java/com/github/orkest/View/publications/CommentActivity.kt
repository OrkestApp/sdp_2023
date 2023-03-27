package com.github.orkest.View


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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

@Composable
fun CommentScreen(post: ComponentActivity) {
    val comments = (1..30).map { "Item $it" }
    LazyColumn {
        itemsIndexed(comments) { _, comment ->
            Divider()
            CommentBox(
                profilePic ="",
                username = "Roman",
                comment = comment
            )
            Divider()
        }
    }
}

@Composable
fun CommentBox(profilePic: String, username: String, comment: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row() {
            //Icon(painter = profilePic, contentDescription = )
            Column() {
                Text(text = username)
                Text(text = comment)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCommentPreview() {
    CommentSetting {
        CommentScreen(CommentActivity())
    }
}