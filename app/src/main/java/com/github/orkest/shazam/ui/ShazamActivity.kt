package com.github.orkest.shazam.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.github.orkest.shazam.data.AudioRecognition
import com.github.orkest.shazam.data.AudioRecording
import com.github.orkest.shazam.domain.ShazamConstants
import kotlinx.coroutines.launch


    /**
     * Composable function for shazaming a song, should be launched in the background of the picture taking activity
     * Records AudioChunks continuously and asks for permission if needed
     */
     @SuppressLint("MissingPermission")
     @Composable
    fun ShazamSong(activity: Activity) {

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                val permission = ShazamConstants.recordPermissionGranted(context)
                //Asks for recording permission if not given
                if (!permission)
                    ShazamConstants.askRecordPermission(activity)
                else {
                    //Starts the recording and the recognition
                    Toast.makeText(context, "Shazaming ...",
                        Toast.LENGTH_LONG).show()

                    val title = AudioRecognition.recognizeSong(
                        coroutineScope, activity,
                        AudioRecording.recordingFlow(coroutineScope)
                    )

                    //Handles the result of the recognition
                    title.whenComplete { song, error ->
                        if (error != null) {
                            Log.d("ShazamActivity", "Error : $error")
                        } else {
                            //TODO: Send the song to the posting UI when found

                            Log.d("ShazamActivity", "Song : $song")
                            //NO MATCH WAS MADE
                            if (song == ShazamConstants.SONG_NO_MATCH) {
                                Toast.makeText(context, "No match found for this song",
                                    Toast.LENGTH_LONG).show()
                            } else {
                                //MATCH WAS MADE
                                Toast.makeText(context, "Song found : ${song.Title} by ${song.Artist}",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }