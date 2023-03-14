package com.github.orkest.View.search

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

import com.github.orkest.R
import com.github.orkest.ViewModel.search.SearchViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SearchUserView {
    companion object{

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable

    fun SearchUi(viewModel : SearchViewModel) {
        var text by remember { mutableStateOf("") }
        var list by remember { mutableStateOf(mutableListOf("")) }

        fun callBack(users:MutableList<String>) {
            list = users

        }
        viewModel.searchUserInDatabase(text,  ::callBack)



        Column(modifier = Modifier.fillMaxSize())
        {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it


                })


            LazyColumn {
                items(list) { userName ->
                    createUser(name = userName)
                }
        }
    }
}

    @Composable
    private fun createUser(name :String){

        Row(modifier = Modifier
            .padding(all = 8.dp)
            .clickable { }
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
}}