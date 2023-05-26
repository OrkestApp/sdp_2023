package com.github.orkest.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HelpActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HelpActivityScreen(this)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpActivityScreen(activity: HelpActivity) {
    Column {
        TopAppBar(
            title = { Text("INFO", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().height(50.dp).testTag("info_title")) },
            navigationIcon = {
                Button(onClick = { activity.finish()}, modifier = Modifier.testTag("back_arrow")) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("help_top_bar")
        )

        Info()
        Spacer(modifier = Modifier.width(10.dp))
        Tutorial()
    }
}

@Composable
fun Info() {
    Column(modifier = Modifier.padding(10.dp)) {
    Text(text = "ABOUT", modifier = Modifier.testTag("about_title"), fontSize = 20.sp)
    Text(
        text = "Orkest is an application that allows you to share music between platforms." +
                "Say you are using Spotify and want to share a song with your friend, but they use " +
                "Deezer. You'd send them a link that they wouldn't be able to exploit since they " +
                "don't use Spotify. With Orkest you do not have to worry about this because we do " +
                "all the work of sending the Deezer version of the Spotify song you want to send. " +
                "All the songs you send are located in your direct messages with your friends.",
        modifier = Modifier.testTag("about_text"),
    )
    }
}

@Composable
fun Tutorial() {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = "TUTORIAL", modifier = Modifier.testTag("tutorial_title"), fontSize = 20.sp)
        Text(
            text = "Your profile is the place to express your creativity and share your music " +
                    "personality with the world!" +
                    "\nBegin by creating a bio, adding a picture, favorite artists and albums!",
            modifier = Modifier.testTag("tutorial_text"),
        )
    }
}