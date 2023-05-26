package com.github.orkest.ui

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.orkest.ui.profile.EditProfileViewModel
import com.github.orkest.ui.profile.ProfileViewModel
import com.github.orkest.ui.theme.OrkestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


const val PADDING_FROM_SCREEN_BORDER = 10

class EditProfileActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        val viewModel = EditProfileViewModel()
        intent.getStringExtra("bio")?.let { viewModel.setBio(it) }
        intent.getByteArrayExtra("profilePic")?.let { viewModel.setProfilePic(it) }


        super.onCreate(savedInstanceState)
        setContent {
            EditProfileSetting {
                if (viewModel != null) {
                    EditProfileScreen(this, viewModel)
                }
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
fun EditProfileScreen(activity: ComponentActivity, viewModel: EditProfileViewModel) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        // keep track of the state of the scaffold (whether it is opened or closed)
        scaffoldState = scaffoldState,
        topBar = { TopBar(activity, viewModel = viewModel, coroutineScope = coroutineScope, scaffoldState = scaffoldState) },
        // The content displayed inside the drawer when you click on the hamburger menu button
        //drawerContent = { CreateMenuDrawer() },

        content = { padding ->
            Modifier
                .fillMaxHeight()
                .padding(padding)
            Column() {
                // profile pic and edit button
                EditProfileImage(viewModel)
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
        onClick = {
            coroutineScope.launch {
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

/**
 * Top part of the screen, containing:
 * - "cancel" button on the top left to stop editing changes and returning to profile screen
 * - "save" button saving changes to database, updating profile screen and returning to it
 * - current profile picture
 * - "edit picture" button to modify picture
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(activity: ComponentActivity, viewModel: EditProfileViewModel, coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {

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
                    modifier = Modifier.clickable { activity.finish() },
                    fontSize = 20.sp
                )

                // "save" clickable text (button)
                Text(
                    text = "Save",
                    modifier = Modifier.clickable {
                        saveChanges(viewModel)
                        activity.finish()
                    },
                    fontSize = 20.sp
                )
            }
        },
        navigationIcon = {
            NavDrawerButton(coroutineScope, scaffoldState)
        }
    )
}

// TODO update bio in database too
fun saveChanges(viewModel: EditProfileViewModel) {
    viewModel.updateStorage()
}


/**
 * The larger part of the screen containing the fields to modify textual information
 */
@Composable
fun MainBody() {
    Column() {
        EditBio()
    }
}

/**
 * Display current profile picture and an "edit picture" button that is used to choose a new one
 */
@Composable
@OptIn(coil.annotation.ExperimentalCoilApi::class)
fun EditProfileImage(viewModel: EditProfileViewModel) {

    val picData = viewModel.profilePicture.value

    //TODO create a function for this
    //transforms ByteArray to BitMap
    val pic = BitmapFactory.decodeByteArray(picData, 0, picData!!.size).asImageBitmap()

    val bitmap: MutableState<ImageBitmap?> = remember {
        mutableStateOf(pic)
    }

    val context = LocalContext.current

    // will be used to access the images library on your phone and set a new picture
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    )
    {
            uri: Uri? -> uri?.let {
        bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it).asImageBitmap();

        // set the new profile picture (fetched by reading bytes of the URI) in the viewmodel
        context.contentResolver.openInputStream(it)?.readBytes()
            ?.let { it1 -> viewModel.setProfilePic(it1) }
    }
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
            bitmap.value?.let {
                Image(
                    bitmap = bitmap.value!!,
                    contentDescription = "edit_profile_pic",
                    modifier = Modifier.wrapContentSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        // clickable Text offering the possibility to change profile pic
        Text(
            text = "edit picture",
            modifier = Modifier.clickable {
                launcher.launch("image/*")
            }
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


/*@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EditProfileSetting {
        EditProfileScreen(EditProfileActivity(), )
    }
}*/