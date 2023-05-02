package com.github.orkest.ui

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.Glide
import com.github.orkest.R
import com.github.orkest.ui.ui.theme.OrkestTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class CloudStorageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrkestTheme {
                val context = LocalContext.current
                // Reference to an image file in Cloud Storage
                val storageRef = Firebase.storage.reference

                val islandRef = storageRef.child("screm.jpg")

                val localFile = File.createTempFile("images", "jpg")

                islandRef.getFile(localFile).addOnSuccessListener {
                    // Local temp file has been created
                    Toast.makeText(context, ":D", Toast.LENGTH_SHORT)
                    print(it)
                }.addOnFailureListener {
                    // Handle any errors
                    Toast.makeText(context, "AHHHH", Toast.LENGTH_SHORT)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    OrkestTheme {
        val context = LocalContext.current
        // Reference to an image file in Cloud Storage
        val storageRef = Firebase.storage.reference

        val islandRef = storageRef.child("screm.jpg")

        val localFile = File.createTempFile("images", "jpg")

        islandRef.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created
            Toast.makeText(context, ":D", Toast.LENGTH_SHORT)
            print(it)
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(context, "AHHHH", Toast.LENGTH_SHORT)
        }
    }
}