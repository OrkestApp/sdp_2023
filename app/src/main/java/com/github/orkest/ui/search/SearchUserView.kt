package com.github.orkest.ui.search

import android.content.Intent
import androidx.camera.core.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.orkest.R
import com.github.orkest.bluetooth.ui.BluetoothActivity
import com.github.orkest.data.User

import com.github.orkest.domain.DeezerApiIntegration
import com.github.orkest.ui.CreateProfilePreview
import com.github.orkest.ui.profile.ProfileActivity
import java.util.*

class SearchUserView {

    companion object {

        /**
         * @param viewModel use to communicate with the Backend
         *
         *This method display the search bar, and support the drawing of the future users that will
         * be find in the database
         *
         * the viewmodel communicate back to the searchUi view using futures
         */
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun SearchUi(viewModel: SearchViewModel) {
            //need to use remember because it triggers an action each time
            var text by remember { mutableStateOf("Search a user here") }
            var list by remember { mutableStateOf(mutableListOf("")) }
            val context = LocalContext.current

            //Each time the text is updated, this is called
            // Need to use future to wait for the asynchronous fetch on the database
            viewModel.searchUserInDatabase(text).thenAccept {
                list = it.map{x -> x.profile.username} as MutableList<String>
            }

            Column(modifier = Modifier.padding(20.dp))
            {

                //This is the search bar
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        Modifier
                            .testTag("SearchBar")
                            .fillMaxWidth()
                            .align(Alignment.Center))

                    Image(
                        painter = painterResource(R.drawable.lens_image), // Replace with your lens image resource
                        contentDescription = "Lens",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                            .testTag("search_logo")// Adjust the size as needed
                    )
                }
                // This draw the found users each time the list is updated
                LazyColumn {
                    items(list) { username ->
                        CreateProfilePreview(username)
                    }
                }

                //This is the button to search users via bluetooth
                Button(
                    onClick = {
                        val intent = Intent(context,BluetoothActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .testTag("bluetoothButton"))

                {
                    Text("Search nearby users")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.png_transparent_bluetooth_low_energy_wireless_speaker_mobile_phones_bluetooth_trademark_computer_logo), // Replace with your image resource
                            contentDescription = "Button Icon",
                            modifier = Modifier.size(24.dp) // Adjust the size as needed
                        )
                        Spacer(modifier = Modifier.width(12.dp)) // Add spacing between image and text

                    }
                }

                //DISPLAY ORKEST
                StyledOrkest()
                //DISPLAY ORKEST LOGO
                Image(
                    painter = painterResource(R.drawable.logo), // Replace with your image resource
                    contentDescription = "Orkest Logo",
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.CenterHorizontally)
                        .testTag("logoOrkest") // Adjust the size as needed
                )

                //Display copyright
                Image(painter = painterResource(R.drawable.copyright),
                    contentDescription = "Copyright",
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.End)
                        .testTag("copyright")
                )// Adjust the size as needed
            }
        }

        @Composable
        fun StyledOrkest() {
            Row {
                Text(text = "O",
                    Modifier
                        .testTag("O")
                        .padding(25.dp))
                Text(text = "R",
                    Modifier
                        .testTag("R")
                        .padding(25.dp))
                Text(text = "K",
                    Modifier
                        .testTag("K")
                        .padding(25.dp))
                Text(text = "E",
                    Modifier
                        .testTag("E")
                        .padding(25.dp))
                Text(text = "S",
                    Modifier
                        .testTag("S")
                        .padding(25.dp))
                Text(text = "T",
                    Modifier
                        .testTag("T")
                        .padding(25.dp))
            }
        }
        /**
         * This encapsulate the drawing of a single user with name @param user
         */
//        @Composable
//        fun CreateUser(name: String, intent: Intent) {
//            val context = LocalContext.current
//            Row(modifier = Modifier
//                .padding(all = 8.dp)
//                .clickable {
//                    context.startActivity(intent)
//                } //TODO the empty brackets need to be replaced by the composable function or fires an intent to the desired profiles
//                .fillMaxSize()) {
//                Image(
//                    painter = painterResource(R.drawable.powerrangerblue),
//                    contentDescription = "Contact profile picture",
//                    modifier = Modifier
//                        // Set image size to 40 dp
//                        .size(40.dp)
//                        // Clip image to be shaped as a circle
//                        .clip(CircleShape)
//                )
//                // Add a horizontal space between the image and the column
//                Spacer(modifier = Modifier.width(8.dp))
//
//                Column {
//                    Text(text = name)
//                }
//            }
//        }
    }
}