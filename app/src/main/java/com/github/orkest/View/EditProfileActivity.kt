package com.github.orkest.View

import android.app.LocalActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.github.orkest.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.DrawerValue
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.View.theme.OrkestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.github.orkest.ViewModel.auth.AuthViewModel

const val PADDING_FROM_SCREEN_BORDER = 10

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditProfileSetting {
                EditProfileScreen(this)
            }
        }
    }
}

/**
 * Function generating the screen, takes as function the Composable modeling the screen
 */
@Composable
fun EditProfileSetting(content: @Composable () -> Unit) {
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
 * Principal function in which we build the general structure of the activity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(activity: ComponentActivity) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        // keep track of the state of the scaffold (whether it is opened or closed)
        scaffoldState = scaffoldState,
        topBar = { TopBar(activity, coroutineScope = coroutineScope, scaffoldState = scaffoldState) },
        // The content displayed inside the drawer when you click on the hamburger menu button
        drawerContent = { CreateMenuDrawer() },

        content = { padding ->
            Modifier
                .fillMaxHeight()
                .padding(padding)
            Column() {
                // profile pic and edit button
                EditProfileImage()
                Divider()
                MainBody()
            }
        },
        drawerGesturesEnabled = true
    )


}

@Composable
fun NavDrawerButton(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {
    IconButton(
        onClick = { coroutineScope.launch {
            if (scaffoldState.drawerState.currentValue == DrawerValue.Closed)
                scaffoldState.drawerState.open()
            else
                scaffoldState.drawerState.close()
        }
        }
    ) {
        Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Drawer Icon")
    }
}

@Composable
fun CreateMenuDrawer() {
    val notifSettingsItem = MenuItem(id = "notificationSettings", title = "Notifications", icon = Icons.Default.Notifications)
    val privacyItem = MenuItem(id = "privacySettings", title = "Privacy", icon = Icons.Default.Phone)
    val helpItem = MenuItem(id = "help", title = "Help", icon = Icons.Default.Info)

    val items = listOf(notifSettingsItem, privacyItem, helpItem)

    MenuDrawer(
        items = items,
        onItemClick = {
            when(it.id) {
                "notificationSettings" -> { /* TODO */ }
                "privacySettings" -> { /* TODO */ }
                "help" -> { /* TODO */ }
            }
        }
    )
}

/**
 * Top part of the screen, containing:
 * - "cancel" button on the top left to stop editing changes and returning to profile screen
 * - "save" button saving changes to database, updating profile screen and returning to it
 * - current profile picture
 * - "edit picture" button to modify picture
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(activity: ComponentActivity, coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {

    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                // this spaces out the "cancel" and "save" buttons
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // "cancel" clickable text (button)
                Text(
                    text = "Cancel",
                    modifier = Modifier.clickable {activity.finish() },
                    fontSize = 20.sp
                )

                // "save" clickable text (button)
                Text(
                    text = "Save",
                    modifier = Modifier.clickable { /* TODO */ },
                    fontSize = 20.sp
                )
            }
        },
        navigationIcon = {
            NavDrawerButton(coroutineScope, scaffoldState)
        }
    )
}



/**
 * The larger part of the screen containing the fields to modify textual information
 */
@Composable
fun MainBody() {
    Column() {
        EditNameSection(name = "Username", default = "default username")
        EditBio()
    }
}

/**
 * Display current profile picture and an "edit picture" button that is used to choose a new one
 */
@Composable
@OptIn(coil.annotation.ExperimentalCoilApi::class)
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
        // clickable Text offering the possibility to change profile pic
        Text(
            text = "edit picture",
            modifier = Modifier.clickable { launcher.launch("image/*") })
    }
}


/**
 * fields to modify small text data such as name and username
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNameSection(name: String, default: String) {
    var modifyName by rememberSaveable { mutableStateOf(default) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = PADDING_FROM_SCREEN_BORDER.dp, end = PADDING_FROM_SCREEN_BORDER.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$name:", modifier = Modifier.width(100.dp))
        TextField(
            value = modifyName,
            onValueChange = { modifyName = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                //textColor = Color.Gray
            )
        )
    }
}


/**
 * function creating the field to modify the bio of the user
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBio() {
    var bio by rememberSaveable { mutableStateOf("Description") }
    Row(
        modifier = Modifier
            .padding(PADDING_FROM_SCREEN_BORDER.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "Bio:", modifier = Modifier
            .width(100.dp)
            .padding(top = 8.dp)
        )
        TextField(
            value = bio,
            onValueChange = { bio = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                //textColor = Color.Gray
            ),
            singleLine = false,
            modifier = Modifier.height(150.dp)
        )
    }
}

// TODO: transform this into a class since it is something that could be reusable?
@Composable
fun MenuDrawer(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    Column(modifier) {
        for(item in items) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EditProfileSetting {
        EditProfileScreen(EditProfileActivity())
    }
}