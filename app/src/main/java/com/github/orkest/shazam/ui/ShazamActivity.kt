package com.github.orkest.shazam.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.github.orkest.R
import com.github.orkest.data.Song
import com.github.orkest.shazam.data.AudioChunk
import com.github.orkest.shazam.domain.AudioRecognition
import com.github.orkest.shazam.domain.AudioRecording
import com.github.orkest.shazam.domain.ShazamConstants
import com.github.orkest.ui.theme.OrkestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


/**
 * Activity for shazaming a song, should be launched in the background of the picture taking activity
 */
class ShazamActivity : AppCompatActivity() {
    lateinit var coroutineScope : CoroutineScope
    lateinit var activity: Activity
    var shazamFinished = mutableStateOf(false)


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    activity = this@ShazamActivity
                    coroutineScope = rememberCoroutineScope()

                    shazamSong()
                }
            }
        }
    }


    /**
     * Records AudioChunks continuously and asks for permission if needed
     */

    @SuppressLint("MissingPermission")
     private fun shazamSong() {
        coroutineScope.launch {
            val permission = ShazamConstants.recordPermissionGranted(activity)
            //Asks for recording permission if not given
            if (!permission)
                ShazamConstants.askRecordPermission(activity)
            else {
                //Starts the recording and the recognition
                val title = AudioRecognition.recognizeSong(
                    coroutineScope, activity,
                    AudioRecording.recordingFlow(coroutineScope)
                )

                //Handles the result of the recognition
                title.whenComplete { song, error ->
                    shazamFinished.value = true
                    if (error != null) {
                        Log.d("ShazamActivity", "Error : $error")
                    } else {
                        //TODO: Send the song to the posting UI when found

                        Log.d("ShazamActivity", "Song : $song")
                        //NO MATCH WAS MADE
                        if (song == ShazamConstants.SONG_NO_MATCH) {
                            Toast.makeText(
                                this@ShazamActivity,
                                "No match found for this song",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            //MATCH WAS MADE
                            Toast.makeText(
                                this@ShazamActivity,
                                "Song found : ${song.Title} by ${song.Artist}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    /**
     * Callback for the result from requesting permissions.
     * This method is invoked for every call on [ActivityCompat.requestPermissions]
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("ShazamActivity", "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ShazamConstants.REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                shazamSong()
            } else {
                // Permission denied
                //Display a text box dialog to inform user that the app needs the permission to work
                Toast.makeText(this, "The app needs the microphone access to analyze " +
                        "which song you are listening to", Toast.LENGTH_LONG).show()
            }
        }
    }
}



