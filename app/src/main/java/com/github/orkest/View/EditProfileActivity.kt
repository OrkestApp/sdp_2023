package com.github.orkest.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.orkest.View.ui.theme.OrkestTheme

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            editProfileSetting {
                mainContent(listOf("Name", "Username", "Bio"))
            }
        }
    }
}

@Composable
fun editProfileSetting(content: @Composable () -> Unit) {
    OrkestTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            //modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Composable
fun mainContent(listSections: List<String>) {
    Column(modifier = Modifier.fillMaxHeight()){
        editProfilePic()
        Divider()
        for(section in listSections) {
            Section(name = section)
            Divider()
        }
    }
}

@Composable
fun editProfilePic() {
    Surface() {
        Button(onClick = { }) {
            /*TODO*/
            Text(text = "edit profile")
        }
    }
}

@Composable
fun Section(name: String) {
    Text(
        text = "$name:",
        modifier = Modifier.padding(5.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    editProfileSetting {
        mainContent(listOf("Name", "Username", "Bio"))
    }
}