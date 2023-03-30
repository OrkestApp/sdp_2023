package com.github.orkest.View.search

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.github.orkest.R
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.ViewModel.search.SearchViewModel
import java.util.*

class SearchUserView {
    companion object {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable


                /**
                 * @param viewModel use to communicate with the Backend
                 *
                 *This method display the search bar, and support the drawing of the future users that will
                 * be find in the database
                 *
                 * the viewmodel communicate back to the searchUi view using futures
                 */
        fun SearchUi(viewModel: SearchViewModel) {
            //need to use remember because it triggers an action each time
            var text by remember { mutableStateOf("") }
            var list by remember { mutableStateOf(mutableListOf("")) }

            //Each time the text is updated, this is called
            // Need to use future to wait for the asynchronous fetch on the datxabase
            viewModel.searchUserInDatabase(text).thenAccept {
                list = it.map{x -> x.profile.username} as MutableList<String>
            }

            Column(modifier = Modifier.fillMaxSize())
            {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it


                    }) //This is the search bar

                // This draw the found users each time the list is updated
                LazyColumn {
                    items(list) { userName ->
                        createUser(name = userName)
                    }
                }
            }
        }

        /**
         * This encapsulate the drawing of a single user with name @param user
         */
        @Composable
        private fun createUser(name: String) {
            val context = LocalContext.current
            Row(modifier = Modifier
                .padding(all = 8.dp)
                .clickable { val intent = Intent(context,ProfileActivity::class.java)
                    intent.putExtra("username",name)
                    context.startActivity(intent)
                } //TODO the empty brackets need to be replaced by the composable function or fires an intent to the desired profiles
                .fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.powerrangerblue),
                    contentDescription = "Contact profile picture",
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(40.dp)
                        // Clip image to be shaped as a circle
                        .clip(CircleShape)
                )

                // Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = name)

                }
            }

        }
    }
}