package com.github.orkest.shazam.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.github.orkest.data.Song
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
                    Toast.makeText(context, "Shazaming ...", Toast.LENGTH_LONG).show()

                    //Starts the recording and the recognition
                    val title = AudioRecognition.recognizeSong(coroutineScope, activity,
                        AudioRecording.recordingFlow(coroutineScope)
                    )

                    //Handles the result of the recognition
                    title.whenComplete { song, error ->
                        handleRecognitionResult(song, error, context) }

                }
            }
        }
    }

    /**
     * Handles the result of the recognition
     * Displays different toasts depending on the result
     * @param song the song found
     * @param error the error
     * @param context the context
     */
    private fun handleRecognitionResult(song: Song, error: Throwable?, context: Context) {

        if (error != null) {
            Log.d("ShazamActivity", "Error : $error")
        } else {
            Log.d("ShazamActivity", "Song : $song")
            //NO MATCH WAS MADE
            if (song == ShazamConstants.SONG_NO_MATCH) {
                Toast.makeText(
                    context, "No match found for this song",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //MATCH WAS MADE
                Toast.makeText(
                    context, "Song found : ${song.Title} by ${song.Artist}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }