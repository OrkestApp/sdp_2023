package com.github.orkest.View

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.github.orkest.View.ui.theme.OrkestTheme
import com.github.orkest.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            editProfileSetting {
                EditProfileScreen()
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
fun EditProfileScreen() {
    Column(modifier = Modifier.fillMaxHeight()){
        topBar()
        Divider()
        mainBody()
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
        EditProfileImage()
    }
}

@Composable
fun mainBody() {

    val nameAndDefault: HashMap<String, String> =
        hashMapOf("Name" to "default name", "Username" to "default username")
    for((n, def) in nameAndDefault) {
        NameSection(name = n, default = def)
    }
    Bio()
}

/**
 * Display current profile picture and an "edit picture" button that is used to choose a new one
 */
@Composable
fun EditProfileImage() {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        imageUri.value.ifEmpty { R.drawable.blank_profile_pic }
    )

    // will be used to access the images library on your phone and set a new picture
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent())
    {
        uri: Uri? -> uri?.let { imageUri.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // displays your profile picture in a circle
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp))
        {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.wrapContentSize(),
                contentScale = ContentScale.Crop)
        }
        Text(
            text = "edit picture",
            modifier = Modifier.clickable { launcher.launch("image/*") })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameSection(name: String, default: String) {
    var modifyName by rememberSaveable { mutableStateOf(default) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$name:", modifier = Modifier.width(100.dp))
        TextField(
            value = modifyName,
            onValueChange = { modifyName = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.Gray
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bio() {
    var bio by rememberSaveable { mutableStateOf("Description") }
    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "Bio:", modifier = Modifier.width(100.dp).padding(top = 8.dp))
        TextField(
            value = bio,
            onValueChange = { bio = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.Gray
            ),
            singleLine = false,
            modifier = Modifier.height(150.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    editProfileSetting {
        EditProfileScreen()
    }
}