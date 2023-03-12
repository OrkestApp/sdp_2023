package com.github.orkest.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.orkest.View.ui.theme.OrkestTheme
import com.github.orkest.R

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            editProfileSetting {
                EditProfileScreen(listOf("Name", "Username", "Bio"))
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
fun EditProfileScreen(listSections: List<String>) {
    Column(modifier = Modifier.fillMaxHeight()){
        editProfilePic()
        Divider()
        for(section in listSections) {
            Section(name = section)
        }
    }
}

@Composable
fun topBar() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // button to cancel editing and return to profile screen
            Text(
                text = "Cancel",
                modifier = Modifier.clickable { /* TODO */ }
            )

            // button to update profile
            Text(
                text = "Save",
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
}

@Composable
fun editProfilePic() {
    Column(modifier = Modifier,
        verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = R.drawable.blank_profile_pic),
            contentDescription = stringResource(id = R.string.HomePage)
        )
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
        EditProfileScreen(listOf("Name", "Username", "Bio"))
    }
}