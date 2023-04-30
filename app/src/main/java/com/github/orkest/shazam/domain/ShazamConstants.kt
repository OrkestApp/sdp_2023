package com.github.orkest.shazam.domain

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.orkest.data.Song

class ShazamConstants {
    companion object{
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        const val RECORDING_SAMPLE_RATE: Int  = 48_000
        const val SHAZAM_SESSION_READ_BUFFER_SIZE: Int = 4096

        val SONG_NO_MATCH = Song("No Match", "No Match")

        /**
         * Returns true if the app has the RECORD_AUDIO permission.
         */
        fun recordPermissionGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * Requests the RECORD_AUDIO permission.
         * This method shows the "Allow access to the microphone?" dialog.
         */
         fun askRecordPermission(activity: Activity){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf( Manifest.permission.RECORD_AUDIO),
                    ShazamConstants.REQUEST_RECORD_AUDIO_PERMISSION);
            }

        }
    }
}